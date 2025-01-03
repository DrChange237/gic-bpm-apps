package com.ccabank.memoservice.process.absence.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.AbsenceForm;
import com.ccabank.memoservice.dto.user.EmployeeFunctionInfo;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.dto.user.FunctionInfo;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.service.faces.CamundaService;
import com.ccabank.memoservice.util.WorkDayCalculator;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import com.ccabank.memoservice.process.general.service.RequestService;


@Component
public class SendAbsence implements JavaDelegate {

    @Autowired
    private ReportingRestClient reportingRestClient;

    @Autowired
    private EmailRestClient emailRestClient;

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private CamundaService camundaService;

    @Autowired
    private RequestService requestService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        AbsenceForm form = new AbsenceForm();
        String owner = (String) delegateExecution.getVariable("owner");
        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);

        form.setName(staff.getFirstName() + " " + staff.getLastName());
        form.setFunction(Optional.ofNullable(staff.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
        form.setDate(LocalDate.now());
        form.setMatricule(staff.getMatricule());
        form.setPlace("DOUALA");
        form.setUnity(staff.getDepartment().getName());

        Date startDateD = (Date) delegateExecution.getVariable("startDate") ;
        LocalDate startDate = startDateD.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();



        int nbDays = (Integer) delegateExecution.getVariable("nbDays");
        LocalDate endDate = WorkDayCalculator.addBusinessDays(startDate, nbDays);
        delegateExecution.setVariable("endDate", endDate);

         endDate = (LocalDate) delegateExecution.getVariable("endDate");

        String reason = (String) delegateExecution.getVariable("reason");

        form.setReason(reason);
        form.setStartDate(startDate);
        form.setEndDate(endDate);

        String interim = (String) delegateExecution.getVariable("interim");
        EmployeeInfo interimaire =  userRestClient.getStaffByUsername(interim);
        form.setInterim(interimaire.getFirstName() + " " + interimaire.getLastName());



        form.setSignature(userRestClient.getEmployeeSignature(staff.getUsername()));

        String n1 = (String) delegateExecution.getVariable("Apbt_n1");
        EmployeeInfo Apbt_n1 =  userRestClient.getStaffByUsername(n1);
        AbsenceForm.Signatory supervisor = new AbsenceForm.Signatory();
        supervisor.setDate(LocalDate.now());
        supervisor.setName(Apbt_n1.getFirstName() + " " + Apbt_n1.getLastName());
        supervisor.setSignature(userRestClient.getEmployeeSignature(Apbt_n1.getUsername()));

        form.setSignatory1(supervisor);

        String n2 = (String) delegateExecution.getVariable("Apbt_n2");
        EmployeeInfo Apbt_n2 =  userRestClient.getStaffByUsername(n2);
        AbsenceForm.Signatory supervisor2 = new AbsenceForm.Signatory();
        supervisor2.setDate(LocalDate.now());
        supervisor2.setName(Apbt_n2.getFirstName() + " " + Apbt_n2.getLastName());
        supervisor2.setSignature(userRestClient.getEmployeeSignature(Apbt_n2.getUsername()));

        form.setSignatory2(supervisor2);

        String direction = "";

        direction = (String) delegateExecution.getVariable("Apbt_DG");
        EmployeeInfo DG =  userRestClient.getStaffByUsername(direction);
        AbsenceForm.Signatory directionG = new AbsenceForm.Signatory();
        directionG.setDate(LocalDate.now());
        directionG.setName(DG.getFirstName() + " " + DG.getLastName());
        directionG.setSignature(userRestClient.getEmployeeSignature(DG.getUsername()));
        form.setHeadOffice(directionG);


        String deduction = (String) delegateExecution.getVariable("deduction");
        form.setDeduction(AbsenceForm.Deduction.valueOf(deduction));

        Long absence = (Long) delegateExecution.getVariable("absence");
        form.setAbsence(absence.doubleValue());

        Long stock = (Long) delegateExecution.getVariable("stock");
        form.setStock(stock.doubleValue());

        Long advice = (Long) delegateExecution.getVariable("advice");
        form.setAdvice(advice.doubleValue());

        Long rights = (Long) delegateExecution.getVariable("rights");
        form.setRights(rights.doubleValue());

        List<String> signatures = new ArrayList<String>();
        signatures.add(supervisor.getSignature());
        signatures.add(supervisor2.getSignature());
        //signatures.add(DG.getSignature());

        form.setSignatures(signatures);


        //Envoyer le HandOver Par Email à l'intérimaire
        ByteArrayResource resource = this.reportingRestClient.absence(form);

        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(staff.getEmail());
        emailDto.setSubject("Autorisation d'absence");
        emailDto.setCc(staff.getEmail());
        emailDto.setBody("Autorisation d'absence");
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("absence.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);

        requestService.confirmRequest(delegateExecution.getProcessInstanceId());

    }
}
