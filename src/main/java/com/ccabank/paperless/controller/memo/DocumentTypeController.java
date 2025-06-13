package com.ccabank.paperless.controller.memo;


import com.ccabank.paperless.dto.HttpResponseError;
import com.ccabank.paperless.dto.memo.DocumentTypeDto;
import com.ccabank.paperless.service.faces.DocumentTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    //@PreAuthorize(Authority.DocumentType.VIEWALL_DOCUMENT_TYPE)
    public ResponseEntity<?> getTypes() {
        try{
            List<DocumentTypeDto> documentTypeDtos = documentTypeService.getDocumentTypes().getData();
            return ResponseEntity.ok(documentTypeDtos);
        }catch (Exception exception){
            return   ResponseEntity.badRequest().body(new HttpResponseError(null, exception.getMessage()));
        }
    }

    @GetMapping("/documentType/getTypeDetails")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    //@PreAuthorize(Authority.DocumentType.VIEW_DOCUMENT_TYPE)
    public ResponseEntity<?> getTypeDetails(@RequestParam(value = "name") String name) {
        try{
            DocumentTypeDto documentTypeDto = documentTypeService.getDocumentType(name).getData();
            return ResponseEntity.ok(documentTypeDto);
        }catch (Exception exception){
            return   ResponseEntity.badRequest().body(new HttpResponseError(null, exception.getMessage()));
        }
    }


}
