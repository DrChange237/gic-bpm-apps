package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.dto.memo.ArchivageDto;
import com.ccabank.memoservice.dto.memo.RequestDto;
import com.ccabank.memoservice.dto.memo.RequestInfo;
import com.ccabank.memoservice.entity.Request;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.springframework.core.io.ByteArrayResource;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RequestService {

    AppServiceResult<Request> newRequest(RequestDto requestDto, HttpServletRequest request);

    AppServiceResult<Request> update(RequestDto requestDto);

    AppServiceResult<?> validateRequest(Long id);

    AppServiceResult<?> download(Long id);

    AppServiceResult<RequestInfo> details(Long id);

    AppServiceResult<RequestInfo> suspend(Long id);

    AppServiceResult<RequestInfo> achivage(ArchivageDto archivageDto);

    AppServiceResult<RequestInfo> getRequestByReference(String reference);

    AppServiceResult<List<RequestInfo>> getRequestByStaff(String staff, String status);

    AppServiceResult<List<RequestInfo>> getRequestAll(HttpServletRequest req);

    AppServiceResult<List<RequestInfo>> getRequestHistory(HttpServletRequest req);
}
