package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.DocumentTypeDto;

import java.util.List;

public interface DocumentTypeService {
    AppServiceResult<List<DocumentTypeDto>> getDocumentTypes();
}
