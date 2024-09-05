package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.WorkForm;

public interface WorkformService {
    WorkForm save(Request request);

    com.ccabank.memoservice.dto.reporting.WorkForm construct(Request request);
}
