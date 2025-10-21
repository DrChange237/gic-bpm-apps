package com.ccabank.paperless.report.implementation;

import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.entity.DocumentType;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.entity.RequestStatus;
import com.ccabank.paperless.report.faces.ExportResumptionService;
import com.ccabank.paperless.report.util.ExcelExtractUtils;
import com.ccabank.paperless.repository.DocumentTypeRepository;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExportResumptionServiceImpl implements ExportResumptionService {

    private final FileService fileService;
    private final DocumentTypeRepository documentTypeRepository;
    private final RequestRepository requestRepository;
    private final ExcelExtractUtils utils;
    private final String DOCUMENT_TYPE = "resumption";
    private final CamundaService camundaService;

    @Override
    public void exportResumption() throws IOException {

        DocumentType type = documentTypeRepository.findOneByStructure(DOCUMENT_TYPE);

        Date endDate = DateUtil.getLastDayOfCurrentMonth();
        Date startDate = DateUtil.getFirstDayOfCurrentMonth();
        String dateProperty = "startDate";

        List<HistoricProcessInstance> instances = camundaService.findCompletedInstancesByDateRange(startDate, endDate, DOCUMENT_TYPE, dateProperty);
        List<Request> requests = new ArrayList<>();

        for (HistoricProcessInstance instance : instances) {
            Request request = requestRepository.findByInstanceIdAndStatus(instance.getId(), RequestStatus.ACCEPTED);
            if (request != null) {
                requests.add(request);
            }
        }

        //List<Request> requests = requestRepository.findAll(spec);

        if(requests.isEmpty()){
            log.warn("No requests found");
            return;
        }

        XSSFWorkbook workbook = new XSSFWorkbook();

        LinkedHashMap<String, String> properties = new LinkedHashMap<>();
        HashMap<String, String> elements = new LinkedHashMap<>();

        elements.put("owner", "staff");
        elements.put("validation", "validation");

        properties.put("reference", "Reference");
        properties.put("staff|owner|matricule", "Matricule");
        properties.put("staff|owner|fullname", "Staff Ayant Initié");
        properties.put("staff|owner|function", "Fonction");
        properties.put("staff|owner|unity", "Unité de Rattachement");
        properties.put("request|createdAt", "Date de Création");
        properties.put("validation|Apbt_n1|date", "Date de Validation N + 1");
        properties.put("reason", "Motif de l'absence");
        properties.put("explication", "Détail du motif");
        properties.put("startDate", "Date de Départ");
        properties.put("endDate", "Date de Reprise prévue");
        properties.put("realEndDate", "Date de Reprise Effective");

        workbook = utils.generateExport(requests, "Export_Reprise_Paperless", properties, elements);

        FileDto dto = new FileDto();
        Locale localeFr = Locale.FRENCH; // ou new Locale("fr", "FR")
        LocalDate now = LocalDate.now();
        String month =  now.getMonth().getDisplayName(TextStyle.FULL, localeFr);
        dto.setName("Rapport Reprise de Service "+ month + " " + now.getYear() + "");
        MultipartFile file = utils.convertWorkbookToMultipartFile(workbook, "report");
        dto.setMultipartFile(file);
        Request request = new Request();
        request.setType(type);
        request.setStaff("thierry.makondi");
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String date = currentDate.format(formatter);
        Long count = requestRepository.countRequestsCreatedToday() + 1;
        date = date + "-" + count;
        request.setReference("DCH/REPRISE" + "/" + date);
        request.setStatus(RequestStatus.ACCEPTED);
        request.setCreatedAt(LocalDateTime.now());
        request.setInstanceId(UUID.randomUUID().toString());
        request.setLastModification(LocalDateTime.now());
        request = requestRepository.save(request);
        fileService.saveFile(request, dto);
    }
}
