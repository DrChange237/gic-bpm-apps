package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.domain.AppServiceResult;
import com.ccabank.paperless.dto.memo.DocumentTypeDto;

import java.util.List;

public interface DocumentTypeService {
    AppServiceResult<List<DocumentTypeDto>> getDocumentTypes();

    AppServiceResult<DocumentTypeDto> getDocumentType(String structure);

}
