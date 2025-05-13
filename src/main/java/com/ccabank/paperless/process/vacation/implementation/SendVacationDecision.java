package com.ccabank.paperless.process.vacation.implementation;

import com.ccabank.paperless.dto.email.AttachmentDto;
import com.ccabank.paperless.dto.email.EmailDto;
import com.ccabank.paperless.dto.reporting.VacationDecision;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.openfeign.EmailRestClient;
import com.ccabank.paperless.openfeign.ReportingRestClient;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.process.general.constant.IncidentTypeConstant;
import com.ccabank.paperless.service.faces.CamundaService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class SendVacationDecision implements JavaDelegate {
    private final UserRestClient userRestClient;
    private final ReportingRestClient reportingRestClient;
    private final EmailRestClient emailRestClient;
    private final CamundaService camundaService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        try{
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
        }catch (Exception e){
            camundaService.createIncident(delegateExecution.getProcessInstanceId(), IncidentTypeConstant.TECHNICAL, "Vacation Decision Generation " + e.getMessage() );
        }
    }
}
