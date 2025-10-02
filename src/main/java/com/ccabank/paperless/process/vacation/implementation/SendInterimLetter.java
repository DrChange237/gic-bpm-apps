package com.ccabank.paperless.process.vacation.implementation;

import com.ccabank.paperless.dto.email.AttachmentDto;
import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.dto.reporting.InterimForm;
import com.ccabank.paperless.dto.reporting.VacationDecision;
import com.ccabank.paperless.dto.user.EmployeeFunctionInfo;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.dto.user.FunctionInfo;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.entity.user.Gender;
import com.ccabank.paperless.openfeign.ReportingRestClient;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.process.general.constant.ApprobationLevel;
import com.ccabank.paperless.process.vacation.constant.CumulConstant;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.EmailService;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.util.CustomMultipartFile;
import lombok.RequiredArgsConstructor;
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
import java.util.Random;

@Component
@RequiredArgsConstructor
public class SendInterimLetter implements JavaDelegate {

    @Autowired
    private  ReportingRestClient reportingRestClient;

    @Autowired
    private  UserRestClient userRestClient;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private FileService fileService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {


            System.out.println("Sending interim letter");

            InterimForm form = new InterimForm();

            String interimId = (String) delegateExecution.getVariable("Apbt_interimaire");
            EmployeeInfo interimaire =  userRestClient.getStaffByUsername(interimId);

            InterimForm.Employee interim = new InterimForm.Employee();
            interim.setMatricule(interimaire.getMatricule());
            interim.setName(interimaire.getFirstName() + " " + interimaire.getLastName());
            String function = interimaire.getFunction().getFunction().getName();
            interim.setFunction(function);
            interim.setSex(InterimForm.Employee.Sex.MALE);
            if(interimaire.getGender().equals(Gender.FEMALE)){
                interim.setSex(InterimForm.Employee.Sex.FEMALE);
            }
            form.setInterim(interim);

            String owner = (String) delegateExecution.getVariable("owner");
            EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);

            InterimForm.Employee employee = new InterimForm.Employee();
            employee.setMatricule(staff.getMatricule());
            employee.setName(staff.getFirstName() + " " + staff.getLastName());
            employee.setFunction((String) delegateExecution.getVariable("function"));
            employee.setSex(InterimForm.Employee.Sex.MALE);
            if(staff.getGender().equals(Gender.FEMALE)){
                employee.setSex(InterimForm.Employee.Sex.FEMALE);
            }
            form.setEmployee(employee);

            form.setDate(LocalDate.now());

            // Numero du bas
            form.setNumber("XXX");

            String typeInterim = (String) delegateExecution.getVariable("typeInterim");

            System.out.println(typeInterim);

            form.setSubject(InterimForm.Subject.valueOf(typeInterim));


            //Note à Generer
            int currentYear = LocalDate.now().getYear();
            int currentMonth = LocalDate.now().getMonthValue();
            Random random = new Random();
            int rnd = random.nextInt(1000);
            String number = String.valueOf(currentMonth) + String.valueOf(rnd);

            form.setNoteId("NOTE "+currentYear+" N° " + number + "/DGA/DAF/RCH/DAAS/CORH");

            Date startDateD = (Date) delegateExecution.getVariable("realStartDate");
            LocalDate startDate = startDateD.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalDate endDate = (LocalDate) delegateExecution.getVariable("endDate") ;

            form.setStartDate(startDate);
            form.setEndDate(endDate);


            EmployeeInfo info = userRestClient.getStaffByUsername(interimaire.getUsername());
            String signature = userRestClient.getEmployeeSignature(interimaire.getUsername());

            InterimForm.Signatory signatory = new InterimForm.Signatory();
            signatory.setSignature(signature);
            signatory.setDate(LocalDate.now());
            signatory.setFunction("Le Directeur du Capital Humain");
            signatory.setName(info.getFirstName() + " " + info.getLastName());
            form.setSignatory(signatory);
            String dg = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_DG);
            EmployeeInfo dgInfo = userRestClient.getStaffByUsername(dg);
            signature = userRestClient.getEmployeeSignature(dg);
            if(dg != null){
                signatory.setSignature(signature);
                signatory.setDate(LocalDate.now());
                signatory.setFunction("Le Directeur Général Adjoint");
                signatory.setName(dgInfo.getFirstName() + " " + dgInfo.getLastName());
                form.setSignatory(signatory);
            }

             String cumul = (String) delegateExecution.getVariable("cummulatif") ;

             if(cumul != null){
                 form.setCumulate(true);
                 if(cumul.equals(CumulConstant.NON_CUMUL)){
                     form.setCumulate(false);
                 }
             }


            System.out.println(form);

            ByteArrayResource resource = reportingRestClient.interim(form);

            EmailAskApprovalDto ask = new EmailAskApprovalDto();
            ask.setSender(interimaire.getUsername());
            ask.setSubject("Lettre d'intérim");
            ask.setBCC(staff.getEmail());
            AttachmentDto attachment = new AttachmentDto();
            attachment.setName("lettre_interim_" + delegateExecution.getBusinessKey() + ".pdf");
            attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
            ask.setAttachments(new AttachmentDto[]{attachment});
            ask.setMessage("Votre document a été généré avec succès bien vouloir prendre connaissance");
            emailService.sendFiles(ask);

            CustomMultipartFile multipartFile = new CustomMultipartFile(resource.getByteArray(), attachment.getName(), "application/pdf");

            FileDto fileDto = new FileDto();
            fileDto.setAddDate(LocalDateTime.now());
            fileDto.setName("Lettre d'intérim");
            fileDto.setFile(Base64.getEncoder().encodeToString(resource.getByteArray()));
            fileDto.setMultipartFile(multipartFile);
            fileDto.setType("application/pdf");
            Request request = requestRepository.findByInstanceId(delegateExecution.getProcessInstanceId());
            fileService.saveFile(request, fileDto);

            /*Request request = requestRepository.findByInstanceId(delegateExecution.getProcessInstanceId());

            EmailDto emailDto = new EmailDto();
            emailDto.setFrom("notification@cca-bank.com");
            emailDto.setTo(interimaire.getEmail());
            emailDto.setSubject("Lettre d'intérim");
            emailDto.setCc(staff.getEmail());
            emailDto.setBody("Lettre d'intérim");

            AttachmentDto attachment = new AttachmentDto();
            attachment.setName("lettre_interim_" + request.getReference() + ".pdf");
            attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
            emailDto.setAttachments(new AttachmentDto[]{attachment});
            this.emailRestClient.send(emailDto);*/

    }
}
