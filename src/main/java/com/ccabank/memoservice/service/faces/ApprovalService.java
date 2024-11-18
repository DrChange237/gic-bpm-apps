package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.AcceptedApprovalDto;
import com.ccabank.memoservice.dto.memo.ApprovalListDto;
import com.ccabank.memoservice.dto.memo.ReassignDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ApprovalService {


    AppServiceResult<?> reassign(ReassignDto reassignDto);

    AppServiceResult<?> decision(HttpServletRequest request, AcceptedApprovalDto acceptedApprovalDto);

    AppServiceResult<List<ApprovalListDto>> getApprovalByStaff(HttpServletRequest request, String status);

    AppServiceResult<ApprovalListDto> getApprovalDetail(String id);
}
