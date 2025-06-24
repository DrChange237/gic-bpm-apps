package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.constant.AppError;
import com.ccabank.paperless.domain.AppServiceResult;
import com.ccabank.paperless.dto.memo.*;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.entity.*;
import com.ccabank.paperless.exception.BadRequestException;
import com.ccabank.paperless.mappers.RequestMapper;
import com.ccabank.paperless.repository.*;
import com.ccabank.paperless.service.faces.*;
import com.ccabank.paperless.util.camunda.Mapping;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ccabank.paperless.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private static final Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);

    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    private final DocumentTypeRepository documentTypeRepository;

    private final FileService fileService;

    private final SecurityService securityService;

    private final CamundaService camundaService;

    private final Mapping mapping;

    private final MapService mapService;

    private final EmailService emailService;


    @Override
    @Transactional
    public AppServiceResult<Request> newRequest(RequestDto requestDto, HttpServletRequest req) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");

            EmployeeInfo employeeInfo = securityService.getCurrentUser();

            if(employeeInfo == null){
                System.out.println("Erreur : employeeInfo is null");
            }

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
            //request = requestRepository.save(request);

            System.out.println("Début du mapping");
            Map<String, Object> variables = mapping.getVariablesFromField(requestDto.getFields());
            Map<String, Object> variablesApprovals = Mapping.getVariablesFromApproval(requestDto.getApprovals());
            variables.putAll(variablesApprovals);
            variables.put("owner", request.getStaff());
            variables.put("reference", request.getReference());
            List<FieldDto> otherFields = new ArrayList<>();
            variables.put("otherFields", otherFields);

            ProcessInstance instance = camundaService.createProcessInstance(request.getType().getStructure(), request.getReference(), variables);

            request.setInstanceId(instance.getId());

            request = requestRepository.save(request);

            return new AppServiceResult<>(true, 0, "Succeed!", request);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " newRequest : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    @Transactional
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

            Map<String, Object> variables = mapping.getVariablesFromField(requestDto.getFields());
            //Map<String, Object> variablesApprovals = Mapping.getVariablesFromApproval(requestDto.getApprovals());
            //variables.putAll(variablesApprovals);
            variables.put("owner", request.getStaff());
            variables.put("reference", request.getReference());

            ProcessInstance instance = camundaService.getProcessInstance(request.getInstanceId());
            camundaService.setProcessVariables(instance.getId(), variables);

            //request.setInstanceId(instance.getId());

            request = requestRepository.save(request);

            return new AppServiceResult<>(true, 0, "Succeed!", request);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " newRequest : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }


    @Override
    @Transactional
    public AppServiceResult<?> validateRequest(Long id) {


        Request request = requestRepository.getOne(id);
        request.setLastModification(LocalDateTime.now());

        boolean signature = securityService.checkUserSignature(request.getStaff());

        if(!signature){
            //throw new BadRequestException("l'utilisateur " + request.getStaff() + " n'a pas de signature");
        }

        if(request == null){
            throw new BadRequestException("Aucune requete retrouvée");
        }

        if(!request.getStatus().equals(RequestStatus.DRAFT)){
            throw new BadRequestException("Cette requete à déjà été validée");
        }

        request.setStatus(RequestStatus.PENDING);
        request = requestRepository.save(request);
        RequestInfo requestDto = this.requestMapper.toDto(request);
        Task task = camundaService.getTaskByProcessInstanceIdAndTaskKey(request.getInstanceId(), "Validation");
        Map<String, Object> variables = new HashMap<>();
        task.setAssignee(requestDto.getStaff());
        camundaService.completeTask(task.getId(), variables);

        return new AppServiceResult<>(true, 0, "Succeed!", request );

    }

    @Override
    public AppServiceResult<?> download(Long id) {
        try {
            Request request = requestRepository.getOne(id);
            request.setLastModification(LocalDateTime.now());

            if (request == null) {
                throw new Exception("Aucune requete retrouvée");
            }

            if (!request.getStatus().equals(RequestStatus.ACCEPTED)) {
                throw new Exception("Cette requete n'est pas encore acceptée");
            }

            camundaService.triggerProcessRestart("Download", request.getInstanceId());
            return new AppServiceResult<>(true, 0, "Succeed!", request);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " downloadRequest : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);
        }
    }

    @Override
    public AppServiceResult<RequestInfo> details(Long id) {
        try {

            Request request = requestRepository.getOne(id);
            RequestInfo dto = requestMapper.toDto(request);
            dto.setDocumentType(request.getType().getName());
            StartFormData formData = camundaService.getStartForm(request.getType().getStructure());
            DocumentStructure documentStructure = Mapping.getStructureFromFormData(formData);
            Map<String, Object> variables = camundaService.getProcessVariables(request.getInstanceId());

            List<FieldDto> updateFields = new ArrayList<>();

            for (FieldDto field : documentStructure.getFields()) {
                field.setValue(String.valueOf(variables.get(field.getKey())));
                updateFields.add(field);
            }

            dto.setFields(updateFields);
            dto.setFiles(fileService.getAllFiles(request.getReference(), ""));

            //List<HistoricActivityInstance> historics = camundaService.getHistoricActivityInstances(request.getInstanceId());

            List<HistoricTaskInstance> histories = camundaService.getHistoricTasksForProcessInstance(request.getInstanceId());

            List<ApprovalDto> approvalDtos = mapService.mapTaskToApprovalDto(histories);
            dto.setApprovals(approvalDtos);

            return new AppServiceResult<>(true, 0, "Succeed!", dto);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " validateRequest : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);
        }
    }

    @Override
    @Transactional
    public AppServiceResult<RequestInfo> suspend(Long id, String reason) {
        try {

            Request request = requestRepository.getOne(id);

            if(request.getStatus().equals(RequestStatus.ACCEPTED)){
                throw new BadRequestException("Cette requete a déjà été validé");
            }


            if(request.getInstanceId() != null){
                camundaService.deleteProcessInstance(request.getInstanceId());
            }

            request.setStatus(RequestStatus.SUSPENDED);
            requestRepository.save(request);
            emailService.sendSuspendRequest(request, reason);

            return new AppServiceResult<>(true, 0, "Succeed!", null);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " suspend : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);
        }
    }




    @Override
    @Transactional
    public AppServiceResult<RequestInfo> achivage(ArchivageDto archivageDto) {
        try {
            logger.info(MEMO_SERVICE + "achivage : methode invocation");

            Request request = requestRepository.getOne(archivageDto.getId());

            request.setArchived(archivageDto.getDecision());

            request = this.requestRepository.save(request);


            RequestInfo dto = requestMapper.toDto(request);

            //DocumentStructure stucture = FieldUtils.getStructure(type.getStructure());

            return new AppServiceResult<>(true, 0, "Succeed!", dto);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " validateRequest : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<RequestInfo> getRequestByReference(String reference) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");

            Request request = requestRepository.findOneByReference(reference);

            RequestInfo requestDto = requestMapper.toDto(request);

            return new AppServiceResult<>(true, 0, "Succeed!", requestDto);



        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<List<RequestInfo>> getRequestByStaff(String staff, String status) {
        try {
            logger.info(MEMO_SERVICE + "newRequest : methode invocation");
            List<Request> requests = requestRepository.findByStaffAndStatus(staff, RequestStatus.valueOf(status));
            return getConvertedResult(requests);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<List<RequestInfo>> getRequestAll(HttpServletRequest req) {
        try {

            EmployeeInfo employeeInfo = securityService.getCurrentUser();
            System.out.println("UserName Employe" + employeeInfo.getUsername());
            List<Request> requests = requestRepository.findByStaffAndArchivedOrderByLastModificationDesc(employeeInfo.getUsername(), false);

            return getConvertedResult(requests);


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " getRequestAll : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<List<RequestInfo>> getRequestHistory(HttpServletRequest req) {
        try {

            EmployeeInfo employeeInfo = securityService.getCurrentUser();
            System.out.println("UserName Employe" + employeeInfo.getUsername());
            List<Request> requests = requestRepository.findByOrderByLastModificationDesc();
            return getConvertedResult(requests);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " getRequestHistory : Exception {}", e.getMessage());
            return new AppServiceResult<>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    private AppServiceResult<List<RequestInfo>> getConvertedResult(List<Request> requests) {
        if (requests == null) {

            return new AppServiceResult<>(false, AppError.Validation.errorCode(),
                    "Request not exist!", null);
        }
        List<RequestInfo> result =  new ArrayList<RequestInfo>();
        if (!requests.isEmpty()) {
            for (Request request : requests) {
                RequestInfo dto = requestMapper.toDto(request);
                dto.setDocumentType(request.getType().getName());
                StartFormData formData = camundaService.getStartForm(request.getType().getStructure());

                Map<String, Object> variables = camundaService.getProcessVariables(request.getInstanceId());
                if(!request.getStatus().equals(RequestStatus.PENDING)){
                    variables = camundaService.retrieveCompletedProcessVariables(request.getInstanceId());
                }
                System.out.println("Debut du mapping");
                DocumentStructure documentStructure = Mapping.getStructureFromFormData(formData);
                dto.setFields(Mapping.getFieldFromFormField(formData, variables));

                List<HistoricTaskInstance> historics = camundaService.getHistoricTasksForProcessInstance(request.getInstanceId());
                List<ApprovalDto> approvalDtos = this.mapService.mapTaskToApprovalDto(historics);
                dto.setApprovals(approvalDtos);

                dto.setFiles(fileService.getAllFiles(request.getReference(), request.getStaff()));
                result.add(dto);
            }
        }
        return new AppServiceResult<>(true, 0, "Succeed!", result);
    }

}
