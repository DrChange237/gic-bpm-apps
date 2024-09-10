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
import com.ccabank.memoservice.service.faces.*;
import com.ccabank.memoservice.util.field.FieldUtils;
import com.ccabank.memoservice.util.file.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;
import static com.ccabank.memoservice.constant.DocumentTypeConstant.*;


@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class MapToReportServiceImpl implements MapToReportService {

    private static final Logger logger = LoggerFactory.getLogger(MapToReportServiceImpl.class);


    @Autowired
    private ReportingRestClient reportingRestClient;


    @Autowired
    private VacationService vacationService;


    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ResumptionService resumptionService;

    @Autowired
    private MemoService memoService;

    @Autowired
    private AbsenceService absenceService;

    @Autowired
    private WorkformService workformService;

    @Autowired
    private OrdreMissionService ordreMissionService;

    @Autowired
    private HandOverService handOverService;


    @Override
    public ByteArrayResource reportRequest(Request request){

        switch (request.getType().getStructure()){

            //  Type 1 : Demande de congés
            case DOCUMENT_TYPE_VACATION :

                VacationForm vacationForm = this.vacationService.construct(request);

                HandOverForm handOverForm = this.handOverService.construct(request);

                VacationFullForm vacationFullForm = new VacationFullForm();

                vacationFullForm.setVacation(vacationForm);

                vacationFullForm.setHandover(handOverForm);

                logger.info("Received : {}", vacationFullForm);

                try {

                    ByteArrayResource response = this.reportingRestClient.vacation(vacationFullForm);

                    return response;

                    //return response.body().asInputStream();
                } catch (Exception e) {
                    throw new RuntimeException("Error downloading PDF file: vacation", e);
                }

                //  Type 2 : Demande d'absence
            case DOCUMENT_TYPE_ABSENSE:

                AbsenceForm absenceForm = this.absenceService.construct(request);

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

            //  Type 3 : Ordre de Mission
            case DOCUMENT_TYPE_MISSION:

                MissionForm missionForm = this.ordreMissionService.construct(request);

                logger.info("Received : {}", missionForm);

                try {

                    logger.info("Mission Form : " + missionForm.toString() , missionForm.toString());


                    ByteArrayResource responseMission = this.reportingRestClient.mission(missionForm);

                    return responseMission;

                } catch (Exception e) {

                    logger.error("Error downloading PDF file: mission", e);

                }

                break;

            //  Type 4 : Reprise de Service
            case DOCUMENT_TYPE_RESUMPTION:

                ResumptionForm resumptionForm = this.resumptionService.construct(request);

                logger.info("Received : {}", resumptionForm);

                try {
                    ByteArrayResource responseResumption = this.reportingRestClient.resumption(resumptionForm);

                    return  responseResumption;

                   // return responseResumption.body().asInputStream();
                } catch (Exception e) {

                    logger.error("Error downloading PDF file: mission", e);

                }

                break;

            //  Type 5 : Mémo
            case DOCUMENT_TYPE_MEMO:

                MemoForm memoForm = this.memoService.construct(request);
                logger.info("Received : {}", memoForm.toString());


                try {
                    ByteArrayResource responseMemo = this.reportingRestClient.memo(memoForm);

                    return  responseMemo;

                    // return responseResumption.body().asInputStream();
                } catch (Exception e) {

                    logger.error("Error downloading PDF file: mission", e);

                }

                break;

            //  Type 6 : Demande de travail
            case DOCUMENT_TYPE_WORKFORM:

                WorkForm workForm = this.workformService.construct(request);
                logger.info("Received : {}", workForm);
                try {
                    ByteArrayResource responseWork = this.reportingRestClient.workform(workForm);

                    return  responseWork;

                    // return responseResumption.body().asInputStream();
                } catch (Exception e) {

                    logger.error("Error downloading PDF file: mission", e);

                }

                break;

            //  Type 7 : Demande d'achat
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

}
