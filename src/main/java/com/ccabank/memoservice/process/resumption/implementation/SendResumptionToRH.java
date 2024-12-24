package com.ccabank.memoservice.process.resumption.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.ResumptionForm;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.process.general.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class SendResumptionToRH implements JavaDelegate {
    private final UserRestClient userRestClient;
    private final ReportingRestClient reportingRestClient;
    private final EmailRestClient emailRestClient;
    private final RequestService requestService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        requestService.confirmRequest(delegateExecution.getProcessInstanceId());

        ResumptionForm form = new ResumptionForm();

        String owner = (String) delegateExecution.getVariable("owner");
        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);


        form.setDate(LocalDate.now());
        form.setName(staff.getFirstName() + " " + staff.getLastName());
        form.setFunction(staff.getFunction().getFunction().getName());
        form.setMatricule(staff.getMatricule());
        form.setUnity(staff.getDepartment().getName());
        form.setSignature(userRestClient.getEmployeeSignature(staff.getUsername()));

        LocalDate startDate = (LocalDate) delegateExecution.getVariable("startDate");
        LocalDate endDate = (LocalDate) delegateExecution.getVariable("endDate");
        LocalDate realEndDate = (LocalDate) delegateExecution.getVariable("realEndDate");

        form.setStartDate(startDate);
        form.setEndDate(endDate);
        form.setRealEndDate(realEndDate);
        form.setPlace("DOUALA");
        String reason = (String) delegateExecution.getVariable("reason");
        form.setReason(ResumptionForm.Reason.valueOf(reason));
        if(form.getReason().equals(ResumptionForm.Reason.OTHER)){
            String explication = (String) delegateExecution.getVariable("explication");
            form.setExplication(explication);
        }

        String supervisorUser = (String) delegateExecution.getVariable("Apbt_n1");
        EmployeeInfo supervisor =  userRestClient.getStaffByUsername(supervisorUser);


        ResumptionForm.Signatory signatory = new ResumptionForm.Signatory();
        signatory.setName(supervisor.getFirstName() + " " + supervisor.getLastName());
        signatory.setSignature(userRestClient.getEmployeeSignature(supervisor.getUsername()));
        form.setSupervisor(signatory);

        //Envoyer le HandOver Par Email à l'intérimaire
        ByteArrayResource resource = this.reportingRestClient.resumption(form);

        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(staff.getEmail());
        emailDto.setSubject("Fiche de Reprise de Service");
        emailDto.setCc(supervisor.getEmail());
        emailDto.setBody("En pièce jointe la fiche de reprise de service");
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("reprise_service.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);

    }
}
