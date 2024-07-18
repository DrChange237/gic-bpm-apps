package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.reporting.ResumptionForm;
import com.ccabank.memoservice.entity.Request;

import java.io.InputStream;

public interface MapToReportService {

    InputStream reportRequest(Request request);

    ResumptionForm constructResumptionRequest(Request request);
}
