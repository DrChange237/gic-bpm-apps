package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.WorkForm;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.SignatoryRepository;
import com.ccabank.memoservice.repository.StaffRepository;
import com.ccabank.memoservice.service.faces.SignatoryService;
import com.ccabank.memoservice.service.faces.WorkformService;
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
public class WorkFormServiceImpl implements WorkformService {

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SignatoryRepository signatoryRepository;

    @Autowired
    private SignatoryService signatoryService;

    @Override
    public WorkForm save(Request request){

        WorkForm workForm = new WorkForm();

        workForm.setDate(LocalDate.now());

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");


        Staff requester = new Staff();
        requester.setFunction(staff.getFunction());
        requester.setName(staff.getName());
        requester.setUnity(staff.getDepartment());
        requester.setMatricule(staff.getMatricule());
        requester.setUsername(request.getStaff());

        requester = staffRepository.save(requester);
        workForm.setRequester(requester);

        Signatory owner = new Signatory();
        owner.setOwner(requester);
        //String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        owner.setSignature("");

        owner = signatoryRepository.save(owner);
        workForm.setOwner(owner);

        //Work
        String works = FieldUtils.getValueOfField(request,"works");
        workForm.setWorkToSolve(works);

        //Provider
        String provider = FieldUtils.getValueOfField(request,"provider");
        workForm.setProvider(provider);

        //Designation
        String designation = FieldUtils.getValueOfField(request,"designation");
        workForm.setDesignation(designation);

        //Brand
        String brand = FieldUtils.getValueOfField(request,"brand");
        workForm.setBrand(designation);

        //Code
        String code = FieldUtils.getValueOfField(request,"code");
        workForm.setCode(code);

        //Supervisor
        String username = request.getApprovalByPosition(1).getStaff();
        Signatory supervisor = signatoryService.getSignatory(username);
        workForm.setSupervisor(supervisor);

        //Head
        String head = request.getApprovalByPosition(2).getStaff();
        Signatory headSignatory = signatoryService.getSignatory(username);
        workForm.setHead(headSignatory);

        //Accountant
        String accountant = request.getApprovalByPosition(3).getStaff();
        Signatory accountantSignatory = signatoryService.getSignatory(username);
        workForm.setAccountant(accountantSignatory);

        return  workForm;
    }

}
