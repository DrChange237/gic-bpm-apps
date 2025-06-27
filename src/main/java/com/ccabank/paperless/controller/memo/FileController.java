package com.ccabank.paperless.controller.memo;


import com.ccabank.paperless.security.Authority;
import com.ccabank.paperless.service.faces.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
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

}
