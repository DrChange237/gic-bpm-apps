package com.ccabank.paperless.process.vacation.implementation;

import com.ccabank.paperless.dto.email.AttachmentDto;
import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.dto.reporting.MissionForm;
import com.ccabank.paperless.dto.reporting.VacationDecision;
import com.ccabank.paperless.dto.reporting.VacationForm;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.dto.user.FunctionInfo;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.exception.BadRequestException;
import com.ccabank.paperless.openfeign.ReportingRestClient;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.process.general.constant.ApprobationLevel;
import com.ccabank.paperless.process.general.service.RequestService;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.EmailService;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.service.faces.ProcessUnityService;
import com.ccabank.paperless.util.CustomMultipartFile;
import com.ccabank.paperless.dto.user.EmployeeFunctionInfo;
import com.ccabank.paperless.util.WorkDayCalculator;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static com.ccabank.paperless.process.general.constant.EmailGroup.EMAIL_CAPITAL_HUMAIN;
import static com.ccabank.paperless.process.general.constant.EmailGroup.EMAIL_HABILITATION;

@Component
@RequiredArgsConstructor
public class SendVacation implements JavaDelegate {

        private static final Logger log = LoggerFactory.getLogger(SendVacation.class);

        private final UserRestClient userRestClient;

        private final ReportingRestClient reportingRestClient;

        private final EmailService emailService;

        private final RequestService requestService;

        private final FileService fileService;

