package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.dto.memo.DocumentTypeDto;

import java.util.List;

public interface DocumentTypeService {
    List<DocumentTypeDto> getDocumentTypes();

    DocumentTypeDto getDocumentType(String structure);

}
