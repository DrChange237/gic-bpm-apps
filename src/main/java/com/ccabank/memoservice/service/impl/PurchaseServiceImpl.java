package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.reporting.PurchaseForm;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Purchase;
import com.ccabank.memoservice.entity.documenttype.Resumption;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.PurchaseRepository;
import com.ccabank.memoservice.repository.SignatoryRepository;
import com.ccabank.memoservice.repository.StaffRepository;
import com.ccabank.memoservice.service.faces.PurchaseService;
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
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SignatoryRepository signatoryRepository;

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

        Signatory owner = new Signatory();
        owner.setOwner(requester);
        //String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        owner.setSignature("");

        owner = signatoryRepository.save(owner);
        purchase.setOwner(owner);


        //Object
        System.out.println("object");
        String object = FieldUtils.getValueOfField(request,"object");
        purchase.setObject(object);

        //unity
        System.out.println("unity");
        String unity = FieldUtils.getValueOfField(request,"unity");
        purchase.setUnity(unity);

        //suppliers_submited
        System.out.println("suppliers_submited");
        String suppliersSubmited = FieldUtils.getValueOfField(request,"suppliers_submited");
        purchase.setSuppliersSubmited(suppliersSubmited);

        //supplier
        System.out.println("supplier");
        String supplier = FieldUtils.getValueOfField(request,"supplier");
        purchase.setSupplier(supplier);

        //supplier
        System.out.println("deadline");
        LocalDate deadline = LocalDate.parse(FieldUtils.getValueOfField(request,"deadline"));
        purchase.setDeadline(deadline);

        //typeReglement
        System.out.println("type_reglement");
        String typeReglement = FieldUtils.getValueOfField(request,"type_reglement");
        purchase.setTypeReglement(typeReglement);


        //account
        System.out.println("account");
        String account = FieldUtils.getValueOfField(request,"account");
        purchase.setAccount(account);


        this.purchaseRepository.save(purchase);

        return purchase;
    }

    @Override
    public PurchaseForm construct(Request request) {

        PurchaseForm purchaseForm = new PurchaseForm();


        return purchaseForm;
    }
}
