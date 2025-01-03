package com.ccabank.memoservice.process.vacation.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.InterimForm;
import com.ccabank.memoservice.dto.user.EmployeeFunctionInfo;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.dto.user.FunctionInfo;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.user.Gender;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.process.general.constant.IncidentTypeConstant;
import com.ccabank.memoservice.repository.RequestRepository;
import com.ccabank.memoservice.service.faces.CamundaService;
import com.ccabank.memoservice.util.DateUtil;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class SendInterimLetter implements JavaDelegate {

    @Autowired
    private ReportingRestClient reportingRestClient;

    @Autowired
    private EmailRestClient emailRestClient;

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private CamundaService camundaService;

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        try{

            System.out.println("Sending interim letter");

            InterimForm form = new InterimForm();

            String interimId = (String) delegateExecution.getVariable("Apbt_interimaire");
            EmployeeInfo interimaire =  userRestClient.getStaffByUsername(interimId);

            InterimForm.Employee interim = new InterimForm.Employee();
            interim.setMatricule(interimaire.getMatricule());
            interim.setName(interimaire.getFirstName() + " " + interimaire.getLastName());
            interim.setFunction(Optional.ofNullable(interimaire.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
            interim.setSex(InterimForm.Employee.Sex.MALE);
            if(interimaire.getGender().equals(Gender.FEMALE)){
                interim.setSex(InterimForm.Employee.Sex.FEMALE);
            }
            form.setInterim(interim);

            String owner = (String) delegateExecution.getVariable("Apbt_interimaire");
            EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);

            InterimForm.Employee employee = new InterimForm.Employee();
            employee.setMatricule(staff.getMatricule());
            employee.setName(staff.getFirstName() + " " + staff.getLastName());
            employee.setFunction(Optional.ofNullable(staff.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
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
            form.setNoteId("NOTE 2024 N° 2970/DGA/DAF/RCH/DAAS/CORH");

            Date startDateD = (Date) delegateExecution.getVariable("realStartDate");
            LocalDate startDate = startDateD.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            Date endDateD = (Date) delegateExecution.getVariable("endDate") ;
            LocalDate endDate = endDateD.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            form.setStartDate(startDate);
            form.setEndDate(endDate);


            String signature = userRestClient.getEmployeeSignature(interimaire.getUsername());
            form.setCachet(signature);


            System.out.println(form);

            ByteArrayResource resource = reportingRestClient.interim(form);

            Request request = requestRepository.findByInstanceId(delegateExecution.getProcessInstanceId());

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
            this.emailRestClient.send(emailDto);

        }catch (Exception e){
            camundaService.createIncident(delegateExecution.getProcessInstanceId(), IncidentTypeConstant.TECHNICAL, "Letter Interim Generation " + e.getMessage() );
        }

    }
}
