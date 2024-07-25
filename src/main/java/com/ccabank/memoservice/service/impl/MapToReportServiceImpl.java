package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.reporting.AbsenceForm;
import com.ccabank.memoservice.dto.reporting.MissionForm;
import com.ccabank.memoservice.dto.reporting.ResumptionForm;
import com.ccabank.memoservice.dto.reporting.VacationForm;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.OrdreMission;
import com.ccabank.memoservice.entity.documenttype.Vacation;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.AbsenceRepository;
import com.ccabank.memoservice.repository.OrdreMissionRepository;
import com.ccabank.memoservice.repository.ResumptionRepository;
import com.ccabank.memoservice.repository.VacationRepository;
import com.ccabank.memoservice.service.faces.MapToReportService;
import com.ccabank.memoservice.service.faces.VacationService;
import com.ccabank.memoservice.util.field.FieldUtils;
import com.ccabank.memoservice.util.file.FileReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
    private  FileReader fileReader;

    @Autowired
    private OrdreMissionRepository ordreMissionRepository;

    @Autowired
    private VacationRepository vacationRepository;

    private ResumptionRepository resumptionRepository;

    private AbsenceRepository absenceRepository;

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

                logger.info("Received : {}", absenceForm);

                try {

                    ByteArrayResource responseAbsence = this.reportingRestClient.absence(absenceForm);

                    return responseAbsence;

                    //return responseAbsence.body().asInputStream();
                } catch (Exception e) {
                    logger.error("Error downloading PDF file: mission", e);

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


        }

        return  null;
    }

    @Override
    public AbsenceForm constructAbsenceRequest(Request request){

        AbsenceForm absenceForm = new AbsenceForm();
        absenceForm.setDate(LocalDate.now());

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        absenceForm.setFunction(staff.getFunction());
        absenceForm.setName(staff.getUsername());
        absenceForm.setUnity(staff.getDepartment());
        absenceForm.setPlace(staff.getAgencyName());

        String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        //String signature = this.getFictifSignature();
        absenceForm.setSignature(signature);


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
        resumptionForm.setName(staff.getUsername());
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

        vacationForm.setSignature(vacation.getOwner().getSignature());



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
        signatory.setSignature(vacation.getSupervisor().getSignature());
        vacationForm.setSupervisor(signatory);


        //SupervisorNext
        signatory = new VacationForm.Signatory();
        signatory.setName(vacation.getSupervisor().getOwner().getName());
        signatory.setSignature(vacation.getSupervisor().getSignature());
        vacationForm.setSupervisorNext(signatory);


        return vacationForm;

    }

    String getFictifSignature(){

        return fileReader.readDataFromFile();
    }
}
