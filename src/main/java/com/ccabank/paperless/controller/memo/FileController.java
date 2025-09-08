package com.ccabank.paperless.controller.memo;


import com.ccabank.paperless.security.Authority;
import com.ccabank.paperless.service.faces.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Paperless")
@RestController
@RequestMapping("/paperless")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/files/search")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> search(
            @RequestParam(value = "reference", required = false) String reference,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "staff", required = false) String staff,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size)
           {
        return ResponseEntity.ok(fileService.search(reference, type, staff, page, size));

    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/files/export")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> export(
            @RequestParam(value = "reference", required = false) String reference,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "staff", required = false) String staff
    {

        byte[] excelBytes = fileService.export(reference, type, staff);
        // Configurer l'en-tête HTTP pour le téléchargement
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Export_Request.xlsx");
        headers.setContentLength(excelBytes.length);
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

    }

}
