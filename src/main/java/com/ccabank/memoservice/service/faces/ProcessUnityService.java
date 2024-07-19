package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.ProcessUnityDto;
import com.ccabank.memoservice.entity.ProcessUnity;

import java.util.List;

public interface ProcessUnityService {
    AppServiceResult<ProcessUnity> create(ProcessUnityDto processUnityDto);

    AppServiceResult<ProcessUnity> update(ProcessUnityDto processUnityDto);

    AppServiceResult<ProcessUnityDto> getDetail(Long id);

    AppServiceResult<List<ProcessUnityDto>> getAll();
}
