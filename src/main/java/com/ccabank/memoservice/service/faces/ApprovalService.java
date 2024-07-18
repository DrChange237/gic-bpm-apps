package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.entity.Approval;
import com.ccabank.memoservice.entity.Request;

import java.util.List;

public interface ApprovalService {
    Approval getNextPendingApproval(Request request);

    Approval getCurrentApproval(Request request);

    AppServiceResult<ApprovalDto> approve(Long id);

    AppServiceResult<ApprovalDto> rejected(Long id);

    AppServiceResult<List<ApprovalDto>> getApprovalByStaff(String staff, String status);
}
