package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.reporting.MissionForm;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.OrdreMission;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import com.ccabank.memoservice.entity.documenttype.sub.Transport;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.OrdreMissionRepository;
import com.ccabank.memoservice.repository.SignatoryRepository;
import com.ccabank.memoservice.repository.StaffRepository;
import com.ccabank.memoservice.repository.TransportRepository;
import com.ccabank.memoservice.service.faces.OrdreMissionService;
import com.ccabank.memoservice.service.faces.SignatoryService;
import com.ccabank.memoservice.util.WorkDayCalculator;
import com.ccabank.memoservice.util.field.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class OrdreMissionServiceImpl implements OrdreMissionService {

    @Autowired
    private OrdreMissionRepository ordreMissionRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private SignatoryRepository signatoryRepository;

    @Autowired
    private UserRestClient userRestClient;

    private static final Logger logger = LoggerFactory.getLogger(SaveDocumentServiceImpl.class);

    @Autowired
    private SignatoryService signatoryService;


    @Override
    public OrdreMission save(Request request){
        OrdreMission ordreMission = new OrdreMission();

        ordreMission.setDate(LocalDate.now());

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");



        Staff requester = new Staff();
        requester.setFunction(staff.getFunction());
        requester.setName(staff.getName());
        requester.setUnity(staff.getDepartment());
        requester.setMatricule(staff.getMatricule());

        requester = staffRepository.save(requester);

        ordreMission.setRequester(requester);

        ordreMission.setPlace(staff.getAgencyName());

        Signatory owner = new Signatory();
        owner.setOwner(requester);
        String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        owner.setSignature(signature);

        owner = signatoryRepository.save(owner);

        ordreMission.setOwner(owner);


        //Object
        String object = FieldUtils.getValueOfField(request,"object");
        ordreMission.setObject(object);

        logger.info("Get Field Staff Ok");


        //Location
        String location = FieldUtils.getValueOfField(request,"location");
        ordreMission.setLocation(location);


        //StartDate
        String startDate = FieldUtils.getValueOfField(request,"startDate");
        ordreMission.setStartDate(LocalDate.parse(startDate));


        //EndDate
        String endDate = FieldUtils.getValueOfField(request,"endDate");
        ordreMission.setEndDate(LocalDate.parse(endDate));


        //nights
        ordreMission.setNights(WorkDayCalculator.calculateNights(LocalDate.parse(startDate), LocalDate.parse(endDate)));


        //Transport
        Transport transport = new Transport();
        transport.setCommon(true);


        //Coursier
        String coursier = FieldUtils.getValueOfField(request,"coursier");
        transport.setCoursier(coursier);


        //Immatriculation
        String immatriculation = FieldUtils.getValueOfField(request,"immatriculation");
        transport.setImmatriculation(immatriculation);

        transport = transportRepository.save(transport);

        ordreMission.setTransport(transport);

        //AccountNumber
        String accountNumber = FieldUtils.getValueOfField(request,"accountNumber");
        ordreMission.setAccountNumber(accountNumber);


        //Supervisor
        String username = request.getApprovalByPosition(1).getStaff();
        Signatory supervisor = signatoryService.getSignatory(username);
        ordreMission.setSupervisor(supervisor);


        //SupervisorNext
        username = request.getApprovalByPosition(2).getStaff();
        supervisor = signatoryService.getSignatory(username);
        ordreMission.setSupervisorNext(supervisor);


        //UCH
        username = request.getApprovalByPosition(4).getStaff();
        supervisor = signatoryService.getSignatory(username);
        ordreMission.setUch(supervisor);

        ordreMission.setOrderGiven(supervisor);


        //decision
        String decision = FieldUtils.getValueOfField(request,"avis");
        ordreMission.setDecision(decision);


        //chargeSupport
        String chargeSupport = FieldUtils.getValueOfField(request,"chargeSupport");
        ordreMission.setChargeSupport(Double.valueOf(chargeSupport));



        //missionFees
        String missionFees = FieldUtils.getValueOfField(request,"missionFees");
        ordreMission.setMissionFees(Double.valueOf(missionFees));


        //transportFees
        String transportFees = FieldUtils.getValueOfField(request,"transportFees");
        ordreMission.setTransportFees(Double.valueOf(transportFees));


        //authorisationNumber
        String authorisationNumber = FieldUtils.getValueOfField(request,"authorisationNumber");
        ordreMission.setAuthorisationNumber(authorisationNumber);


        //receiptNumber
        String receiptNumber = FieldUtils.getValueOfField(request,"receiptNumber");
        ordreMission.setReceiptNumber(receiptNumber);

        //Donneur d'ordre
        String orderGive = FieldUtils.getValueOfField(request,"orderGive");

        signature = userRestClient.getEmployeeSignature(orderGive);

        Signatory signatory = signatoryService.getSignatory(orderGive);

        signatory.setSignature(signature);

        ordreMission.setOrderGiven(signatory);

        this.ordreMissionRepository.save(ordreMission);


        logger.info("Complete Map");

        return ordreMission;
    }

    @Override
    public MissionForm construct(Request request) {


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

        missionForm.setRequesterSignature(ordreMission.getOrderGiven().getSignature());

        logger.info("Complete Map");

        return missionForm;
    }
}
