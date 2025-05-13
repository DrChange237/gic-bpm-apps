package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.domain.AppServiceResult;
import com.ccabank.paperless.dto.memo.ProcessUnityDto;

import java.util.List;

public interface ProcessUnityService {
    AppServiceResult<ProcessUnityDto> create(ProcessUnityDto processUnityDto);

    AppServiceResult<ProcessUnityDto> update(ProcessUnityDto processUnityDto);

    AppServiceResult<ProcessUnityDto> getDetail(Long id);

    String getEmailUnity(String unityCode);

    AppServiceResult<List<ProcessUnityDto>> getAll();
}
