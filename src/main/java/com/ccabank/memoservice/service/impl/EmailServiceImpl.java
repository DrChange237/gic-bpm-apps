package com.ccabank.memoservice.service.impl;


import com.ccabank.memoservice.entity.Approval;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.openfeign.EmailRestClient;
import com.ccabank.memoservice.service.faces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;

@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailRestClient emailRestClient;

    @Override
    public boolean sendAskApproval(Request request, Approval approval){

        System.out.println("sendAskApproval");

        return true;
    }

    @Override
    public boolean sendConfirmApproval(Request request, Approval approval){


        System.out.println("sendConfirmApproval");


        return true;
    }

    @Override
    public boolean sendRejectedApproval(Request request, Approval approval){

        System.out.println("sendRejectedApproval");


        return true;
    }

    @Override
    public boolean sendConfirmRequest(Request request){

        System.out.println("sendConfirmRequest");

        return true;
    }



}
