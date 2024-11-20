package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.constant.AppError;
import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.*;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.entity.*;
import com.ccabank.memoservice.mappers.RequestMapper;
import com.ccabank.memoservice.repository.*;
import com.ccabank.memoservice.service.faces.*;
import com.ccabank.memoservice.util.camunda.Mapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;

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
    public AppServiceResult<?> decision(HttpServletRequest request, AcceptedApprovalDto acceptedApprovalDto)  {

        EmployeeInfo employeeInfo = securityService.getCurrentUser(request);
        System.out.println("UserName Employe" + employeeInfo.getUsername());
        Task task = camundaService.getTaskDetails(acceptedApprovalDto.getIdApproval());
        camundaService.claimTask(task.getId(), employeeInfo.getUsername());

        if(acceptedApprovalDto.isDecision()){
            return this.approve(acceptedApprovalDto, employeeInfo.getUsername());
        }else{
            return this.rejected(acceptedApprovalDto);

        }
    }



    public AppServiceResult<?> approve(AcceptedApprovalDto acceptedApprovalDto, String assignee) {
        try {
            logger.info(MEMO_SERVICE + "approve : methode invocation");

            List<FieldDto> incommingFields = acceptedApprovalDto.getFields();

            Task task = camundaService.getTaskDetails(acceptedApprovalDto.getIdApproval());
            String instanceId = task.getProcessInstanceId();
            Request request = requestRepository.findByInstanceId(instanceId);
            request.setLastModification(LocalDateTime.now());
            requestRepository.save(request);
            Map<String, Object> variables = mapping.getVariablesFromField(incommingFields);
            variables.put("decision", true);
            variables.put(task.getTaskDefinitionKey(), assignee);
            camundaService.claimTask(task.getId(), assignee);
            variables.put("comments", acceptedApprovalDto.getComments());
            camundaService.completeTask(task.getId(), variables);

            return new AppServiceResult<>(true, 0, "Succeed!", null);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }


    public AppServiceResult<?> rejected(AcceptedApprovalDto acceptedApprovalDto) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");

            if (acceptedApprovalDto.getComments() == null) {
                throw new Exception("Le commentaires est obligatoire en cas de refus");
            }


            Task task = camundaService.getTaskDetails(acceptedApprovalDto.getIdApproval());
            String instanceId = task.getProcessInstanceId();
            Request request = requestRepository.findByInstanceId(instanceId);

            request.setStatus(RequestStatus.REJECTED);
            request.setLastModification(LocalDateTime.now());
            requestRepository.save(request);

            Map<String, Object> variables = new HashMap<>();
            variables.put("decision", false);
            variables.put("comments", acceptedApprovalDto.getComments());

            camundaService.completeTask(task.getId(), variables);

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

            List<Task> tasks = camundaService.getActiveTasksForUser(employeeInfo.getUsername());

            System.out.println("Get Tasks " + tasks.size());

            List<ApprovalListDto> approvalDtos = this.mapTaskToApprovalDto(tasks);
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
        requestInfo.setFields(Mapping.getFieldFromFormField(formData, variables));
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

        /*Optional<HistoricTaskInstance> history = camundaService.getLastHistoricTaskInstance(request.getInstanceId(), task.getTaskDefinitionKey());
        if(history.isPresent()){
            approvalDto.setApprovalDate(history.get().getEndTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }*/

        FormData data =  camundaService.getFormData(task.getId());

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
            outFields.add(field);
            System.out.println("Add complete " + f.getLabel());
        }

        approvalDto.setFields(outFields);

        return approvalDto;

    }


    public  List<ApprovalListDto> mapTaskToApprovalDto(List<Task> tasks) {

        List<ApprovalListDto> approvalDtos = new ArrayList<>();


        for (Task task : tasks) {

            ApprovalListDto approvalDto = this.mapOneTaskToApprovalDto(task, tasks.indexOf(task));

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
}
