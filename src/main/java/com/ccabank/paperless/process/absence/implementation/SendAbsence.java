package com.ccabank.paperless.process.absence.implementation;

import com.ccabank.paperless.dto.email.AttachmentDto;
import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.dto.reporting.AbsenceForm;
import com.ccabank.paperless.dto.user.EmployeeFunctionInfo;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.dto.user.FunctionInfo;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.openfeign.ReportingRestClient;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.process.general.constant.ApprobationLevel;
import com.ccabank.paperless.process.general.constant.EmailGroup;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.EmailService;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.util.CustomMultipartFile;
import com.ccabank.paperless.util.WorkDayCalculator;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Base64;

import com.ccabank.paperless.process.general.service.RequestService;

import javax.ws.rs.BadRequestException;


@Component
public class SendAbsence implements JavaDelegate {

    @Autowired
    private ReportingRestClient reportingRestClient;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private FileService fileService;

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

        form.setDays(nbDays);

         endDate = (LocalDate) delegateExecution.getVariable("endDate");

        String reason = (String) delegateExecution.getVariable("reason");

        form.setReason(reason);
        form.setStartDate(startDate);
        form.setEndDate(endDate);

        String interim = (String) delegateExecution.getVariable("interim");
        if(interim != null){
            EmployeeInfo interimaire =  userRestClient.getStaffByUsername(interim);
            form.setInterim(interimaire.getFirstName() + " " + interimaire.getLastName());
        }

        try{
            form.setSignature(userRestClient.getEmployeeSignature(staff.getUsername()));
        }catch (Exception e){
            throw new BadRequestException("La Signature de la employee " + staff.getUsername() +  " n'existe pas");
        }

