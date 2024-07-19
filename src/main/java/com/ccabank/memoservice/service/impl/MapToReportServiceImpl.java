package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.reporting.AbsenceForm;
import com.ccabank.memoservice.dto.reporting.MissionForm;
import com.ccabank.memoservice.dto.reporting.ResumptionForm;
import com.ccabank.memoservice.dto.reporting.VacationForm;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.openfeign.ReportingRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.service.faces.MapToReportService;
import com.ccabank.memoservice.util.field.FieldUtils;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDate;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;
import static com.ccabank.memoservice.constant.DocumentTypeConstant.*;


@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class MapToReportServiceImpl implements MapToReportService {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalServiceImpl.class);


    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private ReportingRestClient reportingRestClient;

    @Override
    public InputStream reportRequest(Request request){

        switch (request.getType().getStructure()){

            case DOCUMENT_TYPE_VACATION :

                VacationForm vacationForm = this.constructVacationRequest(request);

                logger.info("Received : {}", vacationForm);

                Response response = this.reportingRestClient.vacation(vacationForm);
                try {
                    return response.body().asInputStream();
                } catch (Exception e) {
                    throw new RuntimeException("Error downloading PDF file: vacation", e);
                }

            case DOCUMENT_TYPE_ABSENSE:

                AbsenceForm absenceForm = new AbsenceForm();

                logger.info("Received : {}", absenceForm);

                Response responseAbsence = this.reportingRestClient.absence(absenceForm);
                try {
                    return responseAbsence.body().asInputStream();
                } catch (Exception e) {
                    throw new RuntimeException("Error downloading PDF file: absence", e);
                }


            case DOCUMENT_TYPE_MISSION:

                MissionForm missionForm = this.constructMissionRequest(request);

                logger.info("Received : {}", missionForm);

                Response responseMission = this.reportingRestClient.mission(missionForm);
                try {
                    return responseMission.body().asInputStream();
                } catch (Exception e) {
                    throw new RuntimeException("Error downloading PDF file: mission", e);
                }

            case DOCUMENT_TYPE_RESUMPTION:

                ResumptionForm resumptionForm = this.constructResumptionRequest(request);

                logger.info("Received : {}", resumptionForm);

                Response responseResumption = this.reportingRestClient.resumption(resumptionForm);
                try {
                    return responseResumption.body().asInputStream();
                } catch (Exception e) {
                    throw new RuntimeException("Error downloading PDF file: resumption", e);
                }


        }

        return  null;
    }

    @Override
    public MissionForm constructMissionRequest(Request request){

        MissionForm missionForm = new MissionForm();
        missionForm.setDate(LocalDate.from(request.getCreatedAt()));

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        missionForm.setFunction(staff.getFunction());
        missionForm.setName(staff.getUsername());
        missionForm.setUnity(staff.getDepartment());

        String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        missionForm.setSignature(signature);


        //StartDate
        String startDate = FieldUtils.getValueOfField(request,"startDate");
        if(startDate != null){
            missionForm.setStartDate(LocalDate.parse(startDate));
        }


        //EndDate
        String endDate = FieldUtils.getValueOfField(request,"endDate");
        if(endDate != null){
            missionForm.setEndDate(LocalDate.parse(endDate));
        }


        //Supervisor
        MissionForm.Signatory signatory = new MissionForm.Signatory();
        String supervisor = request.getApprovalByPosition(1).getStaff();
        if(supervisor != null){
            staff = userRestClient.getAgencyByStaffUsername(supervisor, "key", "secret");
            signatory.setName(staff.getUsername());
            signatory.setSignature(userRestClient.getEmployeeSignature(staff.getUsername()));
            missionForm.setSupervisor(signatory);

        }

        //SupervisorNext
        signatory = new MissionForm.Signatory();
        supervisor = request.getApprovalByPosition(2).getStaff();
        if(supervisor != null){
            staff = userRestClient.getAgencyByStaffUsername(supervisor, "key", "secret");
            signatory.setName(staff.getUsername());
            signatory.setSignature(userRestClient.getEmployeeSignature(staff.getUsername()));
            missionForm.setSupervisorNext(signatory);
        }


        return missionForm;

    }

    @Override
    public ResumptionForm constructResumptionRequest(Request request){

        ResumptionForm resumptionForm = new ResumptionForm();
        resumptionForm.setDate(LocalDate.from(request.getCreatedAt()));

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

        VacationForm vacationForm = new VacationForm();
        vacationForm.setDate(LocalDate.from(request.getCreatedAt()));

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        vacationForm.setFunction(staff.getFunction());
        vacationForm.setName(staff.getUsername());
        vacationForm.setMatricule(staff.getMatricule());
        vacationForm.setUnity(staff.getDepartment());

        String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        vacationForm.setSignature(signature);

        //LastVacationDate
        String lastVacationDateString = FieldUtils.getValueOfField(request,"lastVacationDate");

        if(lastVacationDateString != null){
            vacationForm.setLastVacationDate(LocalDate.parse(lastVacationDateString));
        }

        //StartDate
        lastVacationDateString = FieldUtils.getValueOfField(request,"startDate");
        if(lastVacationDateString != null){
            vacationForm.setStartDate(LocalDate.parse(lastVacationDateString));
        }


        //EndDate
        lastVacationDateString = FieldUtils.getValueOfField(request,"endDate");
        if(lastVacationDateString != null){
            vacationForm.setEndDate(LocalDate.parse(lastVacationDateString));
        }



        // Interim
        lastVacationDateString = FieldUtils.getValueOfField(request,"interim");
        staff = userRestClient.getAgencyByStaffUsername(lastVacationDateString, "key", "secret");

        VacationForm.Interim interim = new VacationForm.Interim();
        interim.setName(staff.getUsername());
        interim.setFunction(staff.getFunction());

        vacationForm.setInterim(interim);

        //Supervisor
        VacationForm.Signatory signatory = new VacationForm.Signatory();
        String supervisor = request.getApprovalByPosition(1).getStaff();
        if(supervisor != null){
            staff = userRestClient.getAgencyByStaffUsername(supervisor, "key", "secret");
            signatory.setName(staff.getUsername());
            signatory.setSignature(userRestClient.getEmployeeSignature(staff.getUsername()));
            vacationForm.setSupervisor(signatory);
        }


        //SupervisorNext
        signatory = new VacationForm.Signatory();
        supervisor = request.getApprovalByPosition(2).getStaff();
        if(supervisor != null){
            staff = userRestClient.getAgencyByStaffUsername(supervisor, "key", "secret");
            signatory.setName(staff.getUsername());
            signatory.setSignature(userRestClient.getEmployeeSignature(staff.getUsername()));
            vacationForm.setSupervisorNext(signatory);
        }

        return vacationForm;

    }
}
