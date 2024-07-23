package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.reporting.AbsenceForm;
import com.ccabank.memoservice.dto.reporting.MissionForm;
import com.ccabank.memoservice.dto.reporting.ResumptionForm;
import com.ccabank.memoservice.entity.Request;

import java.io.InputStream;

public interface MapToReportService {

    InputStream reportRequest(Request request);

    AbsenceForm constructAbsenceRequest(Request request);

    MissionForm constructMissionRequest(Request request);

    ResumptionForm constructResumptionRequest(Request request);
}
