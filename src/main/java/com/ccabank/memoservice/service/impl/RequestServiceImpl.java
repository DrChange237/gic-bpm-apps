package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.constant.AppError;
import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.*;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.entity.*;
import com.ccabank.memoservice.mappers.RequestMapper;
import com.ccabank.memoservice.openfeign.FileRestClient;
import com.ccabank.memoservice.repository.*;
import com.ccabank.memoservice.service.faces.*;
import com.ccabank.memoservice.util.camunda.Mapping;
import com.ccabank.memoservice.util.file.FileUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class RequestServiceImpl implements RequestService {

    private static final Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;


    @Autowired
    private SecurityService securityService;

    @Autowired
    private CamundaService camundaService;


    @Override
    public AppServiceResult<Request> newRequest(RequestDto requestDto, HttpServletRequest req) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");

            EmployeeInfo employeeInfo = securityService.getCurrentUser(req);
            System.out.println("UserName Employe" + employeeInfo.getUsername());

            Request request = new Request();
            request.setCreatedAt(LocalDateTime.now());
            request.setLastModification(LocalDateTime.now());
            request.setStaff(employeeInfo.getUsername());
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
            String date = currentDate.format(formatter);
            Long count = requestRepository.countRequestsCreatedToday() + 1;
            date = date + "-" + count;
            request.setReference(employeeInfo.getReference() + "/" + date);
            DocumentType type = documentTypeRepository.findOneByStructure(requestDto.getDocumentType());
            request.setType(type);
            request.setStatus(RequestStatus.DRAFT);
            request.setApprobationLevel(0);
            request = requestRepository.save(request);

            Map<String, Object> variables = Mapping.getVariablesFromField(requestDto.getFields());
            Map<String, Object> variablesApprovals = Mapping.getVariablesFromApproval(requestDto.getApprovals());
            variables.putAll(variablesApprovals);
            variables.put("owner", requestDto.getStaff());
            variables.put("reference", requestDto.getReference());

            ProcessInstance instance = camundaService.createProcessInstance(request.getType().getStructure(), variables);

            request.setInstanceId(instance.getId());

            request = requestRepository.save(request);

            return new AppServiceResult<Request>(true, 0, "Succeed!", request );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " newRequest : Exception {}", e.getMessage());
            return new AppServiceResult<Request>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<Request> update(RequestDto requestDto) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");

            Request request = this.requestRepository.getOne(requestDto.getId());

            if(request.getStatus().equals(RequestStatus.ACCEPTED) || request.getStatus().equals(RequestStatus.PENDING) ){
                throw new Exception("Cette requete est déjà acceptée ou encore en cours");
            }

            request.setLastModification(LocalDateTime.now());
            request.setStatus(RequestStatus.DRAFT);
            request = requestRepository.save(request);

            Map<String, Object> variables = Mapping.getVariablesFromField(requestDto.getFields());
            Map<String, Object> variablesApprovals = Mapping.getVariablesFromApproval(requestDto.getApprovals());
            variables.putAll(variablesApprovals);
            variables.put("owner", requestDto.getStaff());
            variables.put("reference", requestDto.getReference());

            ProcessInstance instance = camundaService.createProcessInstance(request.getType().getStructure(), variables);

            request.setInstanceId(instance.getId());

            request = requestRepository.save(request);

            return new AppServiceResult<Request>(true, 0, "Succeed!", request );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " newRequest : Exception {}", e.getMessage());
            return new AppServiceResult<Request>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }


    @Override
    public AppServiceResult<?> validateRequest(Long id) {
        try {
            Request request = requestRepository.getOne(id);
            request.setLastModification(LocalDateTime.now());

            if(request == null){
                throw new Exception("Aucune requete retrouvée");
            }

            if(!request.getStatus().equals(RequestStatus.DRAFT)){
                throw new Exception("Cette requete à déjà été validée");
            }

            request.setStatus(RequestStatus.PENDING);
            request = requestRepository.save(request);
            RequestInfo requestDto = this.requestMapper.toDto(request);
            Task task = camundaService.getTaskByProcessInstanceIdAndTaskKey(request.getInstanceId(), "Validation");
            Map<String, Object> variables = new HashMap<>();
            task.setAssignee(requestDto.getStaff());
            camundaService.completeTask(task.getId(), variables);

            return new AppServiceResult<>(true, 0, "Succeed!", request );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " validateRequest : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }


    }

    @Override
    public AppServiceResult<RequestInfo> details(Long id) {
        try {

            Request request = requestRepository.getOne(id);
            RequestInfo dto = requestMapper.toDto(request);
            List<HistoricActivityInstance> historics = camundaService.getHistoricActivityInstances(request.getInstanceId());
            List<ApprovalDto> approvalDtos = this.mapTaskToApprovalDto(historics);
            dto.setApprovals(approvalDtos);
            return new AppServiceResult<RequestInfo>(true, 0, "Succeed!", dto );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " validateRequest : Exception {}", e.getMessage());
            return new AppServiceResult<RequestInfo>(false, AppError.Unknown.errorCode(), e.getMessage(), null);
        }
    }

    public  List<ApprovalDto> mapTaskToApprovalDto(List<HistoricActivityInstance> historics) {

        List<ApprovalDto> approvalDtos = new ArrayList<>();

        for (HistoricActivityInstance historic : historics) {

            System.out.println(historic);

            String taskId = historic.getTaskId();

            if(taskId == null){
                continue;
            }

            Task task = camundaService.getTaskDetails(taskId);

            if(task == null){
                continue;
            }

            System.out.println("Task Id : " + taskId);
            ApprovalDto approvalDto = new ApprovalDto();
            System.out.println("ActivitiName : " + historic.getActivityName());
            approvalDto.setRole(historic.getActivityName());
            System.out.println("Position : " + historics.indexOf(historic));
            approvalDto.setPosition(historics.indexOf(historic));
            if(task.getAssignee() != null){
                System.out.println("Assigne : " + task.getAssignee());
                approvalDto.setStaff(task.getAssignee());
            }
            approvalDto.setStatus(ApprovalStatus.PENDING);
            if(historic.getDurationInMillis() != null){
                System.out.println("Duration : " + historic.getDurationInMillis());
                if(historic.getDurationInMillis() > 0){
                    approvalDto.setStatus(ApprovalStatus.WAITING);
                }
            }

            if(historic.isCompleteScope()){
                System.out.println("Is Complete : ");
                approvalDto.setStatus(ApprovalStatus.ACCEPTED);
            }

            approvalDto.setFields(new ArrayList<>());
            approvalDto.setType(ApprovalType.OPEN);
            approvalDtos.add(approvalDto);
        }

        return approvalDtos;
    }

    @Override
    public AppServiceResult<RequestInfo> achivage(ArchivageDto archivageDto) {
        try {
            logger.info(MEMO_SERVICE + "achivage : methode invocation");

            Request request = requestRepository.getOne(archivageDto.getId());

            request.setArchived(archivageDto.getDecision());

            request = this.requestRepository.save(request);


            RequestInfo dto = requestMapper.toDto(request);

            //DocumentStructure stucture = FieldUtils.getStructure(type.getStructure());

            return new AppServiceResult<RequestInfo>(true, 0, "Succeed!", dto );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " validateRequest : Exception {}", e.getMessage());
            return new AppServiceResult<RequestInfo>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<RequestInfo> getRequestByReference(String reference) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");

            Request request = requestRepository.findOneByReference(reference);

            RequestInfo requestDto = requestMapper.toDto(request);

            return new AppServiceResult<RequestInfo>(true, 0, "Succeed!", requestDto );



        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<RequestInfo>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<List<RequestInfo>> getRequestByStaff(String staff, String status) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");

            List<Request> requests = requestRepository.findByStaffAndStatus(staff, RequestStatus.valueOf(status));

            return getConvertedResult(requests, "getRequestByStaff ");


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<List<RequestInfo>>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<List<RequestInfo>> getRequestAll(HttpServletRequest req) {
        try {

            EmployeeInfo employeeInfo = securityService.getCurrentUser(req);
            System.out.println("UserName Employe" + employeeInfo.getUsername());
            List<Request> requests = requestRepository.findByStaffAndArchivedOrderByLastModificationDesc(employeeInfo.getUsername(), false);

            return getConvertedResult(requests, "getRequestAll ");


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<List<RequestInfo>>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    private AppServiceResult<List<RequestInfo>> getConvertedResult(List<Request> requests, String functionName) {
        if (requests == null) {

            return new AppServiceResult<List<RequestInfo>>(false, AppError.Validattion.errorCode(),
                    "Request not exist!", null);
        }
        List<RequestInfo> result =  new ArrayList<RequestInfo>();
        if (requests.size() > 0) {
            for (Request request : requests) {
                RequestInfo dto = requestMapper.toDto(request);
                dto.setDocumentType(request.getType().getName());

                result.add(dto);
            }
        }
        return new AppServiceResult<List<RequestInfo>>(true, 0, "Succeed!", result);
    }



}
