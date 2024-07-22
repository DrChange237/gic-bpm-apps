package com.ccabank.memoservice.service.impl;


import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.user.UserRestDto;
import com.ccabank.memoservice.entity.Approval;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.service.faces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailRestClient emailRestClient;

    @Autowired
    private UserRestClient userRestClient;

    @Override
    public boolean sendAskApproval(Request request, Approval approval){

        System.out.println("sendAskApproval");

        String subject = "Nouvelle Approbation Requise";

        String message = "Votre accord ou signature est sollicitée pour une demande ( " + request.getType().getName() + ")";

        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        UserRestDto approver = userRestClient.getAgencyByStaffUsername(approval.getStaff(), "key", "secret");

        String to = approver.getEmail() ;

        List<String> bCC = new ArrayList<>();

        bCC.add(sender.getEmail());

        EmailDto emailDto = new EmailDto();

        emailDto.setTo(to);
        emailDto.setSubject(subject);
        emailDto.setMessage(message);
        emailDto.setbCC(bCC);

        emailRestClient.send(emailDto);

        return true;
    }

    @Override
    public boolean sendConfirmApproval(Request request, Approval approval){


        System.out.println("sendConfirmApproval");

        System.out.println("sendAskApproval");

        String subject = "Nouvelle Approbation Requise";

        String message = "Votre accord ou signature est sollicitée pour une demande ( " + request.getType().getName() + ")";

        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        UserRestDto approver = userRestClient.getAgencyByStaffUsername(approval.getStaff(), "key", "secret");

        String to = approver.getEmail() ;

        List<String> bCC = new ArrayList<>();

        bCC.add(sender.getEmail());

        EmailDto emailDto = new EmailDto();

        emailDto.setTo(to);
        emailDto.setSubject(subject);
        emailDto.setMessage(message);
        emailDto.setbCC(bCC);

        emailRestClient.send(emailDto);

        return true;
    }

    @Override
    public boolean sendRejectedApproval(Request request, Approval approval){

        System.out.println("sendRejectedApproval");

        System.out.println("sendAskApproval");

        String subject = "Nouvelle Approbation Requise";

        String message = "Votre accord ou signature est sollicitée pour une demande ( " + request.getType().getName() + ")";

        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        UserRestDto approver = userRestClient.getAgencyByStaffUsername(approval.getStaff(), "key", "secret");

        String to = approver.getEmail() ;

        List<String> bCC = new ArrayList<>();

        bCC.add(sender.getEmail());

        EmailDto emailDto = new EmailDto();

        emailDto.setTo(to);
        emailDto.setSubject(subject);
        emailDto.setMessage(message);
        emailDto.setbCC(bCC);

        emailRestClient.send(emailDto);

        return true;
    }

    @Override
    public boolean sendConfirmRequest(Request request){

        System.out.println("sendConfirmRequest");

        System.out.println("sendAskApproval");

        String subject = "Nouvelle Approbation Requise";

        String message = "Votre accord ou signature est sollicitée pour une demande ( " + request.getType().getName() + ")";

        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        String to = sender.getEmail() ;

        List<String> bCC = new ArrayList<>();

        bCC.add(sender.getEmail());

        EmailDto emailDto = new EmailDto();

        emailDto.setTo(to);
        emailDto.setSubject(subject);
        emailDto.setMessage(message);
        emailDto.setbCC(bCC);

        emailRestClient.send(emailDto);

        return true;
    }



}
