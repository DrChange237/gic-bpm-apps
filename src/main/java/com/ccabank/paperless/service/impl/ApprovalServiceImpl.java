package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.dto.memo.*;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.dto.user.UserRestDto;
import com.ccabank.paperless.entity.*;
import com.ccabank.paperless.exception.BadRequestException;
import com.ccabank.paperless.mappers.RequestMapper;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.repository.*;
import com.ccabank.paperless.service.faces.*;
import com.ccabank.paperless.util.DateUtil;
import com.ccabank.paperless.util.camunda.Mapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ccabank.paperless.util.DateUtil.isDatePassed;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final RequestMapper requestInfoMapper;

    private final RequestRepository requestRepository;

    private final DocumentTypeRepository documentTypeRepository;

    @Autowired
    private CamundaService camundaService;

    private final SecurityService securityService;

    private final Mapping mapping;

    private final ApprobationRepository approbationRepository;

    private final ApprovalKeyRepository approvalKeyRepository;

    private final UserRestClient userRestClient;

    private final EmailService emailService;

    private final IdentityService identityService;

    private final MapService mapService;

    @Override
    public void reassign(ReassignDto reassignDto)  {

            Task task = camundaService.getTaskDetails(reassignDto.getIdApproval());
            String instanceId = task.getProcessInstanceId();
            Request request = requestRepository.findByInstanceId(instanceId);
            if(!request.getStatus().equals(RequestStatus.PENDING)){
                throw new BadRequestException("Request is not PENDING");
            }
            request.setLastModification(LocalDateTime.now());
            requestRepository.save(request);

            EmailAskApprovalDto ask = new EmailAskApprovalDto();

            String apbt_interimaire = reassignDto.getStaff();
            String owner = request.getStaff();
            String reference = request.getReference();
            ask.setSender(owner);
            ask.setApprover(apbt_interimaire);
            ask.setReference(reference);
            ask.setType(request.getType().getName());

            ask.setSubject("Demande d'approbation - " + request.getType().getName());
            if(task != null){
                ask.setRole(task.getName());
            }else{
                ask.setRole("R.A.S");
            }

            emailService.sendAskApproval(ask);
            camundaService.assignTask(instanceId, task.getId(), reassignDto.getStaff());
    }

    @Override
    public void freeless(HttpServletRequest request, TakeLeaveDto takeLeaveDto)  {

        EmployeeInfo employeeInfo = securityService.getCurrentUser();
        log.info("Username " + employeeInfo.getUsername());
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
            }else{

                if(camundaService.isTaskCandidateGroup(task.getId())){
                    if(!camundaService.isUserInCandidateGroups(task.getId(), employeeInfo.getUsername())){
                        throw new NotAuthorizedException("Vous n'etes pas autorisé à complete cette tâche");
                    }
                }
                camundaService.setProcessVariable(task.getProcessInstanceId(), task.getId(), null);
                camundaService.claimTask(task.getId(), null);

            }
        }
    }

    @Override
    public void decision(HttpServletRequest request, AcceptedApprovalDto acceptedApprovalDto)  {

        EmployeeInfo employeeInfo = securityService.getCurrentUser();
        log.info("Username " + employeeInfo.getUsername());
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
            this.approve(acceptedApprovalDto, employeeInfo.getUsername());
        }else{
            this.rejected(acceptedApprovalDto);
        }
    }

    @Override
    public void decisionViaEmail(String key, String TaskId, boolean decision, String comment)  {

        ApprovalKey approvalKey = approvalKeyRepository.getOne(key);

        if(approvalKey == null){
            throw new NotFoundException("Approval key not found");
        }

        if(!approvalKey.getTaskId().equals(TaskId)){
            throw new NotAuthorizedException("Approval key doesn't match");
        }

        UserRestDto employeeInfo = userRestClient.getAgencyByStaffUsername(approvalKey.getUsername());
        log.info("UserName Employe " + employeeInfo.getUsername());
        Task task = camundaService.getTaskDetails(approvalKey.getTaskId());

        if(task != null){
            camundaService.setProcessVariable(task.getProcessInstanceId(), task.getId(), employeeInfo.getUsername());
            if(task.getAssignee() != null){
                if(!task.getAssignee().equals(employeeInfo.getUsername())){
                    //throw new NotAuthorizedException("Vous n'etes pas autorisé à complete cette tâche");
                }
            }

            //camundaService.claimTask(task.getId(), employeeInfo.getUsername());

        }

        AcceptedApprovalDto acceptedApprovalDto = new AcceptedApprovalDto();
        acceptedApprovalDto.setIdApproval(approvalKey.getTaskId());
        acceptedApprovalDto.setDecision(decision);
        acceptedApprovalDto.setComments(comment);

        if(acceptedApprovalDto.isDecision()){
            this.approve(acceptedApprovalDto, employeeInfo.getUsername());
        }else{
            this.rejected(acceptedApprovalDto);
        }
    }



    @Override
    public void approve(AcceptedApprovalDto acceptedApprovalDto, String assignee)  {

        boolean signature = securityService.checkUserSignature(assignee);
        if(!signature){
            throw new BadRequestException("l'utilisateur " + assignee + " n'a pas de signature");
        }

        log.info(" approve : methode invocation");
        List<FieldDto> incommingFields = acceptedApprovalDto.getFields();
        if(incommingFields == null){
            incommingFields = new ArrayList<>();
        }
        Task task = camundaService.getTaskDetails(acceptedApprovalDto.getIdApproval());
        if(task == null){
            log.warn("Task is null");
        }
        camundaService.addLocalVariableToTask(task.getId(), "signature", true);
        String instanceId = task.getProcessInstanceId();
        Request request = requestRepository.findByInstanceId(instanceId);
        request.setLastModification(LocalDateTime.now());

        if(!request.getStatus().equals(RequestStatus.ACCEPTED)){
            request.setStatus(RequestStatus.PENDING);
        }
        requestRepository.save(request);


        Map<String, Object> variables;
        try {
            variables = mapping.getVariablesFromField(incommingFields);
        } catch (Exception e) {
            throw new BadRequestException("Soucis avec le mapping des variables");
        }

        if(!incommingFields.isEmpty()){
            List<FieldDto> oldFields = (List<FieldDto>) variables.get("otherFields");
            if(oldFields == null){
                oldFields =new ArrayList<>();
            }
            incommingFields.addAll(oldFields);
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
            approbation.setStaff(assignee);
            approbation.setReference(request.getReference());
            approbation.setStatus(ApprovalStatus.ACCEPTED);
            approbation.setComments(acceptedApprovalDto.getComments());
            approbationRepository.save(approbation);
        } else {
            Approbation approbation = new Approbation();
            approbation.setStaff(assignee);
            approbation.setReference(request.getReference());
            approbation.setStatus(ApprovalStatus.ACCEPTED);
            approbation.setComments(acceptedApprovalDto.getComments());
            approbation.setTaskId(task.getId());
            approbationRepository.save(approbation);
        }
    }


    @Override
    public void rejected(AcceptedApprovalDto acceptedApprovalDto) {
        try {
            log.info("newRequest : methode invocation");

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
                approbation.setReference(request.getReference());
                approbation.setStatus(ApprovalStatus.REJECTED);
                approbation.setComments(acceptedApprovalDto.getComments());
                approbationRepository.save(approbation);
            }else {
                Approbation approbation = new Approbation();
                approbation.setReference(request.getReference());
                approbation.setStatus(ApprovalStatus.REJECTED);
                approbation.setComments(acceptedApprovalDto.getComments());
                approbation.setTaskId(task.getId());
                approbationRepository.save(approbation);
            }

            this.requestRepository.save(request);


        } catch (Exception e) {
            log.error("addFeedback : Exception ", e);
        }
    }


    @Override
    public List<ApprovalListDto> getApprovalByStaff(HttpServletRequest req, String status) {
        EmployeeInfo employeeInfo = securityService.getCurrentUser();

        if(employeeInfo == null){
            log.info("EmployeeInfo is null");
        }

        log.info("UserName Employe" + employeeInfo.getUsername());

        List<Task> tasks;
        List<ApprovalListDto> approvalDtos = new ArrayList<>();
        List<Approbation> approbations;


        switch (status){
            case "WAITING":
                tasks = camundaService.getActiveTasksForUser(employeeInfo.getUsername());
                tasks.sort(Comparator.comparing(Task::getCreateTime).reversed());
                approvalDtos = this.mapTaskToApprovalDto(tasks);
                break;

            case "ACCEPTED":
                approbations = approbationRepository.findByStaffAndStatus(employeeInfo.getUsername(), ApprovalStatus.ACCEPTED);
                approvalDtos = this.mapApprobationToApprovalDto(approbations, status);
                break;

            case "REJECTED":
                approbations = approbationRepository.findByStaffAndStatus(employeeInfo.getUsername(), ApprovalStatus.REJECTED);
                approvalDtos = this.mapApprobationToApprovalDto(approbations, status);
                break;
        }
        
        return approvalDtos;
    }

    @Override
    public List<ApprovalListDto> getAllApprobations(HttpServletRequest req) {
        EmployeeInfo employeeInfo = securityService.getCurrentUser();

        if(employeeInfo == null){
            log.info("EmployeeInfo is null");
        }

        log.info("UserName Employe" + employeeInfo.getUsername());

        List<Task> tasks;
        List<ApprovalListDto> approvalDtos;

        tasks = camundaService.getAllTasksForUser();
        tasks.sort(Comparator.comparing(Task::getCreateTime).reversed());
        approvalDtos = this.mapTaskToApprovalDto(tasks);

        return approvalDtos;
    }


    public ApprovalListDto mapOneTaskToApprovalDto(Task task, int position) {

        String instanceId = task.getProcessInstanceId();
        Request request = requestRepository.findByInstanceId(instanceId);
        RequestInfo requestInfo = requestInfoMapper.toDto(request);

        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = camundaService.getProcessDefinition(processDefinitionId);
        DocumentType documentType = documentTypeRepository.findOneByStructure(processDefinition.getKey());
        requestInfo.setDocumentType(documentType.getName());

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
            log.info("Erreur lors de la recupération de la form Data : " + e.getMessage() );
            return null;
        }

        List<FieldDto> outFields = new ArrayList<>();


        for (FormField f : data.getFormFields() ) {
            log.info("Field " + f.getLabel());


            if(f.getProperties().isEmpty()){
                log.info("Properties is empty " + f.getLabel());
                continue;
            }

            if(f.getProperties().get("fieldType") == null){
                camundaService.deleteProcessInstance(request.getInstanceId());
                request.setStatus(RequestStatus.SUSPENDED);
                requestRepository.save(request);
            }

            if(f.getProperties().get("fieldType").isEmpty() ){
                log.info("Is not empty " + f.getLabel());
                continue;
            }


            if(!f.getProperties().get("fieldType").equals("field")){
                log.info("Is not fielType field " + f.getLabel());
                continue;
            }


            FieldDto field = new FieldDto();
            field.setKey(f.getId());

            field.setType(f.getProperties().get("type"));


            if(f.getProperties().get("type").equals("choice")){
                try{
                log.info("Is Choice " + f.getLabel());
                Object choices = camundaService.getProcessVariable(request.getInstanceId(), f.getId() + "_choices");
                if(choices != null){
                        field.setChoices((List<ChoiceDto>) choices);
                }else{
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<ChoiceDto> choicesString = new ArrayList<>();
                    try {
                        choicesString = objectMapper.readValue(f.getProperties().get("choices"),  new TypeReference<List<ChoiceDto>>() {});
                    } catch (JsonProcessingException e) {
                        log.info("JSON Not Valid Exception " + e.getMessage());
                    }
                    field.setChoices(choicesString);
                }
                }catch (Exception e){
                    log.warn(" mapOneTaskToApprovalDto : Exception {}", e.getMessage());
                }
            }

            field.setPosition(data.getFormFields().indexOf(f) + 1);
            field.setName(f.getLabel());
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

    public  List<ApprovalListDto> mapApprobationToApprovalDto(List<Approbation> approbations, String status) {
        List<ApprovalListDto> approvalDtos = new ArrayList<>();

        for (Approbation approbation : approbations) {
            HistoricTaskInstance task = camundaService.getHistoryTaskInstance(approbation.getTaskId());
            ApprovalListDto approvalDto = this.mapOneHistoryTaskToApprovalDto(task, approbations.indexOf(approbation), status);
            if(approvalDto == null){
                continue;
            }
            approvalDtos.add(approvalDto);
        }
        return approvalDtos;
    }

    @Override
    public ApprovalListDto getApprovalDetail(String id) {
        Task task = camundaService.getTaskDetails(id);
        return this.mapOneTaskToApprovalDto(task, 0);
    }

    @Override
    public void relanceApprobation(String taskId){

        Task delegateTask = camundaService.getTaskDetails(taskId);


        log.info("SendEmailForValidation Task Listener");
        List<String> candidateUsers = getCandidateUserIds(delegateTask);
        EmailAskApprovalDto ask = new EmailAskApprovalDto();

        String processDefinitionId = delegateTask.getProcessDefinitionId();
        ProcessDefinition definition = camundaService.getProcessDefinition(processDefinitionId);

        Request request = requestRepository.findByInstanceId(delegateTask.getProcessInstanceId());

        if(!RequestStatus.PENDING.equals(request.getStatus())) return;

        ask.setType(request.getType().getName());

        String owner = request.getStaff();
        log.info("Owner :" + owner);
        String reference = request.getReference();
        ask.setSender(owner);
        ask.setReference(reference);

        ask.setSubject("[Action Requise] Relance Approbation de  - " + definition.getName());
        ask.setRole(delegateTask.getName());

        StartFormData formData = camundaService.getStartForm(request.getType().getStructure());
        Map<String, Object> variables = camundaService.getProcessVariables(request.getInstanceId());

        log.info("Recupération des Champs");
        List<FieldDto> fields = Mapping.getFieldFromFormField(formData, variables);

        log.info("Recupération des Approbations");
        List<HistoricTaskInstance> histories = camundaService.getHistoricTasksForProcessInstance(request.getInstanceId());
        List<ApprovalDto> approvalDtos = this.mapService.mapTaskToApprovalDto(histories);



        for (String user : candidateUsers) {

            log.info("Envoi de mail a " + user);
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
    }

    @Override
    public void relanceForDueDate(String taskId){

        Task delegateTask = camundaService.getTaskDetails(taskId);


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

        log.info("Recupération des Champs");
        List<FieldDto> fields = Mapping.getFieldFromFormField(formData, variables);

        log.info("Recupération des Approbations");
        List<HistoricTaskInstance> histories = camundaService.getHistoricTasksForProcessInstance(request.getInstanceId());
        List<ApprovalDto> approvalDtos = this.mapService.mapTaskToApprovalDto(histories);


        if(delegateTask.getDueDate() != null){
            if (isDatePassed(delegateTask.getDueDate())) {
                for (String user : candidateUsers) {
                    log.info("Envoi de mail a " + user);
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
