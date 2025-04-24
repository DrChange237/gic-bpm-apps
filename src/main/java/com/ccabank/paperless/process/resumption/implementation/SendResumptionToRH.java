package com.ccabank.paperless.process.resumption.implementation;

import com.ccabank.paperless.dto.email.AttachmentDto;
import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.dto.reporting.ResumptionForm;
import com.ccabank.paperless.dto.user.EmployeeFunctionInfo;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.dto.user.FunctionInfo;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.openfeign.ReportingRestClient;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.process.general.constant.EmailGroup;
import com.ccabank.paperless.process.general.service.RequestService;
import com.ccabank.paperless.service.faces.CamundaService;
import com.ccabank.paperless.service.faces.EmailService;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.util.CustomMultipartFile;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class SendResumptionToRH implements JavaDelegate {

    @Autowired
    private  UserRestClient userRestClient;

    @Autowired
    private  ReportingRestClient reportingRestClient;

    @Autowired
    private EmailService emailService;

    @Autowired
    private  RequestService requestService;

    @Autowired
    private CamundaService camundaService;

    @Autowired
    private FileService fileService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {


            Request request = requestService.confirmRequest(delegateExecution.getProcessInstanceId());

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
            System.out.println("Apbt_n1 :" + supervisorUser);
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

            EmailAskApprovalDto ask = new EmailAskApprovalDto();
            ask.setSender(staff.getUsername());
            ask.setSubject("Fiche de Reprise de Service");
            ask.setbCC(emailList + "," + EmailGroup.EMAIL_HABILITATION + "," + EmailGroup.EMAIL_CAPITAL_HUMAIN);
            AttachmentDto attachment = new AttachmentDto();
            attachment.setName("reprise_service" + delegateExecution.getBusinessKey() + ".pdf");
            attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
            ask.setAttachments(new AttachmentDto[]{attachment});
            emailService.sendFiles(ask);

            CustomMultipartFile multipartFile = new CustomMultipartFile(resource.getByteArray(), attachment.getName(), "application/pdf");

            FileDto fileDto = new FileDto();
            fileDto.setAddDate(LocalDateTime.now());
            fileDto.setName("Fiche de reprise de service");
            fileDto.setFile(Base64.getEncoder().encodeToString(resource.getByteArray()));
            fileDto.setMultipartFile(multipartFile);
            fileDto.setType("application/pdf");
            fileService.saveFile(request, fileDto);

            /*EmailDto emailDto = new EmailDto();
            emailDto.setFrom("notification@cca-bank.com");
            emailDto.setTo(staff.getEmail());
            emailDto.setSubject("Fiche de Reprise de Service");
            emailDto.setCc(emailList);
            emailDto.setBody("En pièce jointe la fiche de reprise de service");
            AttachmentDto attachment = new AttachmentDto();
            attachment.setName("reprise_service.pdf");
            attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
            emailDto.setAttachments(new AttachmentDto[]{attachment});
            this.emailRestClient.send(emailDto);*/


    }
}
