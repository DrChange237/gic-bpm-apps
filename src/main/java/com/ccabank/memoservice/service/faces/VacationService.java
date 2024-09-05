package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.reporting.VacationForm;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Vacation;

public interface VacationService {
    Vacation save(Request request);

    VacationForm construct(Request request);
}
