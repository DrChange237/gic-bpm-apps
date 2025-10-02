package com.ccabank.paperless.process.memo.implementation;

import com.ccabank.paperless.dto.email.AttachmentDto;
import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.dto.reporting.MemoForm;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.openfeign.ReportingRestClient;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.process.general.service.RequestService;
import com.ccabank.paperless.service.faces.EmailService;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.util.CustomMultipartFile;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConfirmMemo implements JavaDelegate {

    private final ReportingRestClient reportingRestClient;
    private final EmailService emailService;
    private final UserRestClient userRestClient;
    private final RequestService requestService;
    private final FileService fileService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Request request = requestService.confirmRequest(delegateExecution.getProcessInstanceId());

        MemoForm form = new MemoForm();

        String owner = (String) delegateExecution.getVariable("owner");
        String subject = (String) delegateExecution.getVariable("subject");

        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);

        form.setDate(LocalDate.now());
        form.setReference("reference");
        form.setSubject(subject);
        String body = (String) delegateExecution.getVariable("body");
        form.setBody(body);
        String material = (String) delegateExecution.getVariable("material");
        form.setMaterial(material);

        String receiver = (String) delegateExecution.getVariable("receiver");
        form.setReceiver(receiver);

        int nbSignatory = 0;
        String apbt;
        EmployeeInfo signataire;
        List<MemoForm.Signatory> signatories = new ArrayList<>();

        String listApbt = "";

        while (nbSignatory <= 5){
            apbt = (String) delegateExecution.getVariable("Apbt_n" + nbSignatory);
            if(apbt != null){
                signataire =  userRestClient.getStaffByUsername(apbt);
                MemoForm.Signatory signatory = new MemoForm.Signatory();
                signatory.setDate(LocalDate.now());
                signatory.setName(signataire.getFirstName() + " " + signataire.getLastName());
                signatory.setSignature(userRestClient.getEmployeeSignature(signataire.getUsername()));
                signatories.add(signatory);
                listApbt = listApbt + apbt;
            }
            nbSignatory++;
        }

        form.setSignatories(signatories);

        ByteArrayResource resource = this.reportingRestClient.memo(form);

        EmailAskApprovalDto ask = new EmailAskApprovalDto();
        ask.setSender(staff.getUsername());
        ask.setSubject("MEMO - " + subject.toUpperCase());
        ask.setBCC(listApbt);
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("memo" + delegateExecution.getBusinessKey() + ".pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        ask.setAttachments(new AttachmentDto[]{attachment});
        ask.setMessage("Votre document a été généré avec succès bien vouloir prendre connaissance");
        emailService.sendFiles(ask);

        CustomMultipartFile multipartFile = new CustomMultipartFile(resource.getByteArray(), attachment.getName(), "application/pdf");
        FileDto fileDto = new FileDto();
        fileDto.setAddDate(LocalDateTime.now());
        fileDto.setName("MEMO - " + subject.toUpperCase());
        fileDto.setFile(Base64.getEncoder().encodeToString(resource.getByteArray()));
        fileDto.setMultipartFile(multipartFile);
        fileDto.setType("application/pdf");
        fileService.saveFile(request, fileDto);

        /*EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(staff.getEmail());
        emailDto.setSubject("MEMO - " + subject.toUpperCase());
        emailDto.setCc(staff.getEmail());
        emailDto.setBody(subject);
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("memo.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);*/

    }
}
