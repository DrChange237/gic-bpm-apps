package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.reporting.AbsenceForm;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Absence;

public interface AbsenceService {
    Absence save(Request request);

    AbsenceForm construct(Request request);
}
