package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.dto.memo.AcceptedApprovalDto;
import com.ccabank.paperless.dto.memo.ApprovalListDto;
import com.ccabank.paperless.dto.memo.ReassignDto;
import com.ccabank.paperless.dto.memo.TakeLeaveDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ApprovalService {


    void reassign(ReassignDto reassignDto);

    void freeless(HttpServletRequest request, TakeLeaveDto takeLeaveDto);

    void decision(HttpServletRequest request, AcceptedApprovalDto acceptedApprovalDto);

    void decisionViaEmail(String key, String TaskId, boolean decision, String comment);

    void approve(AcceptedApprovalDto acceptedApprovalDto, String assignee) throws Exception;


    void rejected(AcceptedApprovalDto acceptedApprovalDto);

    List<ApprovalListDto> getApprovalByStaff(HttpServletRequest request, String status);

    List<ApprovalListDto> getAllApprobations(HttpServletRequest req);

    ApprovalListDto getApprovalDetail(String id);

    void relanceApprobation(String taskId);

    void relanceForDueDate(String taskId);
}
