package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.reporting.VacationForm;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Vacation;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.SignatoryRepository;
import com.ccabank.memoservice.repository.StaffRepository;
import com.ccabank.memoservice.repository.VacationRepository;
import com.ccabank.memoservice.service.faces.SignatoryService;
import com.ccabank.memoservice.service.faces.VacationService;
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
public class VacationServiceImpl implements VacationService {

    private static final Logger logger = LoggerFactory.getLogger(SaveDocumentServiceImpl.class);

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SignatoryRepository signatoryRepository;

    @Autowired
    private SignatoryService signatoryService;

    @Autowired
    private VacationRepository vacationRepository;



    @Override
    public Vacation save(Request request){

        Vacation vacation = new Vacation();

        vacation.setDate(LocalDate.now());

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");


        Staff requester = new Staff();
        requester.setFunction(staff.getFunction());
        requester.setName(staff.getName());
        requester.setUnity(staff.getDepartment());
        requester.setMatricule(staff.getMatricule());
        requester.setUsername(request.getStaff());

        requester = staffRepository.save(requester);
        vacation.setRequester(requester);

        vacation.setPlace(staff.getAgencyName());

        Signatory owner = new Signatory();
        owner.setOwner(requester);
        //String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        owner.setSignature("");

        owner = signatoryRepository.save(owner);
        vacation.setOwner(owner);


        //StartDate
        String startDate = FieldUtils.getValueOfField(request,"startDate");
        vacation.setStartDate(LocalDate.parse(startDate));


        //EndDate
        String endDate = FieldUtils.getValueOfField(request,"realEndDate");
        vacation.setEndDate(LocalDate.parse(endDate));

        //Critic Folder
        String criticFolder = FieldUtils.getValueOfField(request,"criticFolder");
        vacation.setCriticFolder(criticFolder);

        //Main Work
        String mainWork = FieldUtils.getValueOfField(request,"mainWork");
        vacation.setMainWork(mainWork);



        //Interim
        String interim = request.getApprovalByPosition(1).getStaff();
        staff = userRestClient.getAgencyByStaffUsername(interim, "key", "secret");
        requester = new Staff();
        requester.setFunction(staff.getFunction());
        requester.setName(staff.getName());
        requester.setUnity(staff.getDepartment());
        requester.setMatricule(staff.getMatricule());
        requester.setUsername(staff.getUsername());
        requester = staffRepository.save(requester);

        vacation.setInterim(requester);



        //Supervisor
        String username = request.getApprovalByPosition(2).getStaff();
        Signatory supervisor = signatoryService.getSignatory(username);
        vacation.setSupervisor(supervisor);


        //SupervisorNext
        username = request.getApprovalByPosition(3).getStaff();
        supervisor = signatoryService.getSignatory(username);
        vacation.setSupervisor2(supervisor);


        vacation = vacationRepository.save(vacation);

        return vacation;

    }

    @Override
    public VacationForm construct(Request request){

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

}
