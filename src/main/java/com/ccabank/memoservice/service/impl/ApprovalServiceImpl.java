package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.constant.AppError;
import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.email.EmailAskApprovalDto;
import com.ccabank.memoservice.dto.memo.*;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.entity.*;
import com.ccabank.memoservice.exception.BadRequestException;
import com.ccabank.memoservice.mappers.RequestMapper;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.*;
import com.ccabank.memoservice.service.faces.*;
import com.ccabank.memoservice.util.DateUtil;
import com.ccabank.memoservice.util.camunda.Mapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;
import static com.ccabank.memoservice.util.DateUtil.isDatePassed;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalServiceImpl.class);


    @Autowired
    private RequestMapper requestInfoMapper;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private CamundaService camundaService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private Mapping mapping;

    @Autowired
    private ApprobationRepository approbationRepository;

    @Autowired
    private ApprovalKeyRepository approvalKeyRepository;

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private EmailService emailService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private MapService mapService;


    @Override
    public AppServiceResult<?> reassign(ReassignDto reassignDto)  {
        try {
            Task task = camundaService.getTaskDetails(reassignDto.getIdApproval());
            String instanceId = task.getProcessInstanceId();
            Request request = requestRepository.findByInstanceId(instanceId);
            request.setLastModification(LocalDateTime.now());
            requestRepository.save(request);
            camundaService.assignTask(instanceId, task.getId(), reassignDto.getStaff());
            return new AppServiceResult<>(true, 0, "Succeed!", null);

        } catch (Exception e) {
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);
        }
    }

    @Override
    public AppServiceResult<?> freeless(HttpServletRequest request, TakeLeaveDto takeLeaveDto)  {

        EmployeeInfo employeeInfo = securityService.getCurrentUser(request);
        System.out.println("UserName Employe " + employeeInfo.getUsername());
        Task task = camundaService.getTaskDetails(takeLeaveDto.getIdApproval());
        if(task != null){
            if(takeLeaveDto.isDecision()){

                if(task.getAssignee() != null){
                    throw new NotAuthorizedException("Cette tâche est déjà prise");
                }

                if(camundaService.isTaskCandidateGroup(task.getId())){
                    if(!camundaService.isUserInCandidateGroups(task.getId(), employeeInfo.getUsername())){
                        throw new NotAuthorizedException("Vous n'etes pas autorisé à complete cette tâche");
                    }
                }

                camundaService.setProcessVariable(task.getProcessInstanceId(), task.getId(), employeeInfo.getUsername());
                camundaService.claimTask(task.getId(), employeeInfo.getUsername());
                return new AppServiceResult<>(true, 0, "Succeed! Claim", null);
            }else{

                if(camundaService.isTaskCandidateGroup(task.getId())){
                    if(!camundaService.isUserInCandidateGroups(task.getId(), employeeInfo.getUsername())){
                        throw new NotAuthorizedException("Vous n'etes pas autorisé à complete cette tâche");
                    }
                }
                camundaService.setProcessVariable(task.getProcessInstanceId(), task.getId(), null);
                camundaService.claimTask(task.getId(), null);
                return new AppServiceResult<>(true, 0, "Succeed! Unclaim", null);

            }
        }
        return new AppServiceResult<>(false, 0, "No Task Id", null);
    }

    @Override
    public AppServiceResult<?> decision(HttpServletRequest request, AcceptedApprovalDto acceptedApprovalDto)  {

        EmployeeInfo employeeInfo = securityService.getCurrentUser(request);
        System.out.println("UserName Employe " + employeeInfo.getUsername());
        Task task = camundaService.getTaskDetails(acceptedApprovalDto.getIdApproval());

        if(task != null){
            camundaService.setProcessVariable(task.getProcessInstanceId(), task.getId(), employeeInfo.getUsername());
            if(task.getAssignee() != null){
                if(!task.getAssignee().equals(employeeInfo.getUsername())){
                    throw new NotAuthorizedException("Vous n'etes pas autorisé à complete cette tâche");
                }
            }
            camundaService.claimTask(task.getId(), employeeInfo.getUsername());
        }

        if(acceptedApprovalDto.isDecision()){
            return this.approve(acceptedApprovalDto, employeeInfo.getUsername());
        }else{
            return this.rejected(acceptedApprovalDto);

        }
    }

    @Override
    public AppServiceResult<?> decisionViaEmail(String key, String TaskId, boolean decision, String comment)  {

        ApprovalKey approvalKey = approvalKeyRepository.getOne(key);
        if(approvalKey == null){
            throw new NotFoundException("Approval key not found");
        }

        if(!approvalKey.getTaskId().equals(TaskId)){
            throw new NotAuthorizedException("Approval key doesn't match");
        }

        EmployeeInfo employeeInfo = userRestClient.getStaffByUsername(approvalKey.getUsername());
        System.out.println("UserName Employe " + employeeInfo.getUsername());
        Task task = camundaService.getTaskDetails(approvalKey.getTaskId());

        if(task != null){
            camundaService.setProcessVariable(task.getProcessInstanceId(), task.getId(), employeeInfo.getUsername());
            if(task.getAssignee() != null){
                if(!task.getAssignee().equals(employeeInfo.getUsername())){
                    //throw new NotAuthorizedException("Vous n'etes pas autorisé à complete cette tâche");
                }
            }

            camundaService.claimTask(task.getId(), employeeInfo.getUsername());

        }

        AcceptedApprovalDto acceptedApprovalDto = new AcceptedApprovalDto();
        acceptedApprovalDto.setIdApproval(approvalKey.getTaskId());
        acceptedApprovalDto.setDecision(decision);
        acceptedApprovalDto.setComments(comment);

        if(acceptedApprovalDto.isDecision()){
            return this.approve(acceptedApprovalDto, employeeInfo.getUsername());
        }else{
            return this.rejected(acceptedApprovalDto);
        }
    }


    @Transactional
    @Override
    public AppServiceResult<?> approve(AcceptedApprovalDto acceptedApprovalDto, String assignee)  {

        boolean signature = securityService.checkUserSignature(assignee);
        if(!signature){
            throw new BadRequestException("l'utilisateur " + assignee + " n'a pas de signature");
        }

        logger.info(MEMO_SERVICE + "approve : methode invocation");
        List<FieldDto> incommingFields = acceptedApprovalDto.getFields();
        if(incommingFields == null){
            incommingFields = new ArrayList<>();
        }
        Task task = camundaService.getTaskDetails(acceptedApprovalDto.getIdApproval());
        camundaService.addLocalVariableToTask(task.getId(), "signature", true);
        String instanceId = task.getProcessInstanceId();
        Request request = requestRepository.findByInstanceId(instanceId);
        request.setLastModification(LocalDateTime.now());

        if(!request.getStatus().equals(RequestStatus.ACCEPTED)){
            request.setStatus(RequestStatus.PENDING);
        }
        requestRepository.save(request);


        Map<String, Object> variables = new HashMap<>();
        try {
            variables = mapping.getVariablesFromField(incommingFields);
        } catch (Exception e) {
            throw new BadRequestException("Soucis avec le mapping des variables");
        }

        if(!incommingFields.isEmpty()){
            List<FieldDto> oldFields = (List<FieldDto>) variables.get("otherFields");
            System.out.println("Begin add other fields");
            if(oldFields == null){
                oldFields =new ArrayList<>();
            }
            incommingFields.addAll(oldFields);
            System.out.println("Add Fields : " + incommingFields);
            variables.put("otherFields", incommingFields);
        }

        variables.put("decision", true);
        variables.put(task.getTaskDefinitionKey(), assignee);
        camundaService.claimTask(task.getId(), assignee);
        variables.put("comments", acceptedApprovalDto.getComments());
        camundaService.completeTask(task.getId(), variables);

        Optional<Approbation>  approbationOptional = approbationRepository.findByTaskId(task.getId());
        if(approbationOptional.isPresent()){
            Approbation approbation = approbationOptional.get();
            approbation.setStatus(ApprovalStatus.ACCEPTED);
            approbation.setComments(acceptedApprovalDto.getComments());
            approbationRepository.save(approbation);
        }else {
        Approbation approbation = new Approbation();
            approbation.setStatus(ApprovalStatus.ACCEPTED);
            approbation.setComments(acceptedApprovalDto.getComments());
            approbation.setTaskId(task.getId());
            approbationRepository.save(approbation);
        }

        return new AppServiceResult<>(true, 0, "Succeed!", null);

    }

    @Transactional
    @Override
    public AppServiceResult<?> rejected(AcceptedApprovalDto acceptedApprovalDto) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");

            if (acceptedApprovalDto.getComments() == null) {
                throw new Exception("Le commentaires est obligatoire en cas de refus");
            }


            Task task = camundaService.getTaskDetails(acceptedApprovalDto.getIdApproval());
            camundaService.addLocalVariableToTask(task.getId(), "signature", false);
            String instanceId = task.getProcessInstanceId();
            Request request = requestRepository.findByInstanceId(instanceId);

            request.setStatus(RequestStatus.REJECTED);
            request.setLastModification(LocalDateTime.now());
            requestRepository.save(request);

            Map<String, Object> variables = new HashMap<>();
            variables.put("decision", false);
            variables.put("comments", acceptedApprovalDto.getComments());
            request.setComments(acceptedApprovalDto.getComments());
            camundaService.completeTask(task.getId(), variables);

            Optional<Approbation>  approbationOptional = approbationRepository.findByTaskId(task.getId());
            if(approbationOptional.isPresent()){
                Approbation approbation = approbationOptional.get();
                approbation.setStatus(ApprovalStatus.REJECTED);
                approbation.setComments(acceptedApprovalDto.getComments());
                approbationRepository.save(approbation);
            }else {
                Approbation approbation = new Approbation();
                approbation.setStatus(ApprovalStatus.REJECTED);
                approbation.setComments(acceptedApprovalDto.getComments());
                approbation.setTaskId(task.getId());
                approbationRepository.save(approbation);
            }

            this.requestRepository.save(request);
            return new AppServiceResult<>(true, 0, "Succeed!", null);


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<ApprovalDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }


    @Override
    public AppServiceResult<List<ApprovalListDto>> getApprovalByStaff(HttpServletRequest req, String status) {
        try {
            EmployeeInfo employeeInfo = securityService.getCurrentUser(req);

            if(employeeInfo == null){
                System.out.println("EmployeeInfo is null");
            }

            System.out.println("UserName Employe" + employeeInfo.getUsername());

            List<Task> tasks = new ArrayList<>();
            List<HistoricTaskInstance> historicTaskInstances = new ArrayList<>();
            List<ApprovalListDto> approvalDtos = new ArrayList<>();


            switch (status){
                case "WAITING":
                    tasks = camundaService.getActiveTasksForUser(employeeInfo.getUsername());
                    tasks.sort(Comparator.comparing(Task::getCreateTime).reversed());
                    System.out.println("Get Tasks " + tasks.size());
                    approvalDtos = this.mapTaskToApprovalDto(tasks);
                    System.out.println("Mapping Complete " + tasks.size());
                break;

                case "ACCEPTED":
                    historicTaskInstances = camundaService.getConfirmTasksForUser(employeeInfo.getUsername(), true);
                   // historicTaskInstances.sort(Comparator.comparing(HistoricTaskInstance::getEndTime).reversed());
                    System.out.println("Get Accepted Tasks " + historicTaskInstances.size());
                    approvalDtos = this.mapHistoryTaskToApprovalDto(historicTaskInstances, status);
                    System.out.println("Mapping Complete " + historicTaskInstances.size());
                break;

                case "REJECTED":
                    historicTaskInstances = camundaService.getConfirmTasksForUser(employeeInfo.getUsername(), false);
                   // historicTaskInstances.sort(Comparator.comparing(HistoricTaskInstance::getEndTime).reversed());
                    System.out.println("Get Rejected Tasks " + historicTaskInstances.size());
                    approvalDtos = this.mapHistoryTaskToApprovalDto(historicTaskInstances, status);
                    System.out.println("Mapping Complete " + historicTaskInstances.size());
                break;
            }


            return new AppServiceResult<List<ApprovalListDto>>(true, 0, "Succeed!", approvalDtos);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<List<ApprovalListDto>>(false, AppError.Unknown.errorCode(), e.getMessage(), null);
        }
    }

    @Override
    public AppServiceResult<List<ApprovalListDto>> getAllApprobations(HttpServletRequest req) {
        try {
            EmployeeInfo employeeInfo = securityService.getCurrentUser(req);

            if(employeeInfo == null){
                System.out.println("EmployeeInfo is null");
            }

            System.out.println("UserName Employe" + employeeInfo.getUsername());

            List<Task> tasks = new ArrayList<>();
            List<HistoricTaskInstance> historicTaskInstances = new ArrayList<>();
            List<ApprovalListDto> approvalDtos = new ArrayList<>();

            tasks = camundaService.getAllTasksForUser();
            tasks.sort(Comparator.comparing(Task::getCreateTime).reversed());
            System.out.println("Get Tasks " + tasks.size());
            approvalDtos = this.mapTaskToApprovalDto(tasks);
            System.out.println("Mapping Complete " + tasks.size());

            return new AppServiceResult<List<ApprovalListDto>>(true, 0, "Succeed!", approvalDtos);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<List<ApprovalListDto>>(false, AppError.Unknown.errorCode(), e.getMessage(), null);
        }
    }


    public ApprovalListDto mapOneTaskToApprovalDto(Task task, int position) {

        String instanceId = task.getProcessInstanceId();
        Request request = requestRepository.findByInstanceId(instanceId);
        RequestInfo requestInfo = requestInfoMapper.toDto(request);

        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = camundaService.getProcessDefinition(processDefinitionId);
        DocumentType documentType = documentTypeRepository.findOneByStructure(processDefinition.getKey());
        requestInfo.setDocumentType(documentType.getName());



        System.out.println("Task " + position);

        ApprovalListDto approvalDto = new ApprovalListDto();
        StartFormData formData = camundaService.getStartForm(request.getType().getStructure());
        Map<String, Object> variables = camundaService.getProcessVariables(request.getInstanceId());
        List<FieldDto> formfield = Mapping.getFieldFromFormField(formData, variables);
        List<FieldDto> oldFields = (List<FieldDto>) variables.get("otherFields");

        if(oldFields != null){
            formfield.addAll(oldFields);
        }

        requestInfo.setFields(formfield);

        approvalDto.setRequest(requestInfo);
        approvalDto.setId(task.getId());
        approvalDto.setRole(task.getName());
        approvalDto.setPosition(position);
        approvalDto.setStaff(task.getAssignee());
        approvalDto.setStatus(ApprovalStatus.WAITING);
        approvalDto.setFields(new ArrayList<>());
        approvalDto.setType(ApprovalType.OPEN);
        approvalDto.setPriority(task.getPriority());
        approvalDto.setDueDate(task.getDueDate());

        if(task.getCreateTime() != null){
            approvalDto.setTime(DateUtil.timeAgo(DateUtil.convertDateToLocalDateTime(task.getCreateTime())));
        }

        if(task.getId() != null){
            HistoricTaskInstance taskInstance = camundaService.getHistoryTaskInstance(task.getId());
            if(taskInstance != null){
                System.out.println("Get Task " + taskInstance.toString());
                if(taskInstance.getEndTime() != null){
                    approvalDto.setApprovalDate(DateUtil.convertDateToLocalDateTime(taskInstance.getEndTime()));
                    approvalDto.setTime(DateUtil.timeAgo(DateUtil.convertDateToLocalDateTime(taskInstance.getEndTime())));
                }
            }
        }

        FormData data = null;

        try {
            data =  camundaService.getFormData(task.getId());
        }catch (Exception e){
            System.out.println("Erreur lors de la recupération de la form Data : " + e.getMessage() );
            return null;
        }

        List<FieldDto> outFields = new ArrayList<>();

        System.out.println("Boucle in Fields " + position);

        for (FormField f : data.getFormFields() ) {
            System.out.println("Field " + f.getLabel());


            if(f.getProperties().isEmpty()){
                System.out.println("Properties is empty " + f.getLabel());
                continue;
            }

            if(f.getProperties().get("fieldType").isEmpty() ){
                System.out.println("Is not empty " + f.getLabel());
                continue;
            }

            if(!f.getProperties().get("fieldType").equals("field")){
                System.out.println("Is not fielType field " + f.getLabel());
                continue;
            }


            FieldDto field = new FieldDto();
            field.setKey(f.getId());
            System.out.println("Add type " + f.getLabel());

            field.setType(f.getProperties().get("type"));


            if(f.getProperties().get("type").equals("choice")){
                System.out.println("Is Choice " + f.getLabel());
                Object choices = camundaService.getProcessVariable(request.getInstanceId(), f.getId() + "_choices");
                if(choices != null){
                    field.setChoices((List<ChoiceDto>) choices);
                }else{
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<ChoiceDto> choicesString = new ArrayList<>();
                    try {
                        choicesString = objectMapper.readValue(f.getProperties().get("choices"),  new TypeReference<List<ChoiceDto>>() {});
                    } catch (JsonProcessingException e) {
                        System.out.println("JSON Not Valid Exception " + e.getMessage());
                    }
                    field.setChoices(choicesString);
                }
            }

            field.setPosition(data.getFormFields().indexOf(f) + 1);
            field.setName(f.getLabel());
            System.out.println("Add required " + f.getLabel());
            field.setRequired(f.getProperties().get("required").equals("true"));
            if(f.getDefaultValue() != null){
                field.setValue(String.valueOf(f.getDefaultValue()));
                field.setDefaultValue(String.valueOf(f.getDefaultValue()));
            }

            if(variables.get(f.getId()) != null){
                field.setValue(String.valueOf(variables.get(f.getId())));
                field.setDefaultValue(String.valueOf(variables.get(f.getId())));
            }

            outFields.add(field);
            System.out.println("Add complete " + f.getLabel());
        }

        approvalDto.setFields(outFields);

        return approvalDto;

    }

    public ApprovalListDto mapOneHistoryTaskToApprovalDto(HistoricTaskInstance task, int position, String status) {

        String instanceId = task.getProcessInstanceId();
        Request request = requestRepository.findByInstanceId(instanceId);
        RequestInfo requestInfo = requestInfoMapper.toDto(request);

        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = camundaService.getProcessDefinition(processDefinitionId);
        DocumentType documentType = documentTypeRepository.findOneByStructure(processDefinition.getKey());
        requestInfo.setDocumentType(documentType.getName());

        System.out.println("Task " + position);

        ApprovalListDto approvalDto = new ApprovalListDto();
        StartFormData formData = camundaService.getStartForm(request.getType().getStructure());
        Map<String, Object> variables = camundaService.getProcessVariables(request.getInstanceId());
        requestInfo.setFields(Mapping.getFieldFromFormField(formData, variables));


        approvalDto.setRequest(requestInfo);
        approvalDto.setId(task.getId());
        approvalDto.setRole(task.getName());
        approvalDto.setPosition(position);
        approvalDto.setStaff(task.getAssignee());

        approvalDto.setStatus(ApprovalStatus.valueOf(status));
        approvalDto.setFields(new ArrayList<>());
        approvalDto.setType(ApprovalType.OPEN);
        approvalDto.setPriority(task.getPriority());
        approvalDto.setDueDate(task.getDueDate());
        approvalDto.setApprovalDate(DateUtil.convertDateToLocalDateTime(task.getEndTime()));

        return approvalDto;

    }


    public  List<ApprovalListDto> mapTaskToApprovalDto(List<Task> tasks) {

        List<ApprovalListDto> approvalDtos = new ArrayList<>();


        for (Task task : tasks) {

            ApprovalListDto approvalDto = this.mapOneTaskToApprovalDto(task, tasks.indexOf(task));
            if(approvalDto == null){
                continue;
            }

            approvalDtos.add(approvalDto);

        }

        return approvalDtos;
    }

    public  List<ApprovalListDto> mapHistoryTaskToApprovalDto(List<HistoricTaskInstance> tasks, String status) {
        List<ApprovalListDto> approvalDtos = new ArrayList<>();

        for (HistoricTaskInstance task : tasks) {
            ApprovalListDto approvalDto = this.mapOneHistoryTaskToApprovalDto(task, tasks.indexOf(task), status);
            if(approvalDto == null){
                continue;
            }
            approvalDtos.add(approvalDto);
        }
        return approvalDtos;
    }

    @Override
    public AppServiceResult<ApprovalListDto> getApprovalDetail(String id) {
        try {
            logger.info(MEMO_SERVICE + "getApprovalDetail : methode invocation");

            Task task = camundaService.getTaskDetails(id);

            ApprovalListDto approvalDto = this.mapOneTaskToApprovalDto(task, 0);

            return new AppServiceResult<ApprovalListDto>(true, 0, "Succeed!", approvalDto );


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " getApprovalDetail : Exception {}", e.getMessage());
            return new AppServiceResult<ApprovalListDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }



    private AppServiceResult<List<ApprovalDto>> getConvertedResult(List<ApprovalDto> approvals, String functionName) {
        if (approvals == null) {
            logger.warn(MEMO_SERVICE, functionName,
                    "Approval not exist!, Cannot further process!");
            return new AppServiceResult<List<ApprovalDto>>(false, AppError.Validattion.errorCode(),
                    "Approval not exist!", null);
        }
        List<ApprovalDto> result =  new ArrayList<ApprovalDto>();
        if (approvals.size() > 0) {

        }
        return new AppServiceResult<List<ApprovalDto>>(true, 0, "Succeed!", result);
    }

    @Override
    public AppServiceResult<?> relanceApprobation(String taskId){

        Task delegateTask = camundaService.getTaskDetails(taskId);


        System.out.println("SendEmailForValidation Task Listener");
        List<String> candidateUsers = getCandidateUserIds(delegateTask);
        EmailAskApprovalDto ask = new EmailAskApprovalDto();

        String processDefinitionId = delegateTask.getProcessDefinitionId();
        ProcessDefinition definition = camundaService.getProcessDefinition(processDefinitionId);

        Request request = requestRepository.findByInstanceId(delegateTask.getProcessInstanceId());
        ask.setType(request.getType().getName());

        String owner = request.getStaff();
        System.out.println("Owner :" + owner);
        String reference = request.getReference();
        ask.setSender(owner);
        ask.setReference(reference);

        ask.setSubject("[Action Requise] Relance Approbation de  - " + definition.getName());
        ask.setRole(delegateTask.getName());

        StartFormData formData = camundaService.getStartForm(request.getType().getStructure());
        Map<String, Object> variables = camundaService.getProcessVariables(request.getInstanceId());

        System.out.println("Recupération des Champs");
        List<FieldDto> fields = Mapping.getFieldFromFormField(formData, variables);

        System.out.println("Recupération des Approbations");
        List<HistoricTaskInstance> histories = camundaService.getHistoricTasksForProcessInstance(request.getInstanceId());
        List<ApprovalDto> approvalDtos = this.mapService.mapTaskToApprovalDto(histories);

        System.out.println(candidateUsers);

        for (String user : candidateUsers) {

            System.out.println("Envoi de mail a " + user);
            ApprovalListDto approvalDto = this.mapOneTaskToApprovalDto(delegateTask, 0);
            ask.setApprover(user);

            if(approvalDto.getFields().isEmpty()){
                ApprovalKey approvalKey = new ApprovalKey();
                approvalKey.setUsername(user);
                approvalKey.setTaskId(delegateTask.getId());
                approvalKey.setReference(request.getReference());
                approvalKeyRepository.save(approvalKey);
                emailService.sendForValidation(approvalKey, ask, fields, approvalDtos);
            }else{
                emailService.sendAskApproval(ask);
            }

        }
        return new AppServiceResult<>(true, 0, "Succeed!", null);
    }

    @Override
    public AppServiceResult<?> relanceForDueDate(String taskId){

        Task delegateTask = camundaService.getTaskDetails(taskId);


        System.out.println("relanceForDueDate Task Listener");
        List<String> candidateUsers = getCandidateUserIds(delegateTask);
        EmailAskApprovalDto ask = new EmailAskApprovalDto();
        String processDefinitionId = delegateTask.getProcessDefinitionId();
        ProcessDefinition definition = camundaService.getProcessDefinition(processDefinitionId);
        Request request = requestRepository.findByInstanceId(delegateTask.getProcessInstanceId());
        ask.setType(request.getType().getName());
        String reference = request.getReference();
        ask.setReference(reference);

        ask.setSubject("[Action Requise] Relance Approbation de  - " + definition.getName());
        ask.setRole(delegateTask.getName());

        StartFormData formData = camundaService.getStartForm(request.getType().getStructure());
        Map<String, Object> variables = camundaService.getProcessVariables(request.getInstanceId());

        System.out.println("Recupération des Champs");
        List<FieldDto> fields = Mapping.getFieldFromFormField(formData, variables);

        System.out.println("Recupération des Approbations");
        List<HistoricTaskInstance> histories = camundaService.getHistoricTasksForProcessInstance(request.getInstanceId());
        List<ApprovalDto> approvalDtos = this.mapService.mapTaskToApprovalDto(histories);

        System.out.println(candidateUsers);

        if(delegateTask.getDueDate() != null){
            if (isDatePassed(delegateTask.getDueDate())) {
                for (String user : candidateUsers) {
                    System.out.println("Envoi de mail a " + user);
                    ask.setApprover(user);
                    EmployeeInfo employeeInfo = userRestClient.getStaffByUsername(user);
                    ask.setSender(employeeInfo.getSupervisor().getUsername());
                    ApprovalListDto approvalDto = this.mapOneTaskToApprovalDto(delegateTask, 0);

                    if(approvalDto.getFields().isEmpty()){
                        ApprovalKey approvalKey = new ApprovalKey();
                        approvalKey.setUsername(user);
                        approvalKey.setTaskId(delegateTask.getId());
                        approvalKey.setReference(request.getReference());
                        approvalKeyRepository.save(approvalKey);
                        emailService.sendForValidation(approvalKey, ask, fields, approvalDtos);
                    }else{
                        emailService.sendAskApproval(ask);
                    }

                    emailService.sendAskApproval(ask);
                }
            }
        }

        return new AppServiceResult<>(true, 0, "Succeed!", null);
    }

    public List<String> getCandidateUserIds(Task delegateTask) {
        List<IdentityLink> candidates = camundaService.getTaskCandidates(delegateTask.getId());
        Set<String> userIds = new HashSet<>();

        // Récupération des userIds des utilisateurs
        for (IdentityLink link : candidates) {
            if (link.getUserId() != null) {
                userIds.add(link.getUserId());
            } else if (link.getGroupId() != null) {
                // Ajout des utilisateurs du groupe
                List<User> groupMembers = identityService.createUserQuery()
                        .memberOfGroup(link.getGroupId())
                        .list();
                userIds.addAll(groupMembers.stream()
                        .map(User::getId)
                        .collect(Collectors.toSet()));
            }
        }

        return List.copyOf(userIds);
    }
}
