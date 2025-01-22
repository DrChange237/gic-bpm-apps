package com.ccabank.memoservice.process.mission.implementation;

import com.ccabank.memoservice.dto.email.AttachmentDto;
import com.ccabank.memoservice.dto.email.EmailAskApprovalDto;
import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.entity.AgencyInfo;
import com.ccabank.memoservice.dto.memo.FileDto;
import com.ccabank.memoservice.dto.reporting.MissionForm;
import com.ccabank.memoservice.dto.user.AgencyDto;
import com.ccabank.memoservice.dto.user.EmployeeFunctionInfo;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.dto.user.FunctionInfo;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.process.general.constant.ApprobationLevel;
import com.ccabank.memoservice.process.general.service.RequestService;
import com.ccabank.memoservice.process.mission.constant.TransportCommonConstant;
import com.ccabank.memoservice.repository.RequestRepository;
import com.ccabank.memoservice.service.faces.EmailService;
import com.ccabank.memoservice.service.faces.FileService;
import com.ccabank.memoservice.util.CustomMultipartFile;
import com.ccabank.memoservice.util.DateUtil;
import com.ccabank.memoservice.util.WorkDayCalculator;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.cert.ocsp.Req;
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

@Component
@RequiredArgsConstructor
public class SendMissionOrder implements JavaDelegate {

    @Autowired
    private final ReportingRestClient reportingRestClient;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final UserRestClient userRestClient;

    @Autowired
    private final RequestService requestService;

    @Autowired
    private final FileService fileService;

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {


        MissionForm missionForm = new MissionForm();
        String owner = (String) delegateExecution.getVariable("owner");
        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);
        String signature = userRestClient.getEmployeeSignature(owner);
        missionForm.setSignature(signature);


        missionForm.setName(staff.getFirstName() + " " + staff.getLastName());
        missionForm.setDate(LocalDate.now());

        String object = (String) delegateExecution.getVariable("object");
        missionForm.setObject(object);

        String accountNumber = (String) delegateExecution.getVariable("accountNumber");
        missionForm.setAccountNumber(accountNumber);
        Date startDateD = (Date) delegateExecution.getVariable("startDate");
        LocalDate startDate = startDateD.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Long missionFeesLong = (Long) delegateExecution.getVariable("missionFees");

        missionForm.setStartDate(startDate);

        Long nbDays = (Long) delegateExecution.getVariable("nbDays");

        missionForm.setMissionFees(missionFeesLong.doubleValue() * nbDays.intValue());

        LocalDate endDate = WorkDayCalculator.addBusinessDays(startDate, nbDays.intValue());
        missionForm.setEndDate(endDate);
        missionForm.setFunction(Optional.ofNullable(staff.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
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

        String supportCharge = (String) delegateExecution.getVariable("supportCharge");
        missionForm.setChargeSupport(supportCharge);

        String apbt_n1 = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_N1);
        EmployeeInfo n1 =  userRestClient.getStaffByUsername(apbt_n1);
        MissionForm.Signatory supervisor = new MissionForm.Signatory();
        supervisor.setDate(LocalDate.now());
        supervisor.setName(n1.getFirstName() + " " + n1.getLastName());
        supervisor.setFunction(Optional.ofNullable(n1.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
        signature = userRestClient.getEmployeeSignature(apbt_n1);
        supervisor.setSignature(signature);
        missionForm.setSupervisor(supervisor);

        String apbt_n2 = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_N2);

        EmployeeInfo n2 =  userRestClient.getStaffByUsername(apbt_n2);
        MissionForm.Signatory supervisor2 = new MissionForm.Signatory();
        supervisor2.setDate(LocalDate.now());
        supervisor2.setName(n2.getFirstName() + " " + n1.getLastName());
        supervisor2.setFunction(Optional.ofNullable(n2.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
        signature = userRestClient.getEmployeeSignature(apbt_n2);
        supervisor2.setSignature(signature);
        missionForm.setSupervisorNext(supervisor2);


        String apbt_uch = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_CA_VALIDATION);


        EmployeeInfo n_uch =  userRestClient.getStaffByUsername(apbt_uch);
        MissionForm.Signatory uch = new MissionForm.Signatory();
        uch.setDate(LocalDate.now());
        uch.setName(n_uch.getFirstName() + " " + n_uch.getLastName());
        uch.setFunction(Optional.ofNullable(n_uch.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
        signature = userRestClient.getEmployeeSignature(apbt_uch);
        uch.setSignature(signature);
        missionForm.setUch(uch);

        String apbt_daf = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_DAF);

        EmployeeInfo n_daf =  userRestClient.getStaffByUsername(apbt_daf);
        MissionForm.Signatory daf = new MissionForm.Signatory();
        daf.setDate(LocalDate.now());
        daf.setName(n_daf.getFirstName() + " " + n_daf.getLastName());
        daf.setFunction(Optional.ofNullable(n_daf.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
        signature = userRestClient.getEmployeeSignature(apbt_daf);
        daf.setSignature(signature);
        missionForm.setRequester(daf);




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

        EmailAskApprovalDto ask = new EmailAskApprovalDto();
        ask.setSender(staff.getUsername());
        ask.setSubject("Ordre de Mission");
        ask.setbCC(n1.getEmail() + "," + n2.getEmail() + "," + n_uch.getEmail());
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("ordre_mission" + delegateExecution.getBusinessKey() + ".pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        ask.setAttachments(new AttachmentDto[]{attachment});
        emailService.sendFiles(ask);

        CustomMultipartFile multipartFile = new CustomMultipartFile(resource.getByteArray(), attachment.getName(), "application/pdf");
        FileDto fileDto = new FileDto();
        fileDto.setAddDate(LocalDateTime.now());
        fileDto.setName("Ordre de Mission");
        fileDto.setFile(Base64.getEncoder().encodeToString(resource.getByteArray()));
        fileDto.setMultipartFile(multipartFile);
        fileDto.setType("application/pdf");
        Request request = requestService.confirmRequest(delegateExecution.getProcessInstanceId());
        fileService.saveFile(request, fileDto);

        /*EmailDto emailDto = new EmailDto();
        emailDto.setFrom("notification@cca-bank.com");
        emailDto.setTo(staff.getEmail());
        emailDto.setSubject("Ordre de Mission");
        emailDto.setCc(n1.getEmail());
        emailDto.setBody("Ordre de Mission");
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("ordre_mission.pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        emailDto.setAttachments(new AttachmentDto[]{attachment});
        this.emailRestClient.send(emailDto);*/



    }
}
