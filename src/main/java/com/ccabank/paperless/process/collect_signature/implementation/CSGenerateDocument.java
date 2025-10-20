package com.ccabank.paperless.process.collect_signature.implementation;

import com.ccabank.paperless.dto.email.AttachmentDto;
import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
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
import com.ccabank.paperless.service.faces.EmailService;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.util.CustomMultipartFile;
import com.ccabank.paperless.util.file.Base64Utils;
import com.ccabank.paperless.util.file.PdfUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itextpdf.text.Rectangle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.css.Rect;

import javax.swing.text.DateFormatter;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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
    private final EmailService emailService;

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
        List<Approbation> filtered = approbations.stream()
                .collect(Collectors.toMap(
                        Approbation::getStaff, // clé : le staff
                        a -> a,                // valeur : l'approbation
                        (a1, a2) -> a1.getCreationDate().isAfter(a2.getCreationDate()) ? a1 : a2 // si doublon, garder le plus récent
                ))
                .values()               // récupérer juste les Approbations
                .stream()
                .collect(Collectors.toList());



        log.info("approbations size: " + filtered.size());
        log.info("approbations: " + filtered);
        CollectSignatureForm form = new CollectSignatureForm();
        form.setReference(request.getReference());

        List<CollectSignatureForm.Signatory> signatories = new ArrayList<>();

        for (Approbation approbation : filtered) {
            CollectSignatureForm.Signatory formSignatory = new CollectSignatureForm.Signatory();
            EmployeeInfo employee = userRestClient.getStaffByUsername(approbation.getStaff());
            String signature = userRestClient.getEmployeeSignature(approbation.getStaff());
            formSignatory.setIdentifier(employee.getUsername());
            formSignatory.setName(employee.getFirstName() + " " + employee.getLastName());
            formSignatory.setFunction(employee.getFunction().getFunction().getName());
            formSignatory.setComments(approbation.getComments());
            formSignatory.setSignature(signature);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            formSignatory.setDate(approbation.getCreationDate().toLocalDate().format(formatter));
            signatories.add(formSignatory);
        }

        form.setSignatories(signatories);
        String fileId = (String) camundaService.getProcessVariable(delegateExecution.getProcessInstanceId(), "file");
        log.info("File ID: " + fileId);
        String base64Page = fileRestClient.getB64FileById(this.extractFileId(fileId));
        byte[] documentPage = Base64Utils.decodeBase64ToBytes(base64Page);
        Rectangle dimensions = PdfUtils.printPdfDimensions(documentPage);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        Boolean ifSigned = Boolean.valueOf(PdfUtils.getParameter(documentPage, IF_SIGNED_WITH_PAPERLESS));

        if (ifSigned) {
            log.info(IF_SIGNED_WITH_PAPERLESS);
            delegateExecution.setVariable("ifSigned", ifSigned);
            String encoded = PdfUtils.getParameter(documentPage, SIGNED_WITH_PAPERLESS);
            documentPage = PdfUtils.removeLastPage(documentPage);
            documentPage = PdfUtils.removeFooter(documentPage);

            List<CollectSignatureForm.Signatory> signatoriesToSign =
                    mapper.readValue(encoded, new TypeReference<List<CollectSignatureForm.Signatory>>() {});

            signatoriesToSign.addAll(signatories);
            form.setSignatories(signatoriesToSign);
        }

        ByteArrayResource resource = reportingRestClient.signature(form);
        log.info("Signature generated");
        byte[] signaturePage = resource.getByteArray();



        signaturePage = PdfUtils.addWatermark(signaturePage, "SIGNED WITH PAPERLESS");


        List<byte[]> documentPages = new ArrayList<>();
        documentPages.add(documentPage);
        documentPages.add(signaturePage);

        byte[] destination = PdfUtils.mergePdfs(documentPages);
        //destination = PdfUtils.addWatermark(destination, "SIGNED WITH PAPERLESS");
        destination = PdfUtils.addFooterToPdf(destination, "Signed with paperless Ref : " + request.getReference());
        String encoded = mapper.writeValueAsString(form.getSignatories());
        destination = PdfUtils.addParameter(destination, IF_SIGNED_WITH_PAPERLESS, "true");
        destination = PdfUtils.addParameter(destination, "reference", request.getReference());
        destination = PdfUtils.addParameter(destination, SIGNED_WITH_PAPERLESS, encoded);

        CustomMultipartFile multipartFile = new CustomMultipartFile(destination, "collecte_signature_" + delegateExecution.getBusinessKey() + ".pdf", "application/pdf");
        FileDto fileDto = new FileDto();
        fileDto.setAddDate(LocalDateTime.now());
        String type = (String) camundaService.getProcessVariable(delegateExecution.getProcessInstanceId(), "type");
        log.info("Type : " + type);
        String object = (String) camundaService.getProcessVariable(delegateExecution.getProcessInstanceId(), "object");
        fileDto.setName(type.toUpperCase() + " - " + object.toUpperCase());
        fileDto.setFile(Base64.getEncoder().encodeToString(resource.getByteArray()));
        fileDto.setMultipartFile(multipartFile);
        fileDto.setType("application/pdf");
        request = requestService.confirmRequest(delegateExecution.getProcessInstanceId());
        fileService.saveFile(request, fileDto);
        log.info("File generated");

        EmailAskApprovalDto ask = new EmailAskApprovalDto();
        String processDefinitionId = delegateExecution.getProcessDefinitionId();
        String owner = (String) delegateExecution.getVariable("owner");
        String reference = (String) delegateExecution.getVariable("reference");
        String signataire = "";
        type = (String) delegateExecution.getVariable("type");
        object = (String) delegateExecution.getVariable("object");
        String collect_type = (String) delegateExecution.getVariable("collect_type");
        List<String> copies  = (List<String>) camundaService.getProcessVariable(delegateExecution.getProcessInstanceId(), "copies");
        log.info("Copy people " + copies.toString() , copies);

        List<String> copieEmails = copies.stream().map(x -> x + "@cca-bank.com").collect(Collectors.toList());

        log.info("Copy people Emails " + copieEmails.toString() , copieEmails);

        String copiesString = String.join(",", copieEmails);
        log.info(copiesString);

        if(collect_type.equals("SEQUENCE")){
            signataire = (String) delegateExecution.getVariable("signataire");
        }else{
            signataire = owner;
            List<String> signataires = (List<String>) camundaService.getProcessVariable(delegateExecution.getProcessInstanceId(), "signataires");
            log.info("Signataires " + signataires.toString() , signataires);
            List<String> signatairesEmails = signataires.stream().map(x -> x + "@cca-bank.com").collect(Collectors.toList());
            log.info("Signataires Emails " + signatairesEmails.toString() , signatairesEmails);
            String signatairesString = String.join(",", signataires);
            copiesString = copiesString + "," + signatairesString;
            log.info(copiesString);
        }

        log.info("Copies String " , copiesString);

        ask.setSender(owner);
        ask.setApprover(signataire);
        ask.setReference(reference);
        ask.setCC(copiesString);
        request = requestRepository.findOneByReference(reference);
        ask.setType(request.getType().getName());
        ask.setSubject("[Signé] " + type + "-" + object);
        ask.setRole("Signataire");

        fileId = (String) camundaService.getProcessVariable(delegateExecution.getProcessInstanceId(), "file");
        log.info("File ID: " + fileId);
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("collect_signature_" + delegateExecution.getBusinessKey() + ".pdf");
        attachment.setData(fileDto.getFile());
        ask.setAttachments(new AttachmentDto[]{attachment});
        ask.setMessage("Votre document a bien été signé");
        emailService.sendFiles(ask);

    }
}
