package com.ccabank.memoservice.process.vacation.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.VacationDecision;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class SendVacationDecision implements JavaDelegate {

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private ReportingRestClient reportingRestClient;

    @Autowired
    private EmailRestClient emailRestClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        VacationDecision decision = new VacationDecision();
        String owner = (String) delegateExecution.getVariable("owner");
        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);

        ByteArrayResource resource = reportingRestClient.vacationDecision(decision);

        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(staff.getEmail());
        emailDto.setSubject("Décision de Congés");
        emailDto.setCc(staff.getEmail());
        emailDto.setBody("Décision de Congés");


        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("demande_congés.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);


    }
}
