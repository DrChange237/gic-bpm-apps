package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Vacation;

public interface VacationService {
    Vacation save(Request request);
}
