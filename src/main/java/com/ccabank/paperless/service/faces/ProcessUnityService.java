package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.dto.memo.ProcessUnityDto;

import java.util.List;

public interface ProcessUnityService {
    ProcessUnityDto create(ProcessUnityDto processUnityDto);

    ProcessUnityDto update(ProcessUnityDto processUnityDto);

    ProcessUnityDto getDetail(Long id);

    String getEmailUnity(String unityCode);

    List<ProcessUnityDto> getAll();
}
