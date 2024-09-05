package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.reporting.AbsenceForm;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Absence;
import com.ccabank.memoservice.entity.documenttype.sub.Deduction;
import com.ccabank.memoservice.entity.documenttype.sub.Settlement;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.AbsenceRepository;
import com.ccabank.memoservice.repository.SettlementRepository;
import com.ccabank.memoservice.repository.SignatoryRepository;
import com.ccabank.memoservice.repository.StaffRepository;
import com.ccabank.memoservice.service.faces.AbsenceService;
import com.ccabank.memoservice.service.faces.SignatoryService;
import com.ccabank.memoservice.util.WorkDayCalculator;
import com.ccabank.memoservice.util.field.FieldUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;


@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class AbsenceServiceImpl implements AbsenceService {

    private static final Logger logger = LoggerFactory.getLogger(AbsenceServiceImpl.class);

    @Autowired
    private SignatoryService signatoryService;

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SignatoryRepository signatoryRepository;

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private AbsenceRepository absenceRepository;


    @Override
    public Absence save(Request request){

        System.out.println("Save Absence");

        Absence absence = new Absence();

        absence.setDate(LocalDate.now());

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        System.out.println("Get Staff");


        Staff requester = new Staff();
        requester.setFunction(staff.getFunction());
        requester.setName(staff.getName());
        requester.setUnity(staff.getDepartment());
        requester.setMatricule(staff.getMatricule());
        requester.setUsername(staff.getUsername());


        requester = staffRepository.save(requester);
        absence.setRequester(requester);

        absence.setPlace(staff.getAgencyName());

        Signatory owner = new Signatory();
        owner.setOwner(requester);
        //String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        //owner.setSignature(signature);

        owner = signatoryRepository.save(owner);

        System.out.println("Save Owner");


        absence.setOwner(owner);

        //StartDate
        String startDate = FieldUtils.getValueOfField(request,"startDate");
        absence.setStartDate(LocalDate.parse(startDate));


        //EndDate
        String endDate = FieldUtils.getValueOfField(request,"endDate");
        absence.setEndDate(LocalDate.parse(endDate));

        //days
        String days = FieldUtils.getValueOfField(request,"days");
        Integer workdays = Math.toIntExact(WorkDayCalculator.calculateWorkdays(absence.getStartDate(), absence.getEndDate()));
        absence.setDays(workdays);



        //reason
        String reason = FieldUtils.getValueOfField(request,"reason");
        absence.setReason(reason);

        //reason
        String interim = FieldUtils.getValueOfField(request,"interim");

        staff = userRestClient.getAgencyByStaffUsername(interim, "key", "secret");

        System.out.println("Get Interim");


        Staff interimaire = new Staff();
        interimaire.setFunction(staff.getFunction());
        interimaire.setName(staff.getName());
        interimaire.setUnity(staff.getDepartment());
        interimaire.setMatricule(staff.getMatricule());

        interimaire = staffRepository.save(interimaire);
        absence.setInterim(interimaire);


        //Supervisor
        String username = request.getApprovalByPosition(1).getStaff();
        System.out.println("Supervisor :" + username);
        Signatory supervisor = signatoryService.getSignatory(username);
        absence.setSupervisor(supervisor);

        System.out.println("Get Supervisor");


        //Supervisor
        username = request.getApprovalByPosition(2).getStaff();
        System.out.println("Supervisor2 :" + username);
        Signatory supervisor2 = signatoryService.getSignatory(username);
        absence.setSupervisor2(supervisor2);

        System.out.println("Get Supervisor2");


        //HeadOffice
        username = request.getApprovalByPosition(3).getStaff();
        System.out.println("HeadOffice :" + username);
        Signatory headOffice = signatoryService.getSignatory(username);
        absence.setHeadOffice(headOffice);

        System.out.println("Get HeadOffice");






        String absencePaid = FieldUtils.getValueOfField(request,"absence_paid");
        String absenceUnPaid = FieldUtils.getValueOfField(request,"absence_unpaid");


        Settlement absenceS = new Settlement();
        absenceS.setPaid(Double.valueOf(absencePaid));
        absenceS.setUnpaid(Double.valueOf(absenceUnPaid));
        absenceS = settlementRepository.save(absenceS);
        absence.setAbsence(absenceS);

        String stockPaid = FieldUtils.getValueOfField(request,"stock_paid");
        String stockUnPaid = FieldUtils.getValueOfField(request,"stock_unpaid");

        Settlement stockS = new Settlement();
        stockS.setPaid(Double.valueOf(stockPaid));
        stockS.setUnpaid(Double.valueOf(stockUnPaid));
        stockS = settlementRepository.save(stockS);
        absence.setStock(stockS);

        String advicePaid = FieldUtils.getValueOfField(request,"advice_paid");
        String adviceUnPaid = FieldUtils.getValueOfField(request,"advice_unpaid");

        Settlement adviceS = new Settlement();
        adviceS.setPaid(Double.valueOf(advicePaid));
        adviceS.setUnpaid(Double.valueOf(adviceUnPaid));
        adviceS = settlementRepository.save(adviceS);
        absence.setAdvice(adviceS);

        String rightsPaid = FieldUtils.getValueOfField(request,"rights_paid");
        String rightsUnPaid = FieldUtils.getValueOfField(request,"rights_unpaid");

        Settlement rightsS = new Settlement();
        rightsS.setPaid(Double.valueOf(rightsPaid));
        rightsS.setUnpaid(Double.valueOf(rightsUnPaid));
        rightsS = settlementRepository.save(rightsS);
        absence.setRights(rightsS);


        String salaryPaid = FieldUtils.getValueOfField(request,"salary_paid");
        String salaryUnPaid = FieldUtils.getValueOfField(request,"salary_unpaid");

        Settlement salaryS = new Settlement();
        salaryS.setPaid(Double.valueOf(salaryPaid));
        salaryS.setUnpaid(Double.valueOf(salaryUnPaid));
        salaryS = settlementRepository.save(salaryS);
        absence.setSalary(salaryS);

        String vacationPaid = FieldUtils.getValueOfField(request,"vacation_paid");
        String vacationUnPaid = FieldUtils.getValueOfField(request,"vacation_unpaid");

        Settlement vacationS = new Settlement();
        vacationS.setPaid(Double.valueOf(vacationPaid));
        vacationS.setUnpaid(Double.valueOf(vacationUnPaid));
        vacationS = settlementRepository.save(vacationS);
        absence.setVacation(vacationS);

        String deduction = FieldUtils.getValueOfField(request,"deduction");

        Deduction deductionS = Deduction.valueOf(deduction);
        absence.setDeduction(deductionS);

        absence = absenceRepository.save(absence);



        return absence;

    }

    @Override
    public AbsenceForm construct(Request request){

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

}
