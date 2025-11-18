package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.dto.memo.ArchivageDto;
import com.ccabank.paperless.dto.memo.RequestDto;
import com.ccabank.paperless.dto.memo.RequestInfo;
import com.ccabank.paperless.entity.Request;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RequestService {

    Request newRequest(RequestDto requestDto, HttpServletRequest request);

    Request update(RequestDto requestDto);

    void validateRequest(Long id);

    Request download(Long id);

    RequestInfo details(Long id);

    RequestInfo detailForUpdate(Long id);


    RequestInfo suspend(Long id, String reason);

    RequestInfo achivage(ArchivageDto archivageDto);

    RequestInfo getRequestByReference(String reference);

    List<RequestInfo> getRequestByStaff(String staff, String status);

    List<RequestInfo> getRequestAll(HttpServletRequest req);

    List<RequestInfo> getRequestHistory(HttpServletRequest req);

    byte[] export(String reference, String type, String staff, String startDate, String endDate);
}
