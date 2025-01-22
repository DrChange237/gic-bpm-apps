package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.email.EmailAskApprovalDto;

public interface EmailService {


    boolean sendFiles(EmailAskApprovalDto ask);

    boolean sendAskApproval(EmailAskApprovalDto ask);


    boolean sendConfirmApproval(EmailAskApprovalDto ask);


    boolean sendRejectedApproval(EmailAskApprovalDto ask);


    boolean sendConfirmRequest(EmailAskApprovalDto ask);
}
