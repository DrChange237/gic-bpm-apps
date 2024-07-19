package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.dto.memo.DocumentTypeDto;

import java.util.List;

public interface DocumentTypeService {
    AppServiceResult<List<DocumentTypeDto>> getDocumentTypes();

    AppServiceResult<DocumentTypeDto> getDocumentType(String structure);

    List<ApprovalDto> getStaticApprobals(String name);
}
