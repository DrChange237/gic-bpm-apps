package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.RequestDto;
import com.ccabank.memoservice.entity.Request;

import java.io.InputStream;
import java.util.List;

public interface RequestService {

    AppServiceResult<Request> newRequest(RequestDto requestDto);

    InputStream downloadRequest(Long id);

    AppServiceResult<Request> validateRequest(Long id);

    AppServiceResult<List<RequestDto>> getRequestByStaff(String staff, String status);

}
