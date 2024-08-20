package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.reporting.*;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Absence;
import com.ccabank.memoservice.entity.documenttype.Memo;
import com.ccabank.memoservice.entity.documenttype.OrdreMission;
import com.ccabank.memoservice.entity.documenttype.Vacation;
import com.ccabank.memoservice.entity.documenttype.sub.Settlement;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.*;
import com.ccabank.memoservice.service.faces.MapToReportService;
import com.ccabank.memoservice.service.faces.PurchaseService;
import com.ccabank.memoservice.util.field.FieldUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;
import static com.ccabank.memoservice.constant.DocumentTypeConstant.*;


@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class MapToReportServiceImpl implements MapToReportService {

    private static final Logger logger = LoggerFactory.getLogger(MapToReportServiceImpl.class);


    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private ReportingRestClient reportingRestClient;

    @Autowired
    private OrdreMissionRepository ordreMissionRepository;

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private ResumptionRepository resumptionRepository;

    @Autowired
    private AbsenceRepository absenceRepository;

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private WorkFormRepository workFormRepository;

    @Autowired
    private PurchaseService purchaseService;

    @Override
    public ByteArrayResource reportRequest(Request request){

        switch (request.getType().getStructure()){

            case DOCUMENT_TYPE_VACATION :

                VacationForm vacationForm = this.constructVacationRequest(request);

                logger.info("Received : {}", vacationForm);

                try {

                    ByteArrayResource response = this.reportingRestClient.vacation(vacationForm);

                    return response;

                    //return response.body().asInputStream();
                } catch (Exception e) {
                    throw new RuntimeException("Error downloading PDF file: vacation", e);
                }

            case DOCUMENT_TYPE_ABSENSE:

                AbsenceForm absenceForm = this.constructAbsenceRequest(request);

               // AbsenceForm absenceForm = new AbsenceForm();

                logger.info("Received : {}", absenceForm);

                try {

                    ByteArrayResource responseAbsence = this.reportingRestClient.absence(absenceForm);

                    return responseAbsence;

                    //return responseAbsence.body().asInputStream();
                } catch (Exception e) {
                    logger.error("Error downloading PDF file: absence", e);

                }

                break;


            case DOCUMENT_TYPE_MISSION:

                MissionForm missionForm = this.constructMissionRequest(request);

                logger.info("Received : {}", missionForm);

                try {

                    logger.info("Mission Form : " + missionForm.toString() , missionForm.toString());


                    ByteArrayResource responseMission = this.reportingRestClient.mission(missionForm);

                    return responseMission;

                } catch (Exception e) {

                    logger.error("Error downloading PDF file: mission", e);

                }

                break;

            case DOCUMENT_TYPE_RESUMPTION:

                ResumptionForm resumptionForm = this.constructResumptionRequest(request);

                logger.info("Received : {}", resumptionForm);

                try {
                    ByteArrayResource responseResumption = this.reportingRestClient.resumption(resumptionForm);

                    return  responseResumption;

                   // return responseResumption.body().asInputStream();
                } catch (Exception e) {

                    logger.error("Error downloading PDF file: mission", e);

                }

                break;

            case DOCUMENT_TYPE_MEMO:

                MemoForm memoForm = this.constructMemoRequest(request);
                logger.info("Received : {}", memoForm);
                try {
                    ByteArrayResource responseMemo = this.reportingRestClient.memo(memoForm);

                    return  responseMemo;

                    // return responseResumption.body().asInputStream();
                } catch (Exception e) {

                    logger.error("Error downloading PDF file: mission", e);

                }

                break;

            case DOCUMENT_TYPE_WORKFORM:

                WorkForm workForm = this.constructWorkRequest(request);
                logger.info("Received : {}", workForm);
                try {
                    ByteArrayResource responseWork = this.reportingRestClient.workform(workForm);

                    return  responseWork;

                    // return responseResumption.body().asInputStream();
                } catch (Exception e) {

                    logger.error("Error downloading PDF file: mission", e);

                }

                break;

            case DOCUMENT_TYPE_PURCHASE:

                PurchaseForm purchaseForm = this.purchaseService.construct(request);
                logger.info("Received : {}", purchaseForm);
                try {
                    ByteArrayResource responseWork = this.reportingRestClient.purchase(purchaseForm);
                    return  responseWork;
                    // return responseResumption.body().asInputStream();
                } catch (Exception e) {

                    logger.error("Error downloading PDF file: mission", e);

                }

                break;

        }

        return  null;
    }

    public MemoForm constructMemoRequest(Request request){

        System.out.println("Document ID : " + request.getDocumentId());

        Memo memo = memoRepository.getOne(request.getDocumentId());

        MemoForm memoForm = new MemoForm();

        memoForm.setDate(memo.getDate());

        memoForm.setSender(memo.getRequester().getUnity());

        memoForm.setReceiver(memo.getReceiver());

        memoForm.setMaterial(memo.getMaterial());

        memoForm.setNumber(request.getReference());

        memoForm.setBody(memo.getBody());

        memoForm.setSubject(memo.getSubject());

        List<MemoForm.Signatory> signatories = new ArrayList<>();

        for(Signatory signatory : memo.getSignatories()){
            MemoForm.Signatory signatory1 = new MemoForm.Signatory();
            signatory1.setName(signatory.getOwner().getName());
            signatory1.setDate(memo.getDate());
            String signature = userRestClient.getEmployeeSignature(signatory.getOwner().getUsername());
            signatory1.setSignature(signature);
            signatories.add(signatory1);
        }

        memoForm.setSignatories(signatories);


        return  memoForm;
    }

    @Override
    public AbsenceForm constructAbsenceRequest(Request request){

        System.out.println("Document ID : " + request.getDocumentId());


        Absence absence = absenceRepository.getOne(request.getDocumentId());

        System.out.println("1");
        AbsenceForm absenceForm = new AbsenceForm();



        System.out.println("Date");
        absenceForm.setDate(absence.getDate());

        absenceForm.setEndDate(absence.getEndDate());
        absenceForm.setStartDate(absence.getStartDate());


        System.out.println("Function");
        absenceForm.setFunction(absence.getRequester().getFunction());

        absenceForm.setMatricule(absence.getRequester().getMatricule());


        System.out.println("Name");
        absenceForm.setName(absence.getRequester().getName());

        System.out.println("Unity");
        absenceForm.setUnity(absence.getRequester().getUnity());

        System.out.println("Place");
        absenceForm.setPlace(absence.getPlace());

        String signature = userRestClient.getEmployeeSignature(absence.getRequester().getUsername());
        //String signature = this.getFictifSignature();
        absenceForm.setSignature(signature);

        System.out.println("Rights");
        AbsenceForm.Settlement settlement = new AbsenceForm.Settlement();
        settlement.setPaid(Optional.of(absence).map(Absence::getRights).map(Settlement::getPaid).orElse(0.0));
        settlement.setUnpaid(Optional.of(absence).map(Absence::getRights).map(Settlement::getUnpaid).orElse(0.0));
        absenceForm.setRights(settlement);

        System.out.println("Stock");
        settlement = new AbsenceForm.Settlement();
        settlement.setPaid(Optional.of(absence).map(Absence::getStock).map(Settlement::getPaid).orElse(0.0));
        settlement.setUnpaid(Optional.of(absence).map(Absence::getStock).map(Settlement::getUnpaid).orElse(0.0));
        absenceForm.setRights(settlement);

        System.out.println("Absence");
        settlement = new AbsenceForm.Settlement();
        settlement.setPaid(Optional.of(absence).map(Absence::getAbsence).map(Settlement::getPaid).orElse(0.0));
        settlement.setUnpaid(Optional.of(absence).map(Absence::getAbsence).map(Settlement::getUnpaid).orElse(0.0));
        absenceForm.setRights(settlement);

        System.out.println("Vacation");
        settlement = new AbsenceForm.Settlement();
        settlement.setPaid(Optional.of(absence).map(Absence::getVacation).map(Settlement::getPaid).orElse(0.0));
        settlement.setUnpaid(Optional.of(absence).map(Absence::getVacation).map(Settlement::getUnpaid).orElse(0.0));
        absenceForm.setRights(settlement);

        System.out.println("Salary");
        settlement = new AbsenceForm.Settlement();
        settlement.setPaid(Optional.of(absence).map(Absence::getSalary).map(Settlement::getPaid).orElse(0.0));
        settlement.setUnpaid(Optional.of(absence).map(Absence::getSalary).map(Settlement::getUnpaid).orElse(0.0));
        absenceForm.setRights(settlement);

        System.out.println("Advice");
        settlement = new AbsenceForm.Settlement();
        settlement.setPaid(Optional.of(absence).map(Absence::getAdvice).map(Settlement::getPaid).orElse(0.0));
        settlement.setUnpaid(Optional.of(absence).map(Absence::getAdvice).map(Settlement::getUnpaid).orElse(0.0));
        absenceForm.setRights(settlement);


        AbsenceForm.Signatory signatory = new AbsenceForm.Signatory();

        signatory.setName(absence.getSupervisor().getOwner().getName());
        signature = userRestClient.getEmployeeSignature(absence.getSupervisor().getOwner().getUsername());
        signatory.setSignature(signature);
        absenceForm.setSignatory1(signatory);

        AbsenceForm.Signatory signatory2 = new AbsenceForm.Signatory();


        signatory2.setName(absence.getSupervisor2().getOwner().getName());
        signature = userRestClient.getEmployeeSignature(absence.getSupervisor2().getOwner().getUsername());
        signatory2.setSignature(signature);
        absenceForm.setSignatory2(signatory2);

        AbsenceForm.Signatory signatory3 = new AbsenceForm.Signatory();


        signatory3.setName(absence.getHeadOffice().getOwner().getName());
        signature = userRestClient.getEmployeeSignature(absence.getHeadOffice().getOwner().getUsername());
        signatory3.setSignature(signature);
        absenceForm.setHeadOffice(signatory3);


        absenceForm.setInterim(absence.getInterim().getName());



        System.out.println("Days");
        absenceForm.setDays(absence.getDays());

        System.out.println("Deduction");
        AbsenceForm.Deduction deduction = AbsenceForm.Deduction.valueOf(absence.getDeduction().name());
        absenceForm.setDeduction(deduction);

        System.out.println("Reason");
        absenceForm.setReason(StringUtils.defaultString(absence.getReason()));

        System.out.println(absenceForm);


        return absenceForm;
    }

    @Override
    public MissionForm constructMissionRequest(Request request) {


        OrdreMission ordreMission = ordreMissionRepository.getOne(request.getDocumentId());

        MissionForm missionForm = new MissionForm();
        missionForm.setDate(ordreMission.getDate());

        missionForm.setFunction(ordreMission.getRequester().getFunction());
        missionForm.setName(ordreMission.getRequester().getName());
        missionForm.setUnity(ordreMission.getRequester().getUnity());
        missionForm.setPlace(ordreMission.getPlace());

        missionForm.setSignature(ordreMission.getOwner().getSignature());

        //Object
        missionForm.setObject(ordreMission.getObject());

        //Location
        missionForm.setLocation(ordreMission.getLocation());

        //StartDate
        missionForm.setStartDate(ordreMission.getStartDate());

        //EndDate
        missionForm.setEndDate(ordreMission.getEndDate());

        //nights
        missionForm.setNights(ordreMission.getNights());

        //Transport
        MissionForm.Transport transport = new MissionForm.Transport();
        transport.setCommon(ordreMission.getTransport().getCommon());

        //Coursier
        transport.setCourier(ordreMission.getTransport().getCoursier());

        //Immatriculation
        transport.setImmatriculation(ordreMission.getTransport().getImmatriculation());

        missionForm.setTransport(transport);

        //AccountNumber
        transport.setImmatriculation(ordreMission.getAccountNumber());

        //Supervisor
        MissionForm.Signatory signatory = new MissionForm.Signatory();

        signatory.setName(ordreMission.getSupervisor().getOwner().getName());
        signatory.setSignature(ordreMission.getSupervisor().getSignature());
        missionForm.setSupervisor(signatory);

        logger.info("Get Supervisor Staff Ok");

        //SupervisorNext
        signatory = new MissionForm.Signatory();
        signatory.setName(ordreMission.getSupervisorNext().getOwner().getName());
        signatory.setSignature(ordreMission.getSupervisorNext().getSignature());
        missionForm.setSupervisorNext(signatory);

        logger.info("Get Supervisor Next Staff Ok");

        //UCH
        signatory = new MissionForm.Signatory();
        signatory.setName(ordreMission.getUch().getOwner().getName());
        signatory.setSignature(ordreMission.getUch().getSignature());
        //signatory.setSignature(getFictifSignature());
        missionForm.setUch(signatory);
        missionForm.setRequesterSignature(ordreMission.getUch().getSignature());
        logger.info("Get Supervisor UCH Ok");

        //decision
        missionForm.setDecision(ordreMission.getDecision());

        //chargeSupport
        missionForm.setChargeSupport(ordreMission.getChargeSupport());

        //missionFees
        missionForm.setMissionFees(ordreMission.getMissionFees());

        //transportFees
        missionForm.setTransportFees(ordreMission.getTransportFees());

        //authorisationNumber
        missionForm.setAuthorisationNumber(ordreMission.getAuthorisationNumber());

        //receiptNumber
        missionForm.setReceiptNumber(ordreMission.getReceiptNumber());

        logger.info("Complete Map");

        return missionForm;
    }

    @Override
    public ResumptionForm constructResumptionRequest(Request request){

        ResumptionForm resumptionForm = new ResumptionForm();
        resumptionForm.setDate(LocalDate.now());

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        resumptionForm.setFunction(staff.getFunction());
        resumptionForm.setName(staff.getName());
        resumptionForm.setMatricule(staff.getMatricule());
        resumptionForm.setUnity(staff.getDepartment());

        String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        resumptionForm.setSignature(signature);


        //StartDate
        String startDate = FieldUtils.getValueOfField(request,"startDate");
        if(startDate != null){
            resumptionForm.setStartDate(LocalDate.parse(startDate));
        }


        //realEndDate
        String realEndDate = FieldUtils.getValueOfField(request,"realEndDate");
        if(realEndDate != null){
            resumptionForm.setRealEndDate(LocalDate.parse(realEndDate));
        }

        //EndDate
        String endDate = FieldUtils.getValueOfField(request,"endDate");
        if(endDate != null){
            resumptionForm.setEndDate(LocalDate.parse(endDate));
        }

        //Reason
        String reason = FieldUtils.getValueOfField(request,"reason");
        if(reason != null){
            resumptionForm.setReason(ResumptionForm.Reason.valueOf(reason));
        }


        //Supervisor
        ResumptionForm.Signatory signatory = new ResumptionForm.Signatory();
        String supervisor = request.getApprovalByPosition(1).getStaff();
        if(supervisor != null){
            staff = userRestClient.getAgencyByStaffUsername(supervisor, "key", "secret");
            signatory.setName(staff.getUsername());
            signatory.setSignature(userRestClient.getEmployeeSignature(staff.getUsername()));
            resumptionForm.setSupervisor(signatory);
        }


        return resumptionForm;

    }


    public  VacationForm constructVacationRequest(Request request){

        Vacation vacation = vacationRepository.getOne(request.getDocumentId());
        VacationForm vacationForm = new VacationForm();
        vacationForm.setDate(vacation.getDate());

        vacationForm.setFunction(vacation.getRequester().getFunction());
        vacationForm.setName(vacation.getRequester().getName());
        vacationForm.setMatricule(vacation.getRequester().getMatricule());
        vacationForm.setUnity(vacation.getRequester().getUnity());


        String signature = userRestClient.getEmployeeSignature(vacation.getRequester().getUsername());
        vacationForm.setSignature(signature);



        //StartDate
        vacationForm.setStartDate(vacation.getStartDate());

        //EndDate
        vacationForm.setEndDate(vacation.getEndDate());

        // Interim

        VacationForm.Interim interim = new VacationForm.Interim();
        interim.setName(vacation.getInterim().getName());
        interim.setFunction(vacation.getInterim().getFunction());

        vacationForm.setInterim(interim);

        //Supervisor



        //Supervisor
        VacationForm.Signatory signatory = new VacationForm.Signatory();
        signatory.setName(vacation.getSupervisor().getOwner().getName());
        signature = userRestClient.getEmployeeSignature(vacation.getSupervisor().getOwner().getUsername());
        signatory.setSignature(signature);
        vacationForm.setSupervisor(signatory);


        //SupervisorNext
        signatory = new VacationForm.Signatory();
        signatory.setName(vacation.getSupervisor().getOwner().getName());
        signature = userRestClient.getEmployeeSignature(vacation.getSupervisor2().getOwner().getUsername());
        signatory.setSignature(signature);
        vacationForm.setSupervisorNext(signatory);


        return vacationForm;

    }


    public  WorkForm constructWorkRequest(Request request){

        com.ccabank.memoservice.entity.documenttype.WorkForm workForm = workFormRepository.getOne(request.getDocumentId());
        WorkForm workForm1 = new WorkForm();
        workForm1.setDate(workForm.getDate());


        workForm1.setUnity(workForm.getRequester().getUnity());


        String signature = userRestClient.getEmployeeSignature(workForm.getRequester().getUsername());
       // workForm1.setSignature(signature);


        //Supervisor
        WorkForm.Signatory signatory = new WorkForm.Signatory();
        signatory.setName(workForm.getSupervisor().getOwner().getName());
        signature = userRestClient.getEmployeeSignature(workForm.getSupervisor().getOwner().getUsername());
        signatory.setSignature(signature);
        workForm1.setSupervisor(signatory);


        //SupervisorNext
        signatory = new WorkForm.Signatory();
        signatory.setName(workForm.getHead().getOwner().getName());
        signature = userRestClient.getEmployeeSignature(workForm.getHead().getOwner().getUsername());
        signatory.setSignature(signature);
        workForm1.setDepartment(signatory);

        //SupervisorNext
        signatory = new WorkForm.Signatory();
        signatory.setName(workForm.getHead().getOwner().getName());
        signature = userRestClient.getEmployeeSignature(workForm.getAccountant().getOwner().getUsername());
        signatory.setSignature(signature);
        workForm1.setAccountant(signatory);


        return workForm1;

    }


}
