package com.ccabank.memoservice.process.vacation.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.VacationForm;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.util.DateUtil;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Base64;

@Component
public class SendVacation implements JavaDelegate {

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private ReportingRestClient reportingRestClient;

    @Autowired
    private EmailRestClient emailRestClient;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("Send Valided Vacation");

        VacationForm form = new VacationForm();
        form.setDate(LocalDate.now());

        String owner = (String) delegateExecution.getVariable("owner");
        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);
        form.setName(staff.getFirstName() + " " + staff.getLastName());
        form.setFunction(staff.getFunctionalTitle());
        form.setMatricule(staff.getMatricule());
        form.setPlace(staff.getAgency().getName());
        form.setUnity(staff.getDepartment().getName());
        String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        form.setSignature(signature);

        LocalDate startDate = DateUtil.convertStringToLocalDate((String) delegateExecution.getVariable("startDate")) ;
        form.setStartDate(startDate);

        LocalDate endDate = DateUtil.convertStringToLocalDate((String) delegateExecution.getVariable("endDate"));
        form.setEndDate(endDate);

        LocalDate lastVacationDate = DateUtil.convertStringToLocalDate((String) delegateExecution.getVariable("lastVacationDate"));
        form.setLastVacationDate(lastVacationDate);

        VacationForm.Interim interim = new VacationForm.Interim();
        String interimId = (String) delegateExecution.getVariable("Apbt_interimaire");
        EmployeeInfo interimaire =  userRestClient.getStaffByUsername(interimId);
        interim.setName(interimaire.getFirstName() + " " + interimaire.getLastName());
        interim.setFunction(interimaire.getFunctionalTitle());
        interim.setUnity(interimaire.getDepartment().getName());
        form.setInterim(interim);

        VacationForm.Signatory supervisor = new VacationForm.Signatory();
        String supervisorId = (String) delegateExecution.getVariable("Apbt_n1");
        EmployeeInfo supervisorInfo =  userRestClient.getStaffByUsername(supervisorId);
        supervisor.setName(supervisorInfo.getFirstName() + " " + supervisorInfo.getLastName());
        signature = userRestClient.getEmployeeSignature(supervisorId);
        supervisor.setSignature(signature);
        form.setSupervisor(supervisor);

        supervisor = new VacationForm.Signatory();
        supervisorId = (String) delegateExecution.getVariable("Apbt_n2");
        supervisorInfo =  userRestClient.getStaffByUsername(supervisorId);
        supervisor.setName(supervisorInfo.getFirstName() + " " + supervisorInfo.getLastName());
        signature = userRestClient.getEmployeeSignature(supervisorId);
        supervisor.setSignature(signature);
        form.setSupervisorNext(supervisor);

        ByteArrayResource resource = reportingRestClient.vacation(form);

        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(interimaire.getEmail());
        emailDto.setSubject("Demande de Congés Validées");
        emailDto.setCc(staff.getEmail());

        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("demande_congés.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);


    }
}
