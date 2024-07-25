package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.RequestDto;
import com.ccabank.memoservice.entity.Request;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;

public interface RequestService {

    AppServiceResult<Request> newRequest(RequestDto requestDto);

    ByteArrayResource downloadRequest(Long id);

    AppServiceResult<Request> validateRequest(Long id);

    AppServiceResult<List<RequestDto>> getRequestByStaff(String staff, String status);

}
