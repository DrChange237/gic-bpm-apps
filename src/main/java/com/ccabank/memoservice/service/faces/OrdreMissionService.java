package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.reporting.MissionForm;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.OrdreMission;

public interface OrdreMissionService {

    OrdreMission save(Request request);

    MissionForm construct(Request request);
}
