package com.ccabank.paperless.process.vacation.implementation;

import com.ccabank.paperless.dto.email.AttachmentDto;
import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.dto.reporting.HandOverForm;
import com.ccabank.paperless.dto.user.EmployeeFunctionInfo;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.dto.user.FunctionInfo;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.openfeign.ReportingRestClient;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.EmailService;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.util.CustomMultipartFile;
import com.ccabank.paperless.util.WorkDayCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendHandOver implements JavaDelegate {

    @Autowired
    private final ReportingRestClient reportingRestClient;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final UserRestClient userRestClient;

    @Autowired
    private final FileService fileService;

    @Autowired
    private final RequestRepository requestRepository;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {


        log.info("Send Hand Over");

            //Générer le HandOver
            HandOverForm handOverForm = new HandOverForm();

            HandOverForm.Employee employee = new HandOverForm.Employee();
            employee.setDate(LocalDate.now());

            String owner = (String) delegateExecution.getVariable("owner");

            EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);
            employee.setName(staff.getFirstName() + " " + staff.getLastName());
            String function = (String) delegateExecution.getVariable("function");
            employee.setFunction(function);

            String signature = userRestClient.getEmployeeSignature(staff.getUsername());
            employee.setSignature(signature);
            handOverForm.setEmployee(employee);


            log.info("Je suis au niveau des dates");
            Date startDateD = (Date) delegateExecution.getVariable("realStartDate");
            LocalDate startDate = startDateD.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            handOverForm.setStartDate(startDate);
            log.info(String.valueOf(startDate));


            Long daysLong =  (Long) delegateExecution.getVariable("days") ;
            Integer days = daysLong.intValue();
            log.info(String.valueOf(days));


            LocalDate endDate = WorkDayCalculator.addBusinessDays(startDate, days);
            log.info(String.valueOf(endDate));

            delegateExecution.setVariable("endDate", endDate);

            handOverForm.setEndDate(endDate);

            HandOverForm.Employee interim = new HandOverForm.Employee();
            interim.setDate(LocalDate.now());

            String interimId = (String) delegateExecution.getVariable("Apbt_interimaire");
            EmployeeInfo interimaire =  userRestClient.getStaffByUsername(interimId);


            interim.setName(interimaire.getFirstName() + " " + interimaire.getLastName());
            interim.setFunction(Optional.ofNullable(interimaire.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
            signature = userRestClient.getEmployeeSignature(interimaire.getUsername());
            interim.setSignature(signature);

            handOverForm.setInterim(interim);


            HandOverForm.Employee supervisorModel = new HandOverForm.Employee();
            supervisorModel.setDate(LocalDate.now());

            String supervisorId = (String) delegateExecution.getVariable("Apbt_n1");
            EmployeeInfo supervisor =  userRestClient.getStaffByUsername(supervisorId);

            supervisorModel.setName(supervisor.getFirstName() + " " + supervisor.getLastName());
            supervisorModel.setFunction(Optional.ofNullable(supervisor.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));

            signature = userRestClient.getEmployeeSignature(supervisor.getUsername());
            supervisorModel.setSignature(signature);
            handOverForm.setSupervisor(supervisorModel);


            String criticFolder = (String) delegateExecution.getVariable("criticFolder");

            handOverForm.setActivities(criticFolder);
            String mainWork = (String) delegateExecution.getVariable("mainWork");
            handOverForm.setResponsibilities(mainWork);

            log.info(handOverForm.toString());


            //Envoyer le HandOver Par Email à l'intérimaire
            ByteArrayResource resource = this.reportingRestClient.handover(handOverForm);

            EmailAskApprovalDto ask = new EmailAskApprovalDto();
            ask.setSender(interimaire.getUsername());
            ask.setSubject("Formulaire de Hand Over");
            ask.setBCC(staff.getEmail());
            AttachmentDto attachment = new AttachmentDto();
            attachment.setName("handover_" + delegateExecution.getBusinessKey() + ".pdf");
            attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
            ask.setAttachments(new AttachmentDto[]{attachment});
            ask.setMessage("Votre document a été généré avec succès bien vouloir prendre connaissance");
            emailService.sendFiles(ask);

            CustomMultipartFile multipartFile = new CustomMultipartFile(resource.getByteArray(), attachment.getName(), "application/pdf");

            FileDto fileDto = new FileDto();
            fileDto.setAddDate(LocalDateTime.now());
            fileDto.setName("Hand Over");
            fileDto.setFile(Base64.getEncoder().encodeToString(resource.getByteArray()));
            fileDto.setMultipartFile(multipartFile);
            fileDto.setType("application/pdf");
            Request request = requestRepository.findByInstanceId(delegateExecution.getProcessInstanceId());
            fileService.saveFile(request, fileDto);

            /*Request request = requestRepository.findByInstanceId(delegateExecution.getProcessInstanceId());

            EmailDto emailDto = new EmailDto();
            emailDto.setFrom("notification@cca-bank.com");
            emailDto.setTo(interimaire.getEmail());
            emailDto.setSubject("Formulaire de Hand Over");
            emailDto.setCc(staff.getEmail());
            emailDto.setBody("En pièce jointe le formulaire de HandOver");
            AttachmentDto attachment = new AttachmentDto();
            attachment.setName("handover_" + request.getReference() + ".pdf");
            attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
            emailDto.setAttachments(new AttachmentDto[]{attachment});
            this.emailRestClient.send(emailDto);*/

    }
}
