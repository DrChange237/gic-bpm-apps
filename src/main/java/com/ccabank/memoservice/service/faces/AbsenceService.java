package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Absence;

public interface AbsenceService {
    Absence save(Request request);

}
