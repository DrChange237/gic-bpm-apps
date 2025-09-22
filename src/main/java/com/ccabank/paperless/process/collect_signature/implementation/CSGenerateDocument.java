package com.ccabank.paperless.process.collect_signature.implementation;

import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.dto.reporting.CollectSignatureForm;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.dto.user.UserRestDto;
import com.ccabank.paperless.entity.Approbation;
import com.ccabank.paperless.entity.ApprovalStatus;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.openfeign.FileRestClient;
import com.ccabank.paperless.openfeign.ReportingRestClient;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.process.general.service.RequestService;
import com.ccabank.paperless.repository.ApprobationRepository;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.util.CustomMultipartFile;
import com.ccabank.paperless.util.file.Base64Utils;
import com.ccabank.paperless.util.file.PdfUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CSGenerateDocument implements JavaDelegate {


    private final ApprobationRepository approbationRepository;
    private final RequestRepository requestRepository;
    private final UserRestClient userRestClient;
    private final ReportingRestClient reportingRestClient;
    private final RequestService requestService;
    private final FileService fileService;
    private final CamundaService camundaService;
    private final FileRestClient fileRestClient;
    private String IF_SIGNED_WITH_PAPERLESS = "IfSignedWithPaperless";
    private String SIGNED_WITH_PAPERLESS = "SignedWithPaperless";

    public String extractFileId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        // Récupère la partie après le dernier "/"
        return url.substring(url.lastIndexOf("/") + 1);
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Inside CSGenerateDocument");

        Request request = requestRepository.findByInstanceId(delegateExecution.getProcessInstanceId());

        List<Approbation> approbations = approbationRepository.findByReference(request.getReference());

        log.info("approbations size: " + approbations.size());
        log.info("approbations: " + approbations);
        CollectSignatureForm form = new CollectSignatureForm();
        form.setReference(request.getReference());

        List<CollectSignatureForm.Signatory> signatories = new ArrayList<>();

        for (Approbation approbation : approbations) {
            CollectSignatureForm.Signatory formSignatory = new CollectSignatureForm.Signatory();
            EmployeeInfo employee = userRestClient.getStaffByUsername(approbation.getStaff());
            String signature = userRestClient.getEmployeeSignature(approbation.getStaff());
            formSignatory.setName(employee.getFirstName() + " " + employee.getLastName());
            formSignatory.setFunction(employee.getFunction().getFunction().getName());
            formSignatory.setComments(approbation.getComments());
            formSignatory.setSignature(signature);
            formSignatory.setDate(approbation.getCreationDate().toLocalDate());
            signatories.add(formSignatory);
        }

        form.setSignatories(signatories);
        String fileId = (String) camundaService.getProcessVariable(delegateExecution.getProcessInstanceId(), "file");
        log.info("File ID: " + fileId);
        String base64Page = fileRestClient.getB64FileById(this.extractFileId(fileId));
        byte[] documentPage = Base64Utils.decodeBase64ToBytes(base64Page);

        Boolean ifSigned = Boolean.valueOf(PdfUtils.getParameter(documentPage, IF_SIGNED_WITH_PAPERLESS));

        if (ifSigned) {
            log.info(IF_SIGNED_WITH_PAPERLESS);
            String encoded = PdfUtils.getParameter(documentPage, SIGNED_WITH_PAPERLESS);
            List<CollectSignatureForm.Signatory> signatoriesToSign =
                    new ObjectMapper().readValue(encoded, new TypeReference<List<CollectSignatureForm.Signatory>>() {});

            signatoriesToSign.addAll(signatories);
            form.setSignatories(signatoriesToSign);
        }

        ByteArrayResource resource = reportingRestClient.signature(form);
        log.info("Signature generated");


        byte[] signaturePage = resource.getByteArray();


        List<byte[]> documentPages = new ArrayList<>();
        documentPages.add(documentPage);
        documentPages.add(signaturePage);

        byte[] destination = PdfUtils.mergePdfs(documentPages);
        destination = PdfUtils.addWatermark(destination, "SIGNED WITH PAPERLESS");
        destination = PdfUtils.addFooterToPdf(destination, "SIGNED WITH PAPERLESS");
        String encoded = new ObjectMapper().writeValueAsString(form.getSignatories());
        destination = PdfUtils.addParameter(destination, IF_SIGNED_WITH_PAPERLESS, "true");
        destination = PdfUtils.addParameter(destination, SIGNED_WITH_PAPERLESS, encoded);

        CustomMultipartFile multipartFile = new CustomMultipartFile(destination, "collecte_signature_" + delegateExecution.getBusinessKey() + ".pdf", "application/pdf");
        FileDto fileDto = new FileDto();
        fileDto.setAddDate(LocalDateTime.now());
        String type = (String) camundaService.getProcessVariable(delegateExecution.getProcessInstanceId(), "type");
        log.info("Type : " + type);
        String object = (String) camundaService.getProcessVariable(delegateExecution.getProcessInstanceId(), "object");
        fileDto.setName("[SIGNE] " + type.toUpperCase() + " - " + object.toUpperCase());
        fileDto.setFile(Base64.getEncoder().encodeToString(resource.getByteArray()));
        fileDto.setMultipartFile(multipartFile);
        fileDto.setType("application/pdf");
        request = requestService.confirmRequest(delegateExecution.getProcessInstanceId());
        fileService.saveFile(request, fileDto);
        log.info("File generated");


    }
}
