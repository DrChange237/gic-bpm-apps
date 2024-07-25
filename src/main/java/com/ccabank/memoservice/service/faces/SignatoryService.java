package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.entity.documenttype.sub.Signatory;

public interface SignatoryService {
    Signatory getSignatory(String username);
}
