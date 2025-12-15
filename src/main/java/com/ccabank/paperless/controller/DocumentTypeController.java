package com.ccabank.paperless.controller;


import com.ccabank.paperless.dto.memo.DocumentTypeDto;
import com.ccabank.paperless.service.faces.DocumentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/paperless")
@RequiredArgsConstructor
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;


    @GetMapping("/documentType/getTypes")
    //@PreAuthorize(Authority.DocumentType.VIEWALL_DOCUMENT_TYPE)
    public ResponseEntity<List<DocumentTypeDto>> getTypes() {
        return ResponseEntity.ok(documentTypeService.getDocumentTypes());
    }

    @GetMapping("/documentType/getTypeDetails")
    //@PreAuthorize(Authority.DocumentType.VIEW_DOCUMENT_TYPE)
    public ResponseEntity<DocumentTypeDto> getTypeDetails(@RequestParam String name) {
        return ResponseEntity.ok(documentTypeService.getDocumentType(name));
    }


}
