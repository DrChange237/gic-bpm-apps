package com.ccabank.memoservice.process.resumption.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.ResumptionForm;
import com.ccabank.memoservice.dto.user.EmployeeFunctionInfo;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.dto.user.FunctionInfo;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.process.general.service.RequestService;
<<<<<<< HEAD
import com.ccabank.memoservice.repository.GroupRepository;
import com.ccabank.memoservice.security.Authority;
import com.ccabank.memoservice.service.faces.CamundaService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
=======
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
>>>>>>> 1540f9e8ef809a73640b20186a60152ee0af8c94
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SendResumptionToRH implements JavaDelegate {
    private final UserRestClient userRestClient;
    private final ReportingRestClient reportingRestClient;
    private final EmailRestClient emailRestClient;
    private final RequestService requestService;

    @Autowired
    private CamundaService camundaService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        requestService.confirmRequest(delegateExecution.getProcessInstanceId());

        ResumptionForm form = new ResumptionForm();

        String owner = (String) delegateExecution.getVariable("owner");
        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);


        form.setDate(LocalDate.now());
        form.setName(staff.getFirstName() + " " + staff.getLastName());
        form.setFunction(Optional.ofNullable(staff.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
        form.setMatricule(staff.getMatricule());
        form.setUnity(staff.getDepartment().getName());
        form.setSignature(userRestClient.getEmployeeSignature(staff.getUsername()));

        Date startDateD = (Date) delegateExecution.getVariable("startDate");
        LocalDate startDate = startDateD.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Date endDateD = (Date) delegateExecution.getVariable("endDate");
        LocalDate endDate = endDateD.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Date realEndDateD = (Date) delegateExecution.getVariable("realEndDate");
        LocalDate realEndDate = realEndDateD.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

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

        List<User> users = camundaService.getGroupDetailsWithMembers("notification-capital-humain");

        String emailList = "";

        for (User user : users) {
            emailList += user.getEmail() + ",";
        }

        emailList = emailList + supervisor.getEmail() + ",";

        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(staff.getEmail());
        emailDto.setSubject("Fiche de Reprise de Service");
        emailDto.setCc(emailList);
        emailDto.setBody("En pièce jointe la fiche de reprise de service");
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("reprise_service.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);

    }
}
