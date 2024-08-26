package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Memo;
import com.ccabank.memoservice.entity.documenttype.Vacation;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.repository.MemoRepository;
import com.ccabank.memoservice.repository.SignatoryRepository;
import com.ccabank.memoservice.repository.StaffRepository;
import com.ccabank.memoservice.service.faces.MemoService;
import com.ccabank.memoservice.service.faces.SignatoryService;
import com.ccabank.memoservice.util.field.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;


@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class MemoServiceImpl implements MemoService {

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private SignatoryRepository signatoryRepository;

    @Autowired
    private SignatoryService signatoryService;

    @Override
    public Memo save(Request request) {

        System.out.println("Save Memo");


        Memo memo = new Memo();
        memo.setDate(LocalDate.now());

        List<Signatory> signatories = new ArrayList<>();

        UserRestDto staff = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        EmployeeInfo employee = userRestClient.getStaffByUsername(request.getStaff());



        System.out.println("Add Requester");
        Staff requester = new Staff();
        requester.setFunction(staff.getFunction());
        requester.setName(staff.getName());
        requester.setUnity(staff.getDepartment());
        requester.setMatricule(staff.getMatricule());
        requester.setUsername(request.getStaff());

        requester = staffRepository.save(requester);
        memo.setRequester(requester);


        Signatory owner = new Signatory();
        owner.setOwner(requester);
        String signature = userRestClient.getEmployeeSignature(staff.getUsername());
        owner.setSignature(signature);
        owner.setMemo(memo);
        owner = signatoryRepository.save(owner);
        signatories.add(owner);

        //Subject
        System.out.println("Subject");
        String subject = FieldUtils.getValueOfField(request,"subject");
        memo.setSubject(subject);

        //Material
        System.out.println("Material");
        String material = FieldUtils.getValueOfField(request,"material");
        memo.setMaterial(material);

        //Receiver
        System.out.println("Receiver");
        String receiver = FieldUtils.getValueOfField(request,"receiver");
        memo.setReceiver(receiver);

        //Memo
        System.out.println("Memo");
        String body = FieldUtils.getValueOfField(request,"body");
        memo.setBody(body);


        //Supervisor
        System.out.println("Supervisor");
        String username = request.getApprovalByPosition(1).getStaff();
        Signatory supervisor = signatoryService.getSignatory(username);
        memo.setSupervisor(supervisor);
        signatories.add(supervisor);


        memo = memoRepository.save(memo);

        return memo;
    }
}
