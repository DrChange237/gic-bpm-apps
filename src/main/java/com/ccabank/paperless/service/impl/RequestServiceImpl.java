package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.dto.memo.*;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.entity.*;
import com.ccabank.paperless.exception.BadRequestException;
import com.ccabank.paperless.exception.NotFoundException;
import com.ccabank.paperless.mappers.RequestMapper;
import com.ccabank.paperless.report.util.ExcelExtractUtils;
import com.ccabank.paperless.repository.*;
import com.ccabank.paperless.service.faces.*;
import com.ccabank.paperless.util.DateUtil;
import com.ccabank.paperless.util.camunda.Mapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    private final DocumentTypeRepository documentTypeRepository;

    private final FileService fileService;

    private final SecurityService securityService;

    private final CamundaService camundaService;

    private final Mapping mapping;

    private final MapService mapService;

    private final EmailService emailService;

    private final ExcelExtractUtils utils;


    @Override
    @Transactional
    public Request newRequest(RequestDto requestDto, HttpServletRequest req) {

            log.info("newRequest : methode invocation");

            EmployeeInfo employeeInfo = securityService.getCurrentUser();

            if(employeeInfo == null){
                log.info("Erreur : employeeInfo is null");
            }

            log.info("UserName Employe" + employeeInfo.getUsername());

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

            Map<String, Object> variables = mapping.getVariablesFromField(requestDto.getFields());
            Map<String, Object> variablesApprovals = Mapping.getVariablesFromApproval(requestDto.getApprovals());
            variables.putAll(variablesApprovals);
            variables.put("owner", request.getStaff());
            variables.put("reference", request.getReference());
            List<FieldDto> otherFields = new ArrayList<>();
            variables.put("otherFields", otherFields);

            ProcessInstance instance = camundaService.createProcessInstance(request.getType().getStructure(), request.getReference(), variables);

            request.setInstanceId(instance.getId());

            return requestRepository.save(request);
    }

    @Override
    @Transactional
    public Request update(RequestDto requestDto) {
        Request request = this.requestRepository.getOne(requestDto.getId());

        if(request.getStatus().equals(RequestStatus.ACCEPTED) || request.getStatus().equals(RequestStatus.PENDING) ){
            throw new ValidationException("Cette requête est déjà acceptée ou encore en cours");
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

        return requestRepository.save(request);
    }


    @Override
    @Transactional
    public void validateRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Aucune requête retrouvée"));
        request.setLastModification(LocalDateTime.now());
        request.setValidationDate(LocalDateTime.now());

        boolean signature = securityService.checkUserSignature(request.getStaff());

        if(!signature){
            throw new BadRequestException("l'utilisateur " + request.getStaff() + " n'a pas de signature, bien vouloir charger votre signature");
        }

        if(!request.getStatus().equals(RequestStatus.DRAFT)){
            throw new BadRequestException("Cette requête à déjà été validée");
        }

        request.setStatus(RequestStatus.PENDING);
        request = requestRepository.save(request);
        RequestInfo requestDto = this.requestMapper.toDto(request);
        Task task = camundaService.getTaskByProcessInstanceIdAndTaskKey(request.getInstanceId(), "Validation");
        Map<String, Object> variables = new HashMap<>();
        task.setAssignee(requestDto.getStaff());
        camundaService.completeTask(task.getId(), variables);
    }

    @Override
    public Request download(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Aucune requête retrouvée"));
        request.setLastModification(LocalDateTime.now());

        if (!request.getStatus().equals(RequestStatus.ACCEPTED)) {
            throw new ValidationException("Cette requête n'est pas encore acceptée");
        }

        camundaService.triggerProcessRestart("Download", request.getInstanceId());
        return request;
    }

    @Override
    public RequestInfo details(Long id) {
        Request request = requestRepository.getOne(id);
        RequestInfo dto = requestMapper.toDto(request);
        dto.setDocumentType(request.getType().getName());
        StartFormData formData = camundaService.getStartForm(request.getType().getStructure());
        DocumentStructure documentStructure = Mapping.getStructureFromFormData(formData);
        Map<String, Object> variables;

        if(camundaService.isProcessInstanceActive(request.getInstanceId())){
            variables = camundaService.getProcessVariables(request.getInstanceId());
        }else {
            variables = camundaService.getHistoricProcessVariables(request.getInstanceId());
        }

        List<FieldDto> updateFields = new ArrayList<>();

        for (FieldDto field : documentStructure.getFields()) {
            field.setValue(String.valueOf(variables.get(field.getKey())));
            updateFields.add(field);
        }

        dto.setFields(updateFields);
        dto.setFiles(fileService.getAllFiles(request.getReference(), ""));

        List<HistoricTaskInstance> histories = camundaService.getHistoricTasksForProcessInstance(request.getInstanceId());

        List<ApprovalDto> approvalDtos = mapService.mapTaskToApprovalDto(histories);
        dto.setApprovals(approvalDtos);

        return dto;
    }

    @Override
    public RequestInfo detailForUpdate(Long id) {
        Request request = requestRepository.getOne(id);
        RequestInfo dto = requestMapper.toDto(request);
        dto.setDocumentType(request.getType().getName());
        StartFormData formData = camundaService.getStartForm(request.getType().getStructure());
        DocumentStructure documentStructure = Mapping.getStructureFromFormData(formData);

        List<ApprovalDto> approvalsStructures = documentStructure.getApprovals();

        Map<String, Object> variables = camundaService.getProcessVariables(request.getInstanceId());

        List<FieldDto> updateFields = new ArrayList<>();

        for (FieldDto field : documentStructure.getFields()) {
            field.setValue(String.valueOf(variables.get(field.getKey())));
            updateFields.add(field);
        }

        dto.setFields(updateFields);
        dto.setFiles(fileService.getAllFiles(request.getReference(), ""));
        dto.setApprovals(approvalsStructures);

        return dto;
    }

    @Override
    public RequestInfo suspend(Long id, String reason) {
        Request request = requestRepository.getOne(id);

        if(request.getStatus().equals(RequestStatus.ACCEPTED)){
            throw new BadRequestException("Cette requête a déjà été validé");
        }


        if(request.getInstanceId() != null){
            camundaService.deleteProcessInstance(request.getInstanceId());
        }

        request.setStatus(RequestStatus.SUSPENDED);
        request = requestRepository.save(request);
        emailService.sendSuspendRequest(request, reason);
        return requestMapper.toDto(request);
    }


    @Override
    @Transactional
    public RequestInfo achivage(ArchivageDto archivageDto) {
        Request request = requestRepository.getOne(archivageDto.getId());

        request.setArchived(archivageDto.getDecision());

        request = this.requestRepository.save(request);

        return requestMapper.toDto(request);
    }

    @Override
    public RequestInfo getRequestByReference(String reference) {
        Request request = requestRepository.findOneByReference(reference);

        return requestMapper.toDto(request);
    }

    @Override
    public List<RequestInfo> getRequestByStaff(String staff, String status) {
        List<Request> requests = requestRepository.findByStaffAndStatus(staff, RequestStatus.valueOf(status));
        return getConvertedResult(requests);
    }

    @Override
    public List<RequestInfo> getRequestAll(HttpServletRequest req) {
        EmployeeInfo employeeInfo = securityService.getCurrentUser();
        log.info("UserName Employe" + employeeInfo.getUsername());
        List<Request> requests = requestRepository.findByStaffAndArchivedOrderByLastModificationDesc(employeeInfo.getUsername(), false);
        return getConvertedResult(requests);
    }

    @Override
    public List<RequestInfo> getRequestHistory(HttpServletRequest req) {
        EmployeeInfo employeeInfo = securityService.getCurrentUser();
        log.info("UserName Employe" + employeeInfo.getUsername());
        List<Request> requests = requestRepository.findByOrderByLastModificationDesc();
        return getConvertedResult(requests);
    }

    private List<RequestInfo> getConvertedResult(List<Request> requests) {
        if (requests == null || requests.isEmpty()) {
            return new ArrayList<>();
        }
        List<RequestInfo> result = new ArrayList<>();
        for (Request request : requests) {
            RequestInfo dto = requestMapper.toDto(request);
            dto.setDocumentType(request.getType().getName());
            StartFormData formData = camundaService.getStartForm(request.getType().getStructure());

            Map<String, Object> variables = camundaService.getProcessVariables(request.getInstanceId());
            if(!request.getStatus().equals(RequestStatus.PENDING)){
                variables = camundaService.retrieveCompletedProcessVariables(request.getInstanceId());
            }
            DocumentStructure documentStructure = Mapping.getStructureFromFormData(formData);
            dto.setFields(Mapping.getFieldFromFormField(formData, variables));

            List<HistoricTaskInstance> historics = camundaService.getHistoricTasksForProcessInstance(request.getInstanceId());
            List<ApprovalDto> approvalDtos = this.mapService.mapTaskToApprovalDto(historics);
            dto.setApprovals(approvalDtos);
            dto.setFiles(fileService.getAllFiles(request.getReference(), request.getStaff()));
            result.add(dto);
        }
        return result;
    }

    @Override
    public byte[] export(String reference, String type, String staff, String startDate, String endDate)  {


        DocumentType documentType;

        if(reference == null){
            if(type == null){
                throw new NotFoundException("Pour l'export le type de document est obligatoire");
            }
            documentType = documentTypeRepository.findOneByStructure(type);
        }else{
            Request request = requestRepository.findOneByReference(reference);
            if(request == null){
                throw new NotFoundException("Reference incorrecte");
            }
            documentType = request.getType();

        }

        /*Specification<Request> spec = Specification.where(null);

        if(endDate != null && startDate != null){
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            spec = Specification.where(RequestSpecifications.dateBetween(start.atStartOfDay(), end.atStartOfDay()));
        }

        spec = spec.and(RequestSpecifications.withDynamicQuery(reference, documentType, staff, RequestStatus.ACCEPTED));
        List<Request> requests = requestRepository.findAll(spec);

        if(requests.isEmpty()){
            throw new NotFoundException("Aucun enregistrement trouvé");
        }*/


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        LinkedHashMap<String, String> properties = new LinkedHashMap<>();
        HashMap<String, String> elements = new LinkedHashMap<>();
        String dateProperty = "";
        List<Request> requests = new ArrayList<>();

        Date startDateD = null;
        Date endDateD = null;
        List<HistoricProcessInstance> instances = new ArrayList<>();

        switch (documentType.getStructure()){
            case "mission":

                dateProperty = "startDate";

                elements.put("owner", "staff");
                elements.put("validation", "validation");

                properties.put("reference", "Reference");
                properties.put("staff|owner|matricule", "Matricule");
                properties.put("staff|owner|fullname", "Staff Ayant Initié");
                properties.put("staff|owner|function", "Fonction");
                properties.put("request|createdAt", "Date de Création");
                properties.put("validation|Apbt_n2|date", "Date de Validation N + 2");
                properties.put("validation|Apbt_ca_supervision|date", "Date de Supervision DCH");
                properties.put("validation|Apbt_ca_validation|date", "Date de Validation DCH");
                properties.put("supportCharge", "Agence Support");
                properties.put("location", "Lieu de la Mission");
                properties.put("object", "Objet de la Mission");
                properties.put("transport", "Moyen de Transport");
                properties.put("immatriculation", "Immatriculation");
                properties.put("startDate", "Date de Départ");
                properties.put("endDate", "Date de Retour");
                properties.put("nbDays", "Nombre de Nuitées Accordées");
                properties.put("missionFees", "Montant Total des Frais de Mission");
                properties.put("transportFees", "Montant des Frais de Transport");


                try {
                    startDateD = DateUtil.convertToDate(startDate);
                    endDateD = DateUtil.convertToDate(endDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                instances = camundaService.findCompletedInstancesByDateRange(startDateD, endDateD, documentType.getStructure(), dateProperty);

                for (HistoricProcessInstance instance : instances) {
                    Request request = requestRepository.findByInstanceIdAndStatus(instance.getId(), RequestStatus.ACCEPTED);
                    if (request != null) {
                        requests.add(request);
                    }
                }

                if(requests.isEmpty()){
                    log.warn("No requests found");
                    break;
                }

                workbook = utils.generateExport(requests, "Export_Mission_Paperless", properties, elements);
                break;
            case "vacation":

                dateProperty = "realStartDate";

                elements.put("owner", "staff");
                elements.put("validation", "validation");
                elements.put("Apbt_interimaire", "staff");
                properties.put("reference", "Reference");
                properties.put("staff|owner|matricule", "Matricule");
                properties.put("staff|owner|fullname", "Staff Ayant Initié");
                properties.put("staff|owner|function", "Fonction");
                properties.put("request|createdAt", "Date de Création");
                properties.put("validation|Apbt_n2|date", "Date de Validation N + 2");
                properties.put("validation|Apbt_ca_supervision|date", "Date de Supervision DCH");
                properties.put("validation|Apbt_ca_validation|date", "Date de Validation DCH");
                properties.put("allocationDue", "Congé Principal");
                properties.put("allocationDue", "Allocation de congé due");
                properties.put("majAncienete", "Majoration pour ancienneté");
                properties.put("majFamille", "Majoration pour charge familiale");
                properties.put("congeAnterieur", "Congés Antérieur");
                properties.put("permDeduction", "Permission à déduire du congés");
                //properties.put("permDeduction", "Nombre de jours total de congés dus");
                properties.put("days", "Nombre de Jours total accordées");
                properties.put("realStartDate", "Date de Départ");
                properties.put("reprise_date", "Date de Reprise");
                properties.put("typeInterim", "Type d'Interim");
                properties.put("staff|Apbt_interimaire|fullname", "Intérimaire");
                properties.put("staff|Apbt_interimaire|function", "Fonction Intérimaire");
                properties.put("staff|Apbt_interimaire|unity", "Unité Intérimaire");

                try {
                    startDateD = DateUtil.convertToDate(startDate);
                    endDateD = DateUtil.convertToDate(endDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                instances = camundaService.findCompletedInstancesByDateRange(startDateD, endDateD, documentType.getStructure(), dateProperty);

                for (HistoricProcessInstance instance : instances) {
                    Request request = requestRepository.findByInstanceIdAndStatus(instance.getId(), RequestStatus.ACCEPTED);
                    if (request != null) {
                        requests.add(request);
                    }
                }

                if(requests.isEmpty()){
                    log.warn("No requests found");
                    break;
                }

                workbook = utils.generateExport(requests, "Export_Vacation_Paperless", properties, elements);
                break;

            case "absence":

                dateProperty = "startDate";


                elements.put("owner", "staff");
                elements.put("validation", "validation");
                elements.put("interim", "staff");

                properties.put("reference", "Reference");
                properties.put("staff|owner|matricule", "Matricule");
                properties.put("staff|owner|fullname", "Staff Ayant Initié");
                properties.put("staff|owner|function", "Fonction");
                properties.put("request|createdAt", "Date de Création");
                properties.put("validation|Apbt_n2|date", "Date de Validation N + 2");
                properties.put("validation|Apbt_ca_supervision|date", "Date de Supervision DCH");
                properties.put("validation|Apbt_ca_validation|date", "Date de Validation DCH");
                properties.put("reason", "Motif de l'absence");
                properties.put("otherReason", "Autre Motif");
                properties.put("startDate", "Date de Départ");
                properties.put("endDate", "Date de reprise de service");
                properties.put("days", "Nombre de Jours Souhaitée");
                properties.put("deduction", "Base de Déduction");
                properties.put("staff|interim|fullname", "Proposition d'interim");
                properties.put("staff|owner|unity", "Unité");

                try {
                    startDateD = DateUtil.convertToDate(startDate);
                    endDateD = DateUtil.convertToDate(endDate);

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                instances = camundaService.findCompletedInstancesByDateRange(startDateD, endDateD, documentType.getStructure(), dateProperty);

                for (HistoricProcessInstance instance : instances) {
                    Request request = requestRepository.findByInstanceIdAndStatus(instance.getId(), RequestStatus.ACCEPTED);
                    if (request != null) {
                        requests.add(request);
                    }
                }

                if(requests.isEmpty()){
                    log.warn("No requests found");
                    break;
                }

                workbook = utils.generateExport(requests, "Export_Vacation_Paperless", properties, elements);
                break;
        }

        try {
            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();
            return excelBytes;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
