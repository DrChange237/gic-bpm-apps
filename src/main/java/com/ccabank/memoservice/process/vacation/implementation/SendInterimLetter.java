package com.ccabank.memoservice.process.vacation.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.InterimForm;
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
public class SendInterimLetter implements JavaDelegate {

    @Autowired
    private ReportingRestClient reportingRestClient;

    @Autowired
    private EmailRestClient emailRestClient;

    @Autowired
    private UserRestClient userRestClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("Sending interim letter");

        InterimForm form = new InterimForm();

        String interimId = (String) delegateExecution.getVariable("Apbt_interimaire");
        EmployeeInfo interimaire =  userRestClient.getStaffByUsername(interimId);

        InterimForm.Employee interim = new InterimForm.Employee();
        interim.setMatricule(interimaire.getMatricule());
        interim.setName(interimaire.getFirstName() + " " + interimaire.getLastName());
        interim.setFunction(interimaire.getFunctionalTitle());
        interim.setSex(InterimForm.Employee.Sex.MALE);
        form.setInterim(interim);

        String owner = (String) delegateExecution.getVariable("Apbt_interimaire");
        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);

        InterimForm.Employee employee = new InterimForm.Employee();
        employee.setMatricule(staff.getMatricule());
        employee.setName(staff.getFirstName() + " " + staff.getLastName());
        employee.setFunction(staff.getFunctionalTitle());
        employee.setSex(InterimForm.Employee.Sex.MALE);
        form.setEmployee(employee);

        form.setDate(LocalDate.now());

        // Numero du bas
        form.setNumber("XXX");



        form.setSubject(InterimForm.Subject.INTERIM);

        //Note à Generer
        form.setNoteId("NOTE");

        LocalDate startDate = DateUtil.convertStringToLocalDate((String) delegateExecution.getVariable("startDate")) ;
        LocalDate endDate = DateUtil.convertStringToLocalDate((String) delegateExecution.getVariable("endDate")) ;

        form.setStartDate(LocalDate.now());
        form.setEndDate(LocalDate.now());


        String signature = userRestClient.getEmployeeSignature(interimaire.getUsername());
        form.setCachet(signature);


        System.out.println(form);

        ByteArrayResource resource = reportingRestClient.interim(form);

        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(interimaire.getEmail());
        emailDto.setSubject("Lettre d'intérim");
        emailDto.setCc(staff.getEmail());
        emailDto.setBody("Lettre d'intérim");

        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("lettre_interim.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);

    }
}
