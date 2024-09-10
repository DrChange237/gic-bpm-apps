package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.AcceptedApprovalDto;
import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.dto.memo.ApprovalListDto;
import com.ccabank.memoservice.dto.memo.ReassignDto;
import com.ccabank.memoservice.entity.Approval;
import com.ccabank.memoservice.entity.Request;

import java.util.List;

public interface ApprovalService {

    Approval getApprovalWithPosition(Request request, int position);

    Approval getNextPendingApproval(Approval approval);

    Approval getNextApproval(Request request);

    Approval getCurrentApproval(Request request);

    AppServiceResult<?> decision(AcceptedApprovalDto acceptedApprovalDto);

    AppServiceResult<?> reassign(ReassignDto reassignDto);

    AppServiceResult<?> approve(AcceptedApprovalDto acceptedApprovalDto);

    AppServiceResult<?> rejected(AcceptedApprovalDto acceptedApprovalDto);

    AppServiceResult<List<ApprovalListDto>> getApprovalByStaff(String staff, String status);

    AppServiceResult<ApprovalDto> getApprovalDetail(Long id);
}
