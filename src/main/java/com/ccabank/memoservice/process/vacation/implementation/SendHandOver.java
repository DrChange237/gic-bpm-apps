package com.ccabank.memoservice.process.vacation.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.HandOverForm;
import com.ccabank.memoservice.dto.user.EmployeeFunctionInfo;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.dto.user.FunctionInfo;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.process.general.constant.IncidentTypeConstant;
import com.ccabank.memoservice.repository.RequestRepository;
import com.ccabank.memoservice.service.faces.CamundaService;
import com.ccabank.memoservice.util.WorkDayCalculator;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;

import java.util.Date;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class SendHandOver implements JavaDelegate {

    private final ReportingRestClient reportingRestClient;
    private final EmailRestClient emailRestClient;
    private final UserRestClient userRestClient;
    private final CamundaService camundaService;

    private RequestRepository requestRepository;

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
            employee.setFunction(Optional.ofNullable(staff.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));

            String signature = userRestClient.getEmployeeSignature(staff.getUsername());
            employee.setSignature(signature);
            handOverForm.setEmployee(employee);


            System.out.println("Je suis au niveau des dates");
            Date startDateD = (Date) delegateExecution.getVariable("realStartDate");
            LocalDate startDate = startDateD.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            handOverForm.setStartDate(startDate);
            System.out.println(startDate);


            Long daysLong =  (Long) delegateExecution.getVariable("days") ;
            Integer days = daysLong.intValue();
            System.out.println(days);


            LocalDate endDate = WorkDayCalculator.addBusinessDays(startDate, days);
            System.out.println(endDate);

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

            System.out.println(handOverForm.toString());


            //Envoyer le HandOver Par Email à l'intérimaire
            ByteArrayResource resource = this.reportingRestClient.handover(handOverForm);

            Request request = requestRepository.findByInstanceId(delegateExecution.getProcessInstanceId());

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
            this.emailRestClient.send(emailDto);

    }
}
