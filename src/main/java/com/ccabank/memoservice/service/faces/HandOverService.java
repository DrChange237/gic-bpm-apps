package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.reporting.HandOverForm;
import com.ccabank.memoservice.entity.Request;

public interface HandOverService {

    HandOverForm construct(Request request);
}
