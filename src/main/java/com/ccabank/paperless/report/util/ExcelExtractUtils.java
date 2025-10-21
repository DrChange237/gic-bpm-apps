package com.ccabank.paperless.report.util;

import com.ccabank.paperless.dto.user.EmployeeFunctionInfo;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.dto.user.FunctionInfo;
import com.ccabank.paperless.entity.Approbation;
import com.ccabank.paperless.entity.ApprovalStatus;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.repository.ApprobationRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelExtractUtils {

    private final CamundaService camundaService;
    private final UserRestClient userRestClient;
    private final ApprobationRepository approbationRepository;


    public MultipartFile convertWorkbookToMultipartFile(XSSFWorkbook workbook, String fileName) {

        return new MultipartFile() {
            private final byte[] content;
            private final String finalName = fileName.endsWith(".xlsx") ? fileName : fileName + ".xlsx";
            private final String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

            {
                try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    workbook.write(out);
                    workbook.close();
                    content = out.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException("Erreur lors de la conversion du workbook en MultipartFile", e);
                }
            }

            @Override
            public String getName() {
                return finalName;
            }

            @Override
            public String getOriginalFilename() {
                return finalName;
            }

            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public boolean isEmpty() {
                return content.length == 0;
            }

            @Override
            public long getSize() {
                return content.length;
            }

            @Override
            public byte[] getBytes() {
                return content;
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(content);
            }

            @Override
            public void transferTo(File dest) throws IOException {
                try (FileOutputStream fos = new FileOutputStream(dest)) {
                    fos.write(content);
                }
            }
        };
    }

    public Map<String, Object> getUserMap(String userName){
        EmployeeInfo employeeInfo = userRestClient.getStaffByUsername(userName);
        Map<String, Object> result = new HashMap<>();
        result.put("matricule", employeeInfo.getMatricule());
        result.put("function", Optional.ofNullable(employeeInfo.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(""));
        result.put("unity", employeeInfo.getDepartment().getName());
        result.put("fullname", employeeInfo.getFirstName() + " " + employeeInfo.getLastName());
        return result;
    }

    public Map<String, Object> getValidationMap(Request request, String id){
        HistoricTaskInstance task = camundaService.getLastHistoricTaskByDefinitionKey(request.getInstanceId(), id);
        if(task == null){
            return null;
        }
        Approbation approbation = approbationRepository.findByTaskIdAndStatus(task.getId(), ApprovalStatus.ACCEPTED);
        if(approbation == null){
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("date", approbation.getCreationDate());
        result.put("comments", approbation.getComments());
        return result;
    }

    public String getValueInProcess(Request request, String key, Map<String, Object> objectMap){

        String value = "";
        log.warn(key);

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
                    if(staff != null){
                        Map<String, Object> userMap = (Map<String, Object>) objectMap.get(staffVariable);
                        property = key.split("\\|")[2];
                        value = String.valueOf(userMap.get(property));
                    }
                    break;
                case "validation":
                    String keyValidation =  key.split("\\|")[1];
                    Map<String, Object> validationMap = getValidationMap(request, keyValidation);
                    if(validationMap != null){
                        property = key.split("\\|")[2];
                        value = String.valueOf(validationMap.get(property));
                    }
            }
        }

        return value;
    }

    public XSSFWorkbook generateExport(List<Request> requests, String sheetName, HashMap<String, String> properties, HashMap<String, String> elements) {

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

            Map<String, Object> objectMap = new HashMap<>();

            for (Map.Entry<String, String> entry : elements.entrySet()) {
                switch (entry.getValue()){
                    case "staff":
                        objectMap.put(entry.getKey(), getUserMap(request.getStaff()));
                        break;
                }
            }

            col = 0;
            headerRow = sheet.createRow(row);
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                Cell cell = headerRow.createCell(col);
                cell.setCellStyle(cellStyle2);

                String value = getValueInProcess(request, entry.getKey(), objectMap);
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
