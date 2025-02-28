package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.AcceptedApprovalDto;
import com.ccabank.memoservice.dto.memo.ApprovalListDto;
import com.ccabank.memoservice.dto.memo.ReassignDto;
import com.ccabank.memoservice.dto.memo.TakeLeaveDto;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ApprovalService {


    AppServiceResult<?> reassign(ReassignDto reassignDto);

    AppServiceResult<?> freeless(HttpServletRequest request, TakeLeaveDto takeLeaveDto);

    AppServiceResult<?> decision(HttpServletRequest request, AcceptedApprovalDto acceptedApprovalDto);

    AppServiceResult<?> decisionViaEmail(String key, String TaskId, boolean decision, String comment);

    @Transactional
    AppServiceResult<?> approve(AcceptedApprovalDto acceptedApprovalDto, String assignee) throws Exception;

    @Transactional
    AppServiceResult<?> rejected(AcceptedApprovalDto acceptedApprovalDto);

    AppServiceResult<List<ApprovalListDto>> getApprovalByStaff(HttpServletRequest request, String status);

    AppServiceResult<List<ApprovalListDto>> getAllApprobations(HttpServletRequest req);

    AppServiceResult<ApprovalListDto> getApprovalDetail(String id);
}
