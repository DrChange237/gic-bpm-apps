package com.ccabank.memoservice.controller.memo;


import com.ccabank.memoservice.dto.HttpResponse;
import com.ccabank.memoservice.dto.HttpResponseError;
import com.ccabank.memoservice.dto.HttpResponseSuccess;
import com.ccabank.memoservice.dto.memo.DocumentTypeDto;
import com.ccabank.memoservice.service.faces.DocumentTypeService;
import com.ccabank.memoservice.service.faces.RequestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@Api(tags = "Paperless")
@RestController
@RequestMapping("/document-type")
public class DocumentTypeController {

    @Autowired
    private DocumentTypeService documentTypeService;

    @GetMapping("/getTypes")
    public ResponseEntity<HttpResponse> getTypes() {

        try{
            List<DocumentTypeDto> documentTypeDtos = documentTypeService.getDocumentTypes().getData();
            return ResponseEntity.ok(new HttpResponseSuccess<List<DocumentTypeDto>>(documentTypeDtos));
        }catch (Exception exception){
            return   ResponseEntity.badRequest().body(new HttpResponseError(null, exception.getMessage()));

        }
    }

    @GetMapping("/getTypeDetails")
    public ResponseEntity<HttpResponse> getTypeDetails(@RequestParam(value = "name") String name) {

        try{
            DocumentTypeDto documentTypeDto = documentTypeService.getDocumentType(name).getData();
            return ResponseEntity.ok(new HttpResponseSuccess<DocumentTypeDto>(documentTypeDto));
        }catch (Exception exception){
            return   ResponseEntity.badRequest().body(new HttpResponseError(null, exception.getMessage()));

        }
    }


}
