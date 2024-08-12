package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.SignatoryRepository;
import com.ccabank.memoservice.repository.StaffRepository;
import com.ccabank.memoservice.service.faces.SignatoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;


@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class SignatoryServiceImpl implements SignatoryService {

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SignatoryRepository signatoryRepository;

    @Override
    public Signatory getSignatory(String username){

        Signatory supervisor = new Signatory();

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(username, "key", "secret");
        Staff requester = new Staff();
        requester.setFunction(staff.getFunction());
        requester.setName(staff.getName());
        requester.setUnity(staff.getDepartment());
        requester.setMatricule(staff.getMatricule());
        requester.setUsername(staff.getUsername());
        requester = staffRepository.save(requester);

        supervisor.setOwner(requester);
        //supervisor.setSignature(userRestClient.getEmployeeSignature(staff.getUsername()));

        return signatoryRepository.save(supervisor);
    }


}
