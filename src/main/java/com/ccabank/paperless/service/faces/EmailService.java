package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.dto.email.EmailAskApprovalDto;
import com.ccabank.paperless.dto.memo.ApprovalDto;
import com.ccabank.paperless.dto.memo.FieldDto;
import com.ccabank.paperless.entity.ApprovalKey;

import java.util.List;

public interface EmailService {

    boolean sendForValidation(ApprovalKey approvalKey, EmailAskApprovalDto ask, List<FieldDto> fields, List<ApprovalDto> approvalDtos);

    boolean sendFiles(EmailAskApprovalDto ask);

    boolean sendAskApproval(EmailAskApprovalDto ask);


    boolean sendConfirmApproval(EmailAskApprovalDto ask);


    boolean sendRejectedApproval(EmailAskApprovalDto ask);


    boolean sendConfirmRequest(EmailAskApprovalDto ask);
}
