package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Absence;
import com.ccabank.memoservice.entity.documenttype.Vacation;
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

}
