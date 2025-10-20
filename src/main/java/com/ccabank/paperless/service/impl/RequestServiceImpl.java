package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.constant.AppError;
import com.ccabank.paperless.domain.AppServiceResult;
import com.ccabank.paperless.dto.memo.*;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.entity.*;
import com.ccabank.paperless.exception.BadRequestException;
import com.ccabank.paperless.exception.NotFoundException;
import com.ccabank.paperless.mappers.RequestMapper;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.repository.*;
import com.ccabank.paperless.service.faces.*;
import com.ccabank.paperless.specification.FileSpecifications;
import com.ccabank.paperless.specification.RequestSpecifications;
import com.ccabank.paperless.util.camunda.Mapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ccabank.paperless.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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

    private final UserRestClient userRestClient;

    private final ApprobationRepository approbationRepository;


    @Override
    @Transactional
    public AppServiceResult<Request> newRequest(RequestDto requestDto, HttpServletRequest req) {

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
    public byte[] export(String reference, String type, String staff, String startDate, String endDate) {


        DocumentType documentType = null;

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

        Specification<Request> spec = Specification.where(null);

        if(endDate != null && startDate != null){
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            spec = Specification.where(RequestSpecifications.dateBetween(start, end));
        }

        spec = RequestSpecifications.withDynamicQuery(reference, documentType, staff, RequestStatus.ACCEPTED);
        List<Request> requests = requestRepository.findAll(spec);

        if(requests.isEmpty()){
            throw new NotFoundException("Aucun enregistrement trouvé");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();

        LinkedHashMap<String, String> properties = new LinkedHashMap<>();

        switch (documentType.getStructure()){
            case "mission":
                properties.put("reference", "Reference");
                properties.put("staff|owner|matricule", "Matricule");
                properties.put("staff|owner|fullname", "Staff Ayant Initié");
                properties.put("staff|owner|function", "Fonction");
                properties.put("validation|Validation|date", "Date de Création");
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
                workbook = this.generateExport(requests, "Export_Mission_Paperless", properties);
                break;
            case "vacation":
                properties.put("reference", "Reference");
                properties.put("owner", "Staff");
                properties.put("realStartDate", "Date de Début");
                properties.put("reprise_date", "Date de Fin");
                properties.put("days", "Nombre de Jours");
                properties.put("typeInterim", "Type d'Interim");
                properties.put("lastVacationDate ", "Date de dernier congés");
                workbook = this.generateExport(requests, "Export_Vacation_Paperless", properties);
                break;
            case "absence":
                properties.put("reference", "Reference");
                properties.put("owner", "Staff");
                properties.put("startDate", "Date de Début");
                properties.put("endDate", "Date de Fin");
                properties.put("reason", "Motif");
                properties.put("otherReason", "Autre Motif");
                properties.put("deduction", "Base de Déduction");
                properties.put("absence", "Cumul annuel des absences");
                properties.put("stock ", "Stock des congés année N");
                properties.put("advice", "Avis d'octroi");
                properties.put("rights ", "Droit restant dû");
                workbook = this.generateExport(requests, "Export_Vacation_Paperless", properties);
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

    public Map<String, Object> getUserMap(String username){
        EmployeeInfo employeeInfo =  userRestClient.getStaffByUsername(username);
        Map<String, Object> result = new HashMap<>();
        result.put("matricule", employeeInfo.getMatricule());
        result.put("function", employeeInfo.getFunction().getFunction().getName());
        result.put("unity", employeeInfo.getDepartment().getName());
        result.put("fullname", employeeInfo.getFirstName() + " " + employeeInfo.getLastName());
        return result;
    }

    public Map<String, Object> getValidationMap(Request request, String id){
        HistoricTaskInstance task = camundaService.getLastHistoricTaskByDefinitionKey(request.getInstanceId(), id);
        Approbation approbation = approbationRepository.findByTaskIdAndStatus(task.getId(),ApprovalStatus.ACCEPTED);
        if(approbation == null){
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("date", approbation.getCreationDate());
        result.put("comments", approbation.getComments());
        return result;
    }

    public String getValueInProcess(Request request, String key){

        String value = "";

        if(!key.contains("|")){
            if(camundaService.isProcessInstanceActive(request.getInstanceId())){
                value  = String.valueOf(camundaService.getProcessVariable(request.getInstanceId(), key));
            }else {
                value = String.valueOf(camundaService.getHistoricProcessVariable(request.getInstanceId(), key));
            }
        }else{
            String type = key.split("\\|")[0];
            String property = null;
            switch (type){
                case "staff":
                    String staffVariable =  key.split("\\|")[1];
                    String staff = (String) camundaService.getHistoricProcessVariable(request.getInstanceId(), staffVariable);
                    EmployeeInfo employeeInfo = userRestClient.getStaffByUsername(staff);
                    Map<String, Object> userMap = getUserMap(employeeInfo.getUsername());
                    property = key.split("\\|")[2];
                    value = String.valueOf(userMap.get(property));
                    break;
                case "validation":
                    String keyValidation =  key.split("\\|")[1];
                    Map<String, Object> validationMap = getValidationMap(request, keyValidation);
                    property = key.split("\\|")[2];
                    value = String.valueOf(validationMap.get(property));
            }
        }

        return value;
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
        font.setColor(IndexedColors.BLACK.getIndex());
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
            sheet.setColumnWidth(col, 75 * 100);
            col++;
        }

        row = 1;
        for (Request request : requests) {
            col = 0;
            headerRow = sheet.createRow(row);
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                Cell cell = headerRow.createCell(col);
                cell.setCellStyle(cellStyle2);
                String value = getValueInProcess(request, entry.getKey());
                log.warn("Value "+ value);
                cell.setCellValue(value);
                if(entry.getValue().contains("Date")){
                    if(value != null){
                        if(!value.equals("null")){
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                            try{
                                ZonedDateTime zonedDateTime = ZonedDateTime.parse(value, formatter);
                                LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
                                CellStyle dateCellStyle = cellStyle2;
                                CreationHelper createHelper = workbook.getCreationHelper();
                                short dateFormat = createHelper.createDataFormat().getFormat("dd/MM/yyyy");
                                dateCellStyle.setDataFormat(dateFormat);
                                cell.setCellStyle(dateCellStyle);
                                cell.setCellValue(localDateTime);
                            }catch (Exception e){
                                log.error("Erreur : "+e.getMessage());
                            }
                        }
                    }
                }
                col++;
            }
            row++;
        }
        return  workbook;
    }

}
