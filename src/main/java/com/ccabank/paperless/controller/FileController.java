package com.ccabank.paperless.controller;


import com.ccabank.paperless.security.Authority;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.service.faces.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paperless")
@RequiredArgsConstructor
public class FileController {


    private final FileService fileService;
    private final RequestService requestService;

    @GetMapping("/files/search")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> search(
            @RequestParam(value = "reference", required = false) String reference,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "staff", required = false) String staff,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size)
           {
        return ResponseEntity.ok(fileService.search(reference, type, staff, startDate, endDate, page, size));

    }

    @GetMapping("/files/export")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> export(
            @RequestParam(value = "reference", required = false) String reference,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "staff", required = false) String staff,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate)
    {

        byte[] excelBytes = requestService.export(reference, type, staff, startDate, endDate);
        // Configurer l'en-tête HTTP pour le téléchargement
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Export_Request_" + type  +".xlsx");
        headers.setContentLength(excelBytes.length);
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

    }

}
