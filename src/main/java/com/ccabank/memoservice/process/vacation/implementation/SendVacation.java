package com.ccabank.memoservice.process.vacation.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.VacationDecision;
import com.ccabank.memoservice.dto.reporting.VacationForm;
import com.ccabank.memoservice.dto.user.EmployeeFunctionInfo;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.dto.user.FunctionInfo;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.process.general.constant.IncidentTypeConstant;
import com.ccabank.memoservice.service.faces.CamundaService;
import com.ccabank.memoservice.util.DateUtil;
import com.ccabank.memoservice.util.WorkDayCalculator;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Function;

@Component
public class SendVacation implements JavaDelegate {

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private ReportingRestClient reportingRestClient;

    @Autowired
    private EmailRestClient emailRestClient;

    @Autowired
    private CamundaService camundaService;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        try{

            System.out.println("Send Valided Vacation");

            VacationForm form = new VacationForm();
            form.setDate(LocalDate.now());

            String owner = (String) delegateExecution.getVariable("owner");
            EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);
            form.setName(staff.getFirstName() + " " + staff.getLastName());
            form.setFunction(staff.getFunction().getFunction().getName());
            form.setMatricule(staff.getMatricule());
            form.setPlace("DOUALA");
            form.setUnity(staff.getDepartment().getName());
            String signature = userRestClient.getEmployeeSignature(staff.getUsername());
            form.setSignature(signature);

            LocalDate startDate = (LocalDate) delegateExecution.getVariable("startDate");
            form.setStartDate(startDate);

            Long nbDays = (Long) delegateExecution.getVariable("nbDays");
            LocalDate endDate = WorkDayCalculator.addBusinessDays(startDate, nbDays.intValue());

            form.setEndDate(endDate);

            LocalDate lastVacationDate = (LocalDate) delegateExecution.getVariable("lastVacationDate");
            form.setLastVacationDate(lastVacationDate);

            VacationForm.Interim interim = new VacationForm.Interim();
            String interimId = (String) delegateExecution.getVariable("Apbt_interimaire");

            if (interimId == null) {
                interim.setName("...");
                interim.setFunction("...");
                interim.setUnity("...");
            }else{
                EmployeeInfo interimaire =  userRestClient.getStaffByUsername(interimId);
                interim.setName(interimaire.getFirstName() + " " + interimaire.getLastName());
                interim.setFunction(interimaire.getFunction().getFunction().getName());
                interim.setUnity(interimaire.getDepartment().getName());
            }

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
            EmployeeInfo supervisorInfo2 =  userRestClient.getStaffByUsername(supervisorId);
            supervisor.setName(supervisorInfo2.getFirstName() + " " + supervisorInfo2.getLastName());
            signature = userRestClient.getEmployeeSignature(supervisorId);
            supervisor.setSignature(signature);
            form.setSupervisorNext(supervisor);


            VacationDecision decision = new VacationDecision();

            decision.setDate(LocalDate.now());
            decision.setFunction(staff.getFunction().getFunction().getName());
            decision.setEmployee(staff.getFirstName() + " " + staff.getLastName());
            decision.setMatricule(staff.getMatricule());
            decision.setStartDate(startDate);
            decision.setEndDate(endDate);
            decision.setPeriod(WorkDayCalculator.getDateRangeAsString(startDate, endDate));
            decision.setUnity(staff.getDepartment().getName());

            Long allocationDueLong = (Long) delegateExecution.getVariable("allocationDue");
            Integer allocationDue = allocationDueLong.intValue();
            decision.setAllocation(allocationDue);
            decision.setPrincipalVacation(allocationDue);

            Long majAncieneteLong = (Long) delegateExecution.getVariable("majAncienete");
            Integer majAncienete = majAncieneteLong.intValue();
            decision.setSeniority(majAncienete);

            Long majFamilleLong = (Long) delegateExecution.getVariable("majFamille");
            Integer majFamille = majFamilleLong.intValue();
            decision.setFamilyCharges(majFamille);

            Long congeAnterieurLong = (Long) delegateExecution.getVariable("congeAnterieur");
            Integer congeAnterieur = congeAnterieurLong.intValue();
            decision.setPreviousVacation(congeAnterieur);

            Long permDeductionLong = (Long) delegateExecution.getVariable("permDeduction");
            Integer permDeduction = permDeductionLong.intValue();
            decision.setPermissions(permDeduction);

            ByteArrayResource decisionVacation = reportingRestClient.vacationDecision(decision);


            ByteArrayResource resource = reportingRestClient.vacation(form);

            EmailDto emailDto = new EmailDto();
            emailDto.setFrom("notification@cca-bank.com");
            emailDto.setTo(staff.getEmail());
            emailDto.setSubject("Demande de Congés Validées");
            emailDto.setCc(staff.getEmail());
            emailDto.setBody("Demande de Congés Validées");
            AttachmentDto attachment = new AttachmentDto();
            attachment.setName("demande_congés.pdf");
            attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));

            AttachmentDto attachmentDecision = new AttachmentDto();
            attachmentDecision.setName("decision_congés.pdf");
            attachmentDecision.setData(Base64.getEncoder().encodeToString(decisionVacation.getByteArray()));

            emailDto.setAttachments(new AttachmentDto[]{attachment, attachmentDecision});
            this.emailRestClient.send(emailDto);

        }catch (Exception e){

            camundaService.createIncident(delegateExecution.getProcessInstanceId(), IncidentTypeConstant.TECHNICAL, "Vacation Generation " + e.getMessage() );
            throw new Exception(e.getMessage());

        }




    }
}
