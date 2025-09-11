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
import com.ccabank.paperless.specification.FileSpecifications;
import com.ccabank.paperless.specification.RequestSpecifications;
import com.ccabank.paperless.util.camunda.Mapping;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ccabank.paperless.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
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
        request.setValidationDate(LocalDateTime.now());

        boolean signature = securityService.checkUserSignature(request.getStaff());

        if(!signature){
            throw new BadRequestException("l'utilisateur " + request.getStaff() + " n'a pas de signature, bien vouloir charger votre signature");
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
            Map<String, Object> variables = new HashMap<>();

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
    public AppServiceResult<RequestInfo> detailForUpdate(Long id) {
        try {

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

            return new AppServiceResult<>(true, 0, "Succeed!", dto);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " detailForUpdate : Exception {}", e.getMessage());
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

    @Override
    public byte[] export(String reference, String type, String staff, LocalDate startDate, LocalDate endDate) {

        Specification<Request> spec = Specification.where(null);
        if(endDate != null && startDate != null){
            spec = Specification.where(RequestSpecifications.dateBetween(startDate, endDate));
        }
        DocumentType documentType = documentTypeRepository.findOneByStructure(type);
        spec = RequestSpecifications.withDynamicQuery(reference, documentType, staff, RequestStatus.ACCEPTED);
        List<Request> requests = requestRepository.findAll(spec);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();

        HashMap<String, String> propertiesMission = new HashMap<>();
        propertiesMission.put("reference", "Reference");
        propertiesMission.put("owner", "Staff");
        propertiesMission.put("object", "Objet");
        propertiesMission.put("location", "Lieu");
        propertiesMission.put("startDate", "Date de Début");
        propertiesMission.put("endDate", "Date de Fin");
        propertiesMission.put("nbDays", "Nombre de Nuitées");
        propertiesMission.put("transport", "Moyen de Transport");
        propertiesMission.put("immatriculation", "Immatriculation");

        switch (documentType.getName()){
            case "mission":
                workbook = this.generateExport(requests, "Export_Mission_Paperless", propertiesMission);
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

    public XSSFWorkbook generateExport(List<Request> requests, String sheetName, HashMap<String, String> properties) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font =  workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        font.setBold(false);
        font.setColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFont(font);

        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        font =  workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 9);
        font.setItalic(true);
        font.setColor(IndexedColors.VIOLET.getIndex());
        cellStyle2.setFont(font);

        cellStyle2.setBorderTop(BorderStyle.MEDIUM);
        cellStyle2.setBorderRight(BorderStyle.MEDIUM);
        cellStyle2.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle2.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle2.setAlignment(HorizontalAlignment.LEFT);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle cellStyle3 = workbook.createCellStyle();
        cellStyle3.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        font =  workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 9);
        cellStyle3.setFont(font);

        cellStyle3.setBorderTop(BorderStyle.MEDIUM);
        cellStyle3.setBorderRight(BorderStyle.MEDIUM);
        cellStyle3.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle3.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle3.setAlignment(HorizontalAlignment.LEFT);
        cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);

        Row headerRow = null;

        int row = 0;
        int col = 0;
        headerRow = sheet.createRow(row);

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            System.out.println("Clé : " + entry.getKey() + ", Valeur : " + entry.getValue());
            // Écrire l'en-tête
            Cell cell = headerRow.createCell(col);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(entry.getValue());
            col++;
        }


        row = 1;

        for (Request request : requests) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                headerRow = sheet.createRow(row);
                Cell cell = headerRow.createCell(row);
                cell.setCellStyle(cellStyle2);
                String value = String.valueOf(camundaService.getProcessVariable(request.getInstanceId(), entry.getKey()));
                cell.setCellValue(value);
                row++;
            }
        }

        sheet.setColumnWidth(0, 100 * 256);
        return  workbook;
    }

}
