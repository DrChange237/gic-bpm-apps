package com.ccabank.paperless.process.mission.implementation;

import com.ccabank.paperless.dto.email.AttachmentDto;
import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.dto.reporting.MissionForm;
import com.ccabank.paperless.dto.user.EmployeeFunctionInfo;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.dto.user.FunctionInfo;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.openfeign.ReportingRestClient;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.process.general.constant.ApprobationLevel;
import com.ccabank.paperless.process.general.constant.EmailGroup;
import com.ccabank.paperless.process.general.service.RequestService;
import com.ccabank.paperless.process.mission.constant.TransportCommonConstant;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.EmailService;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.service.faces.ProcessUnityService;
import com.ccabank.paperless.util.CustomMultipartFile;
import com.ccabank.paperless.util.WorkDayCalculator;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


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
    private final ProcessUnityService processUnityService;

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {


        MissionForm missionForm = new MissionForm();
        List<MissionForm.Signatory> signatoryList = new ArrayList<>();
        String owner = (String) delegateExecution.getVariable("owner");
        EmployeeInfo staff =  userRestClient.getStaffByUsername(owner);
        String signature = "";

        try{
            signature = userRestClient.getEmployeeSignature(owner);
            missionForm.setSignature(signature);
        }catch (Exception e){
            throw new BadRequestException("La signature de l'initiateur n'est pas disponible");
        }

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
        if(nbDays == 0){
            missionForm.setMissionFees(missionFeesLong.doubleValue());
        }

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
        try{
            signature = userRestClient.getEmployeeSignature(apbt_n1);
        }catch (Exception e){
            throw new BadRequestException("La signature de lemployé " + apbt_n1 + " n'est pas disponible");
        }
        supervisor.setSignature(signature);
        missionForm.setSupervisor(supervisor);

        String apbt_n2 = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_N2);


        if(apbt_n2 != null){

            EmployeeInfo n2 =  userRestClient.getStaffByUsername(apbt_n2);
            MissionForm.Signatory supervisor2 = new MissionForm.Signatory();
            supervisor2.setDate(LocalDate.now());
            supervisor2.setName(n2.getFirstName() + " " + n2.getLastName());
            supervisor2.setFunction(Optional.ofNullable(n2.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
            try{
                signature = userRestClient.getEmployeeSignature(apbt_n2);
            }catch (Exception e){
                throw new BadRequestException("La signature de lemployé " + apbt_n2 + " n'est pas disponible");
            }
            supervisor2.setSignature(signature);
            missionForm.setSupervisorNext(supervisor2);
        }

        String apbt_dg = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_DG);

        if(apbt_dg != null){
            EmployeeInfo dg =  userRestClient.getStaffByUsername(apbt_dg);
            MissionForm.Signatory headOffice = new MissionForm.Signatory();
            headOffice.setDate(LocalDate.now());
            headOffice.setName(dg.getFirstName() + " " + dg.getLastName());
            headOffice.setFunction(Optional.ofNullable(dg.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
            try{
                signature = userRestClient.getEmployeeSignature(apbt_dg);
            }catch (Exception e){
                throw new BadRequestException("La signature de lemployé " + apbt_dg + " n'est pas disponible");
            }
            headOffice.setSignature(signature);
            missionForm.setHeadOffice(headOffice);
        }

        String apbt_uch = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_CA_VALIDATION);
        EmployeeInfo n_uch =  userRestClient.getStaffByUsername(apbt_uch);
        MissionForm.Signatory uch = new MissionForm.Signatory();
        uch.setDate(LocalDate.now());
        uch.setName(n_uch.getFirstName() + " " + n_uch.getLastName());
        uch.setFunction(Optional.ofNullable(n_uch.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
        try{
            signature = userRestClient.getEmployeeSignature(apbt_uch);
        }catch (Exception e){
            throw new BadRequestException("La signature de lemployé " + apbt_uch + " n'est pas disponible");
        }
        uch.setSignature(signature);
        missionForm.setUch(uch);

        String apbt_daf = "";


        try{
            apbt_daf = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_DAF);
        }catch (Exception e){
            throw new BadRequestException("La signature de lemployé " + apbt_daf + " n'est pas disponible");
        }

        EmployeeInfo n_daf =  userRestClient.getStaffByUsername(apbt_daf);
        MissionForm.Signatory daf = new MissionForm.Signatory();
        daf.setDate(LocalDate.now());
        daf.setName(n_daf.getFirstName() + " " + n_daf.getLastName());
        daf.setFunction(Optional.ofNullable(n_daf.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
        signature = userRestClient.getEmployeeSignature(apbt_daf);
        daf.setSignature(signature);
        missionForm.setRequester(daf);

        String apbt_direction = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_DIRECTOR);
        if(apbt_direction != null){
            EmployeeInfo signer =  userRestClient.getStaffByUsername(apbt_direction);
            MissionForm.Signatory signatory = new MissionForm.Signatory();
            signatory.setDate(LocalDate.now());
            signatory.setName(signer.getFirstName() + " " + signer.getLastName());
            signatory.setFunction(Optional.ofNullable(signer.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
            try{
                signature = userRestClient.getEmployeeSignature(apbt_direction);
            }catch (Exception e){
                throw new BadRequestException("La signature de lemployé " + apbt_direction + " n'est pas disponible");
            }
            signatory.setSignature(signature);
            signatoryList.add(signatory);
        }

        String apbt_dcr = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_DCR);
        if(apbt_dcr != null){
            EmployeeInfo signer =  userRestClient.getStaffByUsername(apbt_dcr);
            MissionForm.Signatory signatory = new MissionForm.Signatory();
            signatory.setDate(LocalDate.now());
            signatory.setName(signer.getFirstName() + " " + signer.getLastName());
            signatory.setFunction(Optional.ofNullable(signer.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
            try{
                signature = userRestClient.getEmployeeSignature(apbt_dcr);
            }catch (Exception e){
                throw new BadRequestException("La signature de lemployé " + apbt_dcr + " n'est pas disponible");
            }
            signatory.setSignature(signature);
            signatoryList.add(signatory);
        }

        String apbt_dcs = (String) delegateExecution.getVariable(ApprobationLevel.APPROBATION_DCS);
        if(apbt_dcs != null){
            EmployeeInfo signer =  userRestClient.getStaffByUsername(apbt_dcs);
            MissionForm.Signatory signatory = new MissionForm.Signatory();
            signatory.setDate(LocalDate.now());
            signatory.setName(signer.getFirstName() + " " + signer.getLastName());
            signatory.setFunction(Optional.ofNullable(signer.getFunction()).map(EmployeeFunctionInfo::getFunction).map(FunctionInfo::getName).orElse(null));
            try{
                signature = userRestClient.getEmployeeSignature(apbt_dcs);
            }catch (Exception e){
                throw new BadRequestException("La signature de lemployé " + apbt_dcs + " n'est pas disponible");
            }
            signatory.setSignature(signature);
            signatoryList.add(signatory);
        }

        signatoryList.add(daf);

        missionForm.setSignatories(signatoryList);





        MissionForm.Transport transport  = new MissionForm.Transport();
        String transportMoyen = (String) delegateExecution.getVariable("transport");
        transport.setCommon(false);
        if(transportMoyen.equals("COMMUN")){
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
        ask.setBCC(n1.getEmail() + "," + processUnityService.getEmailUnity(EmailGroup.EMAIL_CAPITAL_HUMAIN));
        AttachmentDto attachment = new AttachmentDto();
        attachment.setName("ordre_mission-" + delegateExecution.getBusinessKey() + ".pdf");
        attachment.setData(Base64.getEncoder().encodeToString(resource.getByteArray()));
        ask.setAttachments(new AttachmentDto[]{attachment});
        ask.setMessage("Votre document a été généré avec succès bien vouloir prendre connaissance");
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
