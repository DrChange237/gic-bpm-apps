package com.ccabank.memoservice.process.mission.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.entity.AgencyInfo;
import com.ccabank.memoservice.dto.reporting.MissionForm;
import com.ccabank.memoservice.dto.user.AgencyDto;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.process.general.service.RequestService;
import com.ccabank.memoservice.process.mission.constant.TransportCommonConstant;
import com.ccabank.memoservice.util.DateUtil;
import com.ccabank.memoservice.util.WorkDayCalculator;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SendMissionOrder implements JavaDelegate {
    private final ReportingRestClient reportingRestClient;
    private final EmailRestClient emailRestClient;
    private final UserRestClient userRestClient;
    private final RequestService requestService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {


        MissionForm missionForm = new MissionForm();
        String owner = (String) delegateExecution.getVariable("owner");
        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);
        String signature = userRestClient.getEmployeeSignature(owner);
        missionForm.setRequesterSignature(signature);
        missionForm.setSignature(signature);


        missionForm.setName(staff.getFirstName() + " " + staff.getLastName());
        missionForm.setDate(LocalDate.now());

        String object = (String) delegateExecution.getVariable("object");
        missionForm.setObject(object);

        String accountNumber = (String) delegateExecution.getVariable("accountNumber");
        missionForm.setAccountNumber(accountNumber);
        LocalDate startDate = (LocalDate) delegateExecution.getVariable("startDate");

        Long missionFeesLong = (Long) delegateExecution.getVariable("missionFees");

        missionForm.setStartDate(startDate);

        Long nbDays = (Long) delegateExecution.getVariable("nbDays");

        missionForm.setMissionFees(missionFeesLong.doubleValue() * nbDays.doubleValue());


        LocalDate endDate = WorkDayCalculator.addBusinessDays(startDate, nbDays.intValue());
        missionForm.setEndDate(endDate);
        missionForm.setFunction(staff.getFunction().getFunction().getName());
        missionForm.setUnity(staff.getDepartment().getName());
        String location = (String) delegateExecution.getVariable("location");
        missionForm.setLocation(location);
        missionForm.setPlace("DOUALA");


        //int nightsLong = WorkDayCalculator.calculateNights(startDate, endDate);
        missionForm.setNights(nbDays.intValue());



        Long transportFees = (Long) delegateExecution.getVariable("transportFees");
        missionForm.setTransportFees(transportFees.doubleValue());


        String authorisationNumber = (String) delegateExecution.getVariable("authorisationNumber");
        missionForm.setAuthorisationNumber(authorisationNumber);

        String receiptNumber = (String) delegateExecution.getVariable("receiptNumber");
        missionForm.setReceiptNumber(receiptNumber);

        missionForm.setChargeSupport(Optional.ofNullable(staff.getAgency()).map(AgencyDto::getName).orElse(""));

        String apbt_n1 = (String) delegateExecution.getVariable("Apbt_n1");
        EmployeeInfo n1 =  userRestClient.getStaffByUsername(apbt_n1);
        MissionForm.Signatory supervisor = new MissionForm.Signatory();
        supervisor.setDate(LocalDate.now());
        supervisor.setName(n1.getFirstName() + " " + n1.getLastName());
        supervisor.setFunction(n1.getFunction().getFunction().getName());
        signature = userRestClient.getEmployeeSignature(apbt_n1);
        supervisor.setSignature(signature);
        missionForm.setSupervisor(supervisor);

        String apbt_n2 = (String) delegateExecution.getVariable("Apbt_n2");
        EmployeeInfo n2 =  userRestClient.getStaffByUsername(apbt_n2);
        MissionForm.Signatory supervisor2 = new MissionForm.Signatory();
        supervisor2.setDate(LocalDate.now());
        supervisor2.setName(n2.getFirstName() + " " + n1.getLastName());
        supervisor2.setFunction(n2.getFunction().getFunction().getName());
        signature = userRestClient.getEmployeeSignature(apbt_n2);
        supervisor2.setSignature(signature);
        missionForm.setSupervisorNext(supervisor2);


        String apbt_uch = (String) delegateExecution.getVariable("Apbt_rh");


        EmployeeInfo n_uch =  userRestClient.getStaffByUsername(apbt_n2);
        MissionForm.Signatory uch = new MissionForm.Signatory();
        uch.setDate(LocalDate.now());
        uch.setName(n_uch.getFirstName() + " " + n_uch.getLastName());
        uch.setFunction(n_uch.getFunction().getFunction().getName());
        signature = userRestClient.getEmployeeSignature(apbt_uch);
        uch.setSignature(signature);
        missionForm.setUch(uch);



        MissionForm.Transport transport  = new MissionForm.Transport();
        String transportMoyen = (String) delegateExecution.getVariable("transport");
        transport.setCommon(false);
        if(transportMoyen.equals(TransportCommonConstant.COMMON_TRANSPORT)){
            transport.setCommon(true);
        }
        String coursier = (String) delegateExecution.getVariable("coursier");
        transport.setCourier(coursier);
        String immatriculation = (String) delegateExecution.getVariable("immatriculation");
        transport.setImmatriculation(immatriculation);
        missionForm.setTransport(transport);


        //Envoyer le HandOver Par Email à l'intérimaire
        ByteArrayResource resource = this.reportingRestClient.mission(missionForm);

        EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(staff.getEmail());
        emailDto.setSubject("Ordre de Mission");
        emailDto.setCc(n1.getEmail());
        emailDto.setBody("Ordre de Mission");
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("ordre_mission.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);

        requestService.confirmRequest(delegateExecution.getProcessInstanceId());


    }
}
