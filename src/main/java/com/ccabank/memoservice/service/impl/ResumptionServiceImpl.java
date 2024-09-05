package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.reporting.ResumptionForm;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Resumption;
import com.ccabank.memoservice.entity.documenttype.sub.ResumptionReason;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.ResumptionRepository;
import com.ccabank.memoservice.repository.SignatoryRepository;
import com.ccabank.memoservice.repository.StaffRepository;
import com.ccabank.memoservice.service.faces.ResumptionService;
import com.ccabank.memoservice.service.faces.SignatoryService;
import com.ccabank.memoservice.util.field.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class ResumptionServiceImpl implements ResumptionService {

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SignatoryRepository signatoryRepository;

    @Autowired
    private SignatoryService signatoryService;

    @Autowired
    private ResumptionRepository resumptionRepository;

    @Override
    public Resumption save(Request request){

        Resumption resumption = new Resumption();

        resumption.setDate(LocalDate.now());

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");


        Staff requester = new Staff();
        requester.setFunction(staff.getFunction());
        requester.setName(staff.getName());
        requester.setUnity(staff.getDepartment());
        requester.setMatricule(staff.getMatricule());
        requester.setUsername(request.getStaff());

        requester = staffRepository.save(requester);
        resumption.setRequester(requester);

        resumption.setPlace(staff.getAgencyName());

        Signatory owner = new Signatory();
        owner.setOwner(requester);
        //String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        owner.setSignature("");

        owner = signatoryRepository.save(owner);
        resumption.setOwner(owner);


        //StartDate
        String startDate = FieldUtils.getValueOfField(request,"startDate");
        resumption.setStartDate(LocalDate.parse(startDate));


        //EndDate
        String endDate = FieldUtils.getValueOfField(request,"endDate");
        resumption.setEndDate(LocalDate.parse(endDate));


        //realEndDate
        String realEndDate = FieldUtils.getValueOfField(request,"realEndDate");
        resumption.setRealEndDate(LocalDate.parse(realEndDate));

        //reason
        String reason = FieldUtils.getValueOfField(request,"reason");
        resumption.setReason(ResumptionReason.valueOf(reason));


        //Supervisor
        String username = request.getApprovalByPosition(1).getStaff();
        Signatory supervisor = signatoryService.getSignatory(username);
        resumption.setSupervisor(supervisor);



        resumption = resumptionRepository.save(resumption);

        return resumption;

    }

    @Override
    public ResumptionForm construct(Request request){

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
            signatory.setName(staff.getName());
            signatory.setSignature(userRestClient.getEmployeeSignature(staff.getUsername()));
            resumptionForm.setSupervisor(signatory);
        }


        return resumptionForm;

    }


}
