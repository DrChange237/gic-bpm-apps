package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.ArchivageDto;
import com.ccabank.memoservice.dto.memo.RequestDto;
import com.ccabank.memoservice.entity.Request;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;

public interface RequestService {

    AppServiceResult<Request> newRequest(RequestDto requestDto);

    AppServiceResult<Request> update(RequestDto requestDto);

    ByteArrayResource downloadRequest(Long id);

    AppServiceResult<?> validateRequest(Long id);

    AppServiceResult<RequestDto> details(Long id);

    AppServiceResult<RequestDto> achivage(ArchivageDto archivageDto);

    AppServiceResult<RequestDto> getRequestByReference(String reference);

    AppServiceResult<List<RequestDto>> getRequestByStaff(String staff, String status);

    AppServiceResult<List<RequestDto>> getRequestAll(String staff);
}
