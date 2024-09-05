package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.reporting.MemoForm;
import com.ccabank.memoservice.entity.Request;
import com.ccabank.memoservice.entity.documenttype.Memo;
import com.ccabank.memoservice.entity.documenttype.OrdreMission;

public interface MemoService {

    Memo save(Request request);


    MemoForm construct(Request request);
}
