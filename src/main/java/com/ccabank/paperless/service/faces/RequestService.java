package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.domain.AppServiceResult;
import com.ccabank.paperless.dto.memo.ArchivageDto;
import com.ccabank.paperless.dto.memo.RequestDto;
import com.ccabank.paperless.dto.memo.RequestInfo;
import com.ccabank.paperless.entity.Request;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public interface RequestService {

    AppServiceResult<Request> newRequest(RequestDto requestDto, HttpServletRequest request);

    AppServiceResult<Request> update(RequestDto requestDto);

    AppServiceResult<?> validateRequest(Long id);

    AppServiceResult<?> download(Long id);

    AppServiceResult<RequestInfo> details(Long id);

    AppServiceResult<RequestInfo> detailForUpdate(Long id);


    AppServiceResult<RequestInfo> suspend(Long id, String reason);

    AppServiceResult<RequestInfo> achivage(ArchivageDto archivageDto);

    AppServiceResult<RequestInfo> getRequestByReference(String reference);

    AppServiceResult<List<RequestInfo>> getRequestByStaff(String staff, String status);

    AppServiceResult<List<RequestInfo>> getRequestAll(HttpServletRequest req);

    AppServiceResult<List<RequestInfo>> getRequestHistory(HttpServletRequest req);

    byte[] export(String reference, String type, String staff, String startDate, String endDate);
}
