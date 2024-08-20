package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.reporting.PurchaseForm;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Purchase;
import com.ccabank.memoservice.entity.documenttype.Resumption;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.PurchaseRepository;
import com.ccabank.memoservice.repository.StaffRepository;
import com.ccabank.memoservice.service.faces.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public Purchase save(Request request) {

        Purchase purchase = new Purchase();

        purchase.setDate(LocalDate.now());

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");


        Staff requester = new Staff();
        requester.setFunction(staff.getFunction());
        requester.setName(staff.getName());
        requester.setUnity(staff.getDepartment());
        requester.setMatricule(staff.getMatricule());
        requester.setUsername(request.getStaff());

        requester = staffRepository.save(requester);
        purchase.setRequester(requester);



        return null;
    }

    @Override
    public PurchaseForm construct(Request request) {


        return null;
    }
}