        private final ProcessUnityService processUnityService;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {


            log.info("Send Valided Vacation");

            Request request = requestService.confirmRequest(delegateExecution.getProcessInstanceId());


            VacationForm form = new VacationForm();
            form.setDate(LocalDate.now());

            String owner = (String) delegateExecution.getVariable("owner");
            EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);
            form.setName(staff.getFirstName() + " " + staff.getLastName());
            form.setFunction(Optional.ofNullable(staff.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
            form.setMatricule(staff.getMatricule());
            form.setPlace("DOUALA");
            form.setUnity(staff.getDepartment().getName());
            String signature = userRestClient.getEmployeeSignature(staff.getUsername());
            form.setSignature(signature);


            Date startDateD = (Date) delegateExecution.getVariable("realStartDate");
            LocalDate startDate = startDateD.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            form.setStartDate(startDate);

            Long nbDays = (Long) delegateExecution.getVariable("nbDays");
            LocalDate endDate = WorkDayCalculator.addBusinessDays(startDate, nbDays.intValue());

            form.setEndDate(endDate);

            Date repriseDateD = (Date) delegateExecution.getVariable("reprise_date");
            LocalDate repriseDate = null;
            if(repriseDateD != null) {
                    repriseDate = repriseDateD.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    form.setEndDate(repriseDate);

            }

            Date lastVacationDateD = (Date) delegateExecution.getVariable("realLastVacationDate");
            LocalDate lastVacationDate = lastVacationDateD.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
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
                interim.setFunction(Optional.ofNullable(staff.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
                interim.setUnity(interimaire.getDepartment().getName());
            }

            form.setInterim(interim);

            VacationForm.Signatory supervisor = new VacationForm.Signatory();
            String supervisorId = (String) delegateExecution.getVariable("Apbt_n1");
            EmployeeInfo supervisorInfo = new EmployeeInfo();
            if(supervisorId != null){
                    supervisorInfo =  userRestClient.getStaffByUsername(supervisorId);
                    supervisor.setName(supervisorInfo.getFirstName() + " " + supervisorInfo.getLastName());
                    signature = userRestClient.getEmployeeSignature(supervisorId);
                    supervisor.setSignature(signature);
                    form.setSupervisor(supervisor);
            }



            supervisor = new VacationForm.Signatory();
            supervisorId = (String) delegateExecution.getVariable("Apbt_n2");
            if(supervisorId != null){
                    EmployeeInfo supervisorInfo2 =  userRestClient.getStaffByUsername(supervisorId);
                    log.info(supervisorId);
                    if(supervisorInfo2 == null){
                            throw new BadRequestException("User N + 2 not found " + supervisorId);
                    }
                    supervisor.setName(supervisorInfo2.getFirstName() + " " + supervisorInfo2.getLastName());
                    signature = userRestClient.getEmployeeSignature(supervisorId);
                    supervisor.setSignature(signature);
                    form.setSupervisorNext(supervisor);
            }

            //String apbt_uch = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_CA_VALIDATION);
            signature = userRestClient.getEmployeeSignature(owner);
            form.setSignature(signature);
            supervisor = new VacationForm.Signatory();

            if(supervisorId != null){
                    EmployeeInfo supervisorInfo2 =  userRestClient.getStaffByUsername(supervisorId);
                    log.info(supervisorId);
                    if(supervisorInfo2 == null){
                            throw new BadRequestException("DG not found " + supervisorId);
                    }
                    supervisor.setName(supervisorInfo2.getFirstName() + " " + supervisorInfo2.getLastName());
                    signature = userRestClient.getEmployeeSignature(supervisorId);
                    supervisor.setSignature(signature);
                    form.setSupervisorNext(supervisor);
            }



            VacationDecision decision = new VacationDecision();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate currentDate = LocalDate.now();
            currentDate.format(formatter);

            decision.setReference(currentDate.format(formatter) + "/DG/DGA/DAF/RCH/DAAS/CORH/" + request.getId());

            decision.setDate(LocalDate.now());
            decision.setFunction(Optional.ofNullable(staff.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
            decision.setEmployee(staff.getFirstName() + " " + staff.getLastName());
            decision.setMatricule(staff.getMatricule());
            decision.setStartDate(startDate);
            decision.setEndDate(repriseDate);
            decision.setPeriod(WorkDayCalculator.getDateRangeAsString(lastVacationDate, startDate));
            decision.setUnity(staff.getDepartment().getName());

            Long allocationDueLong = (Long) delegateExecution.getVariable("allocationDue");
            Integer allocationDue = allocationDueLong.intValue();
            decision.setAllocation(allocationDue);
            decision.setPrincipalVacation(allocationDue);

            Long daysLong = (Long) delegateExecution.getVariable("days");
            Integer days = daysLong.intValue();
            decision.setProvidedVacation(days);


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

            Long consumedVacationLong = (Long) delegateExecution.getVariable("consumedVacation");
            Integer consumedVacation = consumedVacationLong.intValue();
            decision.setConsumedVacation(consumedVacation);

            String complementaryString = (String) delegateExecution.getVariable("complementary");
            decision.setComplementary(Boolean.valueOf(complementaryString));

            String respCA = (String) delegateExecution.getVariable("Apbt_ca_validation");
            EmployeeInfo respCAInfo = userRestClient.getStaffByUsername(respCA);
            signature = userRestClient.getEmployeeSignature(respCA);



            VacationDecision.Signatory signatory = new VacationDecision.Signatory();
            signatory.setSignature(signature);
            signatory.setDate(LocalDate.now());
            signatory.setName(respCAInfo.getFirstName() + " " + respCAInfo.getLastName());
            decision.setSignatory(signatory);

            String dg = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_DG);
            if(dg != null){
                    EmployeeInfo dgInfo = userRestClient.getStaffByUsername(dg);
                    signature = userRestClient.getEmployeeSignature(dg);
                    signatory = new VacationDecision.Signatory();
                    signatory.setSignature(signature);
                    signatory.setDate(LocalDate.now());
                    signatory.setFunction("Le Directeur Général Adjoint");
                    signatory.setName(dgInfo.getFirstName() + " " + dgInfo.getLastName());
                    decision.setSignatory(signatory);
            }

            String function = (String) delegateExecution.getVariable("function");
            decision.setFunction(function);

            decision.setNumber(request.getReference());

            ByteArrayResource decisionVacation = reportingRestClient.vacationDecision(decision);


            ByteArrayResource resource = reportingRestClient.vacation(form);

            EmailAskApprovalDto ask = new EmailAskApprovalDto();
            ask.setSender(staff.getUsername());
            ask.setSubject("Demande de Congés Validées");
            ask.setBCC(supervisorInfo.getEmail() + "," + processUnityService.getEmailUnity(EMAIL_CAPITAL_HUMAIN) + "," + processUnityService.getEmailUnity(EMAIL_HABILITATION));
            AttachmentDto attachment = new AttachmentDto();
            attachment.setName("demande_congés" + delegateExecution.getBusinessKey() + ".pdf");
            attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
            AttachmentDto attachmentDecision = new AttachmentDto();
            attachmentDecision.setName("decision_congés.pdf");
            attachmentDecision.setData(Base64.getEncoder().encodeToString(decisionVacation.getByteArray()));
            ask.setAttachments(new AttachmentDto[]{attachment, attachmentDecision});
            ask.setMessage("Votre document a été généré avec succès bien vouloir prendre connaissance");
            emailService.sendFiles(ask);

            CustomMultipartFile multipartFile = new CustomMultipartFile(resource.getByteArray(), attachment.getName(), "application/pdf");

            FileDto fileDto = new FileDto();
            fileDto.setAddDate(LocalDateTime.now());
            fileDto.setName("Demande de Congés Validées");
            fileDto.setFile(Base64.getEncoder().encodeToString(resource.getByteArray()));
            fileDto.setMultipartFile(multipartFile);
            fileDto.setType("application/pdf");
            fileService.saveFile(request, fileDto);

            multipartFile = new CustomMultipartFile(decisionVacation.getByteArray(), attachment.getName(), "application/pdf");

            fileDto = new FileDto();
            fileDto.setAddDate(LocalDateTime.now());
            fileDto.setName("Decision de Congés");
            fileDto.setFile(Base64.getEncoder().encodeToString(decisionVacation.getByteArray()));
            fileDto.setMultipartFile(multipartFile);
            fileDto.setType("application/pdf");
            fileService.saveFile(request, fileDto);

            /*EmailDto emailDto = new EmailDto();
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
            this.emailRestClient.send(emailDto);*/


    }
}
