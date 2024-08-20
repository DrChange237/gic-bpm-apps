package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.reporting.PurchaseForm;
import com.ccabank.memoservice.dto.reporting.ResumptionForm;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.OrdreMission;
import com.ccabank.memoservice.entity.documenttype.Purchase;

public interface PurchaseService {

    Purchase save(Request request);

    PurchaseForm construct(Request request);



}