        String n1 = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_N1);
        EmployeeInfo Apbt_n1 =  userRestClient.getStaffByUsername(n1);
        AbsenceForm.Signatory supervisor = new AbsenceForm.Signatory();
        supervisor.setDate(LocalDate.now());
        supervisor.setName(Apbt_n1.getFirstName() + " " + Apbt_n1.getLastName());
        supervisor.setSignature(userRestClient.getEmployeeSignature(Apbt_n1.getUsername()));

        form.setSignatory1(supervisor);

        List<AbsenceForm.Signatory> signatures = new ArrayList<AbsenceForm.Signatory>();


        String apbt_n2 = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_N2);

        if(apbt_n2 != null){
            String n2 = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_N2);
            EmployeeInfo Apbt_n2 =  userRestClient.getStaffByUsername(n2);
            AbsenceForm.Signatory supervisor2 = new AbsenceForm.Signatory();
            supervisor2.setDate(LocalDate.now());
            supervisor2.setName(Apbt_n2.getFirstName() + " " + Apbt_n2.getLastName());
            supervisor2.setSignature(userRestClient.getEmployeeSignature(Apbt_n2.getUsername()));
            form.setSignatory2(supervisor2);

            AbsenceForm.Signatory supervisor2Signatory = new AbsenceForm.Signatory();
            supervisor2Signatory.setDate(LocalDate.now());
            supervisor2Signatory.setName(supervisor2.getName());
            supervisor2Signatory.setSignature(supervisor2.getSignature());
            signatures.add(supervisor2Signatory);
        }


        String direction = "";

        direction = (String) delegateExecution.getVariable("Apbt_DG");
        if(direction != null){
            EmployeeInfo DG =  userRestClient.getStaffByUsername(direction);
            AbsenceForm.Signatory directionG = new AbsenceForm.Signatory();
            directionG.setDate(LocalDate.now());
            directionG.setName(DG.getFirstName() + " " + DG.getLastName());
            directionG.setSignature(userRestClient.getEmployeeSignature(DG.getUsername()));
            form.setHeadOffice(directionG);
        }

        String deduction = (String) delegateExecution.getVariable("deduction");
        form.setDeduction(AbsenceForm.Deduction.valueOf(deduction));

        Long absence = (Long) delegateExecution.getVariable("absence");
        form.setAbsence(absence.doubleValue());

        Long stock = (Long) delegateExecution.getVariable("stock");
        form.setStock(stock.doubleValue());

        Long advice = (Long) delegateExecution.getVariable("advice");
        form.setAdvice(advice.doubleValue());
        form.setDays(advice.intValue());


        Long rights = (Long) delegateExecution.getVariable("rights");
        form.setRights(rights.doubleValue());


        AbsenceForm.Signatory supervisorSignatory = new AbsenceForm.Signatory();
        supervisorSignatory.setDate(LocalDate.now());
        supervisorSignatory.setName(supervisor.getName());
        supervisorSignatory.setSignature(supervisor.getSignature());
        signatures.add(supervisorSignatory);



        //signatures.add(DG.getSignature());

        String apbt_ca = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_CA_SAISIE);
        EmployeeInfo apbt = userRestClient.getStaffByUsername(apbt_ca);
        if(apbt_ca != null){
            AbsenceForm.Signatory apbtSignatory = new AbsenceForm.Signatory();
            apbtSignatory.setDate(LocalDate.now());
            apbtSignatory.setName(apbt.getFirstName() + " " + apbt.getLastName());
            apbtSignatory.setSignature(userRestClient.getEmployeeSignature(apbt_ca));
            signatures.add(apbtSignatory);
        }
        apbt_ca = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_CA_SUPERVISION);
        apbt = userRestClient.getStaffByUsername(apbt_ca);
        if(apbt_ca != null){
            AbsenceForm.Signatory apbtSignatory = new AbsenceForm.Signatory();
            apbtSignatory.setDate(LocalDate.now());
            apbtSignatory.setName(apbt.getFirstName() + " " + apbt.getLastName());
            apbtSignatory.setSignature(userRestClient.getEmployeeSignature(apbt_ca));
            signatures.add(apbtSignatory);
        }
        apbt_ca = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_CA_VALIDATION);
        apbt = userRestClient.getStaffByUsername(apbt_ca);
        if(apbt_ca != null){
            AbsenceForm.Signatory apbtSignatory = new AbsenceForm.Signatory();
            apbtSignatory.setDate(LocalDate.now());
            apbtSignatory.setName(apbt.getFirstName() + " " + apbt.getLastName());
            apbtSignatory.setSignature(userRestClient.getEmployeeSignature(apbt_ca));
            signatures.add(apbtSignatory);
        }

        form.setSignatories(signatures);

        //Envoyer le HandOver Par Email à l'intérimaire
        ByteArrayResource resource = this.reportingRestClient.absence(form);


        EmailAskApprovalDto ask = new EmailAskApprovalDto();
        ask.setSender(staff.getUsername());
        ask.setSubject("Autorisation d'absence");
        ask.setbCC(Apbt_n1.getEmail() + "," + EmailGroup.EMAIL_HABILITATION + "," + EmailGroup.EMAIL_CAPITAL_HUMAIN);
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("absence" + delegateExecution.getBusinessKey() + ".pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        ask.setAttachments(new AttachmentDto[]{attachment});
        emailService.sendFiles(ask);

        CustomMultipartFile multipartFile = new CustomMultipartFile(resource.getByteArray(), attachment.getName(), "application/pdf");

        FileDto fileDto = new FileDto();
        fileDto.setAddDate(LocalDateTime.now());
        fileDto.setName("Autorisation d'absence");
        fileDto.setFile(Base64.getEncoder().encodeToString(resource.getByteArray()));
        fileDto.setMultipartFile(multipartFile);
        fileDto.setType("application/pdf");
        Request request = requestRepository.findByInstanceId(delegateExecution.getProcessInstanceId());
        fileService.saveFile(request, fileDto);

        /*EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(staff.getEmail());
        emailDto.setSubject("Autorisation d'absence");
        emailDto.setCc(staff.getEmail());
        emailDto.setBody("Autorisation d'absence");
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("absence.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);*/


        requestService.confirmRequest(delegateExecution.getProcessInstanceId());

    }
}
