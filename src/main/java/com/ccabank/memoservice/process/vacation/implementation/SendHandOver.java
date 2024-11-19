package com.ccabank.memoservice.process.vacation.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.HandOverForm;
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
import java.util.Date;

@Component
public class SendHandOver implements JavaDelegate {

    @Autowired
    private ReportingRestClient reportingRestClient;

    @Autowired
    private EmailRestClient emailRestClient;

    @Autowired
    private UserRestClient userRestClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("Send Hand Over");

        //Générer le HandOver
        HandOverForm handOverForm = new HandOverForm();

        HandOverForm.Employee employee = new HandOverForm.Employee();
        employee.setDate(LocalDate.now());

        String owner = (String) delegateExecution.getVariable("owner");

        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);
        employee.setName(staff.getFirstName() + " " + staff.getLastName());
        employee.setFunction(staff.getFunctionalTitle());

        String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        employee.setSignature(signature);
        handOverForm.setEmployee(employee);

        LocalDate startDate = DateUtil.convertStringToLocalDate((String) delegateExecution.getVariable("realStartDate")) ;
        handOverForm.setStartDate(LocalDate.now());

        LocalDate endDate = DateUtil.convertStringToLocalDate((String) delegateExecution.getVariable("endDate"));
        handOverForm.setEndDate(LocalDate.now());


        HandOverForm.Employee interim = new HandOverForm.Employee();
        interim.setDate(LocalDate.now());

        String interimId = (String) delegateExecution.getVariable("Apbt_interimaire");
        EmployeeInfo interimaire =  userRestClient.getStaffByUsername(interimId);



        interim.setName(interimaire.getFirstName() + " " + interimaire.getLastName());
        interim.setFunction(interimaire.getFunctionalTitle());
        signature = userRestClient.getEmployeeSignature(interimaire.getUsername());
        interim.setSignature(signature);

        handOverForm.setInterim(interim);


        HandOverForm.Employee supervisorModel = new HandOverForm.Employee();
        supervisorModel.setDate(LocalDate.now());

        String supervisorId = (String) delegateExecution.getVariable("Apbt_n1");
        EmployeeInfo supervisor =  userRestClient.getStaffByUsername(supervisorId);

        supervisorModel.setName(supervisor.getFirstName() + " " + supervisor.getLastName());
        supervisorModel.setFunction(supervisor.getFunctionalTitle());

        signature = userRestClient.getEmployeeSignature(supervisor.getUsername());
        supervisor.setSignature(signature);

        handOverForm.setSupervisor(supervisorModel);


        String criticFolder = (String) delegateExecution.getVariable("criticFolder");

        handOverForm.setActivities(criticFolder);
        String mainWork = (String) delegateExecution.getVariable("mainWork");
        handOverForm.setResponsibilities(mainWork);

        System.out.println(handOverForm.toString());

        //HandOverForm handOverForm1 = new HandOverForm();

        //Envoyer le HandOver Par Email à l'intérimaire
        ByteArrayResource resource = this.reportingRestClient.handover(handOverForm);

        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(interimaire.getEmail());
        emailDto.setSubject("Formulaire de Hand Over");
        emailDto.setCc(staff.getEmail());
        emailDto.setBody("En pièce jointe le formulaire de HandOver");
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("handover.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);

    }
}
