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
        String subject = request.getReference() +  " - Nouvelle Approbation Requise";
        String message = "Votre accord ou signature est sollicitée pour une demande ( " + request.getType().getName() + ")";
        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");
        UserRestDto approver = userRestClient.getAgencyByStaffUsername(approval.getStaff(), "key", "secret");
        String from = approver.getEmail() ;

        System.out.println("Email :" + approver.getEmail());


        EmailDto emailDto = new EmailDto();

        emailDto.setFrom("notifications@cca-bank.com");
        emailDto.setTo(from);
        emailDto.setSubject(subject);
        emailDto.setBody(message);
        emailDto.setCc(sender.getEmail());

        try {
            emailRestClient.send(emailDto);
        }catch (Exception e){
            System.out.println("Email Error" + e.getMessage());
        }


        return true;
    }

    @Override
    public boolean sendConfirmApproval(Request request, Approval approval){


        System.out.println("sendConfirmApproval");
        String subject = request.getReference() + " - Confirmation de l'approbation";

        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        UserRestDto approver = userRestClient.getAgencyByStaffUsername(approval.getStaff(), "key", "secret");

        String message = "M. " + approver.getName() + " a approuvé votre requete avec pour référence " + request.getReference();

        String from = approver.getEmail() ;

        System.out.println("Email :" + approver.getEmail());



        EmailDto emailDto = new EmailDto();

        emailDto.setFrom("notifications@cca-bank.com");
        emailDto.setTo(from);
        emailDto.setSubject(subject);
        emailDto.setBody(message);
        emailDto.setCc(sender.getEmail());

        try {
            emailRestClient.send(emailDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean sendRejectedApproval(Request request, Approval approval){

        System.out.println("sendRejectedApproval");

        String subject = request.getReference() + " - Refus de l'approbation";

        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        UserRestDto approver = userRestClient.getAgencyByStaffUsername(approval.getStaff(), "key", "secret");


        String message = "M. " + approver.getName() + " a rejeté votre requete avec pour référence " + request.getReference();


        String from = approver.getEmail() ;

        System.out.println("Email :" + approver.getEmail());


        EmailDto emailDto = new EmailDto();

        emailDto.setFrom("notifications@cca-bank.com");
        emailDto.setTo(from);
        emailDto.setSubject(subject);
        emailDto.setBody(message);
        emailDto.setCc(sender.getEmail());

        try {
            emailRestClient.send(emailDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean sendConfirmRequest(Request request){


        String subject = request.getReference() + " - Votre "+ request.getType().getName() + " a reçu toutes les approbations";

        String message = "Votre "+ request.getType().getName() + " a reçu toutes les approbations.";

        UserRestDto sender = userRestClient.getAgencyByStaffUsername(request.getStaff(), "key", "secret");

        String from = sender.getEmail() ;

        System.out.println("Email :" + sender.getEmail());

        EmailDto emailDto = new EmailDto();

        emailDto.setFrom("notifications@cca-bank.com");
        emailDto.setTo(from);
        emailDto.setSubject(subject);
        emailDto.setBody(message);

        try {
            emailRestClient.send(emailDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }



}
