package com.ccabank.paperless.process.collect_signature.implementation;

import com.ccabank.paperless.dto.email.AttachmentDto;
import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.openfeign.FileRestClient;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.EmailService;
import com.ccabank.paperless.util.file.Base64Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CSSendAskEmail implements JavaDelegate {

    private final CamundaService camundaService;
    private final RequestRepository requestRepository;
    private final EmailService emailService;
    private final FileRestClient fileRestClient;

    public String extractFileId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        // Récupère la partie après le dernier "/"
        return url.substring(url.lastIndexOf("/") + 1);
    }


    @Override
    public void execute(DelegateExecution execution) throws Exception {

        EmailAskApprovalDto ask = new EmailAskApprovalDto();
        String processDefinitionId = execution.getProcessDefinitionId();
        String owner = (String) execution.getVariable("owner");
        String reference = (String) execution.getVariable("reference");
        String signataire = "";
        String type = (String) execution.getVariable("type");
        String object = (String) execution.getVariable("object");
        String collect_type = (String) execution.getVariable("collect_type");
        List<String> copies  = (List<String>) execution.getVariable("copies");
        String copiesString = String.join(",", copies);

        if(collect_type.equals("SEQUENCE")){
            signataire = (String) execution.getVariable("signataire");
        }else{
            signataire = owner;
            List<String> signataires = (List<String>) execution.getVariable("signataires");
            String signatairesString = String.join(",", signataires);
            copiesString = copiesString + "," + signatairesString;
        }

        ask.setSender(owner);
        ask.setApprover(signataire);
        ask.setReference(reference);
        ask.setCC(copiesString);
        Request request = requestRepository.findOneByReference(reference);
        ask.setType(request.getType().getName());
        ask.setSubject("[Signature] " + type + "-" + object);
        ask.setRole("Signataire");

        String fileId = (String) camundaService.getProcessVariable(execution.getProcessInstanceId(), "file");
        log.info("File ID: " + fileId);
        String base64Page = fileRestClient.getB64FileById(this.extractFileId(fileId));
        byte[] documentPage = Base64Utils.decodeBase64ToBytes(base64Page);
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("collect_signature_" + execution.getBusinessKey() + ".pdf");
        attachment.setData(Base64.getEncoder().encodeToString(documentPage));
        ask.setAttachments(new AttachmentDto[]{attachment});
        ask.setMessage("Vous avez un document en attente de siganture");
        emailService.sendFiles(ask);

    }
}
