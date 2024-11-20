package com.ccabank.memoservice.process.memo.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.MemoForm;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.process.general.service.RequestService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class ConfirmMemo implements JavaDelegate {

    @Autowired
    private ReportingRestClient reportingRestClient;

    @Autowired
    private EmailRestClient emailRestClient;

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private RequestService requestService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        requestService.confirmRequest(delegateExecution.getProcessInstanceId());

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

        while (nbSignatory <= 5){
            apbt = (String) delegateExecution.getVariable("Apbt_n" + nbSignatory);
            if(apbt != null){
                signataire =  userRestClient.getStaffByUsername(apbt);
                MemoForm.Signatory signatory = new MemoForm.Signatory();
                signatory.setDate(LocalDate.now());
                signatory.setName(signataire.getFirstName() + " " + signataire.getLastName());
                signatory.setSignature(userRestClient.getEmployeeSignature(signataire.getUsername()));
                signatories.add(signatory);
            }
            nbSignatory++;
        }

        form.setSignatories(signatories);

        ByteArrayResource resource = this.reportingRestClient.memo(form);

        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(staff.getEmail());
        emailDto.setSubject("MEMO - " + subject.toUpperCase());
        emailDto.setCc(staff.getEmail());
        emailDto.setBody(subject);
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("memo.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);

    }
}
