package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Vacation;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.SignatoryRepository;
import com.ccabank.memoservice.repository.StaffRepository;
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



    @Override
    public Vacation save(Request request){

        Vacation vacation = new Vacation();

        vacation.setDate(LocalDate.now());

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");


        Staff requester = new Staff();
        requester.setFunction(staff.getFunction());
        requester.setName(staff.getUsername());
        requester.setUnity(staff.getDepartment());
        requester.setMatricule(staff.getMatricule());

        requester = staffRepository.save(requester);

        vacation.setPlace(staff.getAgencyName());

        Signatory owner = new Signatory();
        owner.setOwner(requester);
        String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        owner.setSignature(signature);

        owner = signatoryRepository.save(owner);
        vacation.setOwner(owner);


        //StartDate
        String startDate = FieldUtils.getValueOfField(request,"startDate");
        vacation.setStartDate(LocalDate.parse(startDate));


        //EndDate
        String endDate = FieldUtils.getValueOfField(request,"endDate");
        vacation.setEndDate(LocalDate.parse(endDate));

        //Interim
        String interim = FieldUtils.getValueOfField(request,"interim");
        staff = userRestClient.getAgencyByStaffUsername(interim, "key", "secret");
        requester = new Staff();
        requester.setFunction(staff.getFunction());
        requester.setName(staff.getUsername());
        requester.setUnity(staff.getDepartment());
        requester.setMatricule(staff.getMatricule());
        requester = staffRepository.save(requester);

        vacation.setInterim(requester);


        //Supervisor
        String username = request.getApprovalByPosition(1).getStaff();
        Signatory supervisor = signatoryService.getSignatory(username);
        vacation.setSupervisor(supervisor);


        //SupervisorNext
        username = request.getApprovalByPosition(2).getStaff();
        supervisor = signatoryService.getSignatory(username);
        vacation.setSupervisor2(supervisor);


        logger.info("Complete Map");

        return vacation;
    }


}
