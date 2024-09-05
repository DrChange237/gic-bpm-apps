package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.entity.Approval;
import com.ccabank.memoservice.entity.ProcessUnity;
import com.ccabank.memoservice.entity.Request;

public interface EmailService {

    boolean sendAskApprovalUnity(Request request, Approval approval, ProcessUnity unity);

    boolean sendAskApproval(Request request, Approval approval);

    boolean sendConfirmApproval(Request request, Approval approval);

    boolean sendRejectedApproval(Request request, Approval approval);

    boolean sendConfirmRequest(Request request);
}
