package com.ccabank.memoservice.controller.memo;


import com.ccabank.memoservice.dto.HttpResponse;
import com.ccabank.memoservice.dto.HttpResponseError;
import com.ccabank.memoservice.dto.HttpResponseSuccess;
import com.ccabank.memoservice.dto.memo.DocumentTypeDto;
import com.ccabank.memoservice.service.faces.DocumentTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "Paperless")
@RestController
@RequestMapping("/paperless")
public class DocumentTypeController {

    @Autowired
    private DocumentTypeService documentTypeService;


    @GetMapping("/documentType/getTypes")
    public ResponseEntity<?> getTypes() {
        try{
            List<DocumentTypeDto> documentTypeDtos = documentTypeService.getDocumentTypes().getData();
            return ResponseEntity.ok(documentTypeDtos);
        }catch (Exception exception){
            return   ResponseEntity.badRequest().body(new HttpResponseError(null, exception.getMessage()));
        }
    }

    @GetMapping("/documentType/getTypeDetails")
    public ResponseEntity<?> getTypeDetails(@RequestParam(value = "name") String name) {
        try{
            DocumentTypeDto documentTypeDto = documentTypeService.getDocumentType(name).getData();
            return ResponseEntity.ok(documentTypeDto);
        }catch (Exception exception){
            return   ResponseEntity.badRequest().body(new HttpResponseError(null, exception.getMessage()));
        }
    }


}
