package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.reporting.ResumptionForm;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Resumption;

public interface ResumptionService {
    Resumption save(Request request);

    ResumptionForm construct(Request request);
}
