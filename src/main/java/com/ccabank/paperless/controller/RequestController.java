package com.ccabank.paperless.controller;


import com.ccabank.paperless.dto.memo.ArchivageDto;
import com.ccabank.paperless.dto.memo.RequestDto;
import com.ccabank.paperless.dto.memo.RequestInfo;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.security.Authority;
import com.ccabank.paperless.service.faces.RequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "Paperless")
@RestController
@RequiredArgsConstructor
@RequestMapping("/paperless")
public class RequestController {

    private final RequestService requestService;

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PostMapping("/request/create")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public void newRequest(HttpServletRequest request, @RequestBody RequestDto requestDto) {
        requestService.newRequest(requestDto, request);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PutMapping("/request/update")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public void update(@RequestBody RequestDto requestDto) {
        requestService.update(requestDto);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/details")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<RequestInfo> details(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(requestService.details(id));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/detailForUpdate")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<RequestInfo> detailForUpdate(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(requestService.detailForUpdate(id));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/suspend")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<RequestInfo> suspend(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(requestService.suspend(id, "Auto-Suspension"));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PostMapping("/request/archived")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<RequestInfo> archived(@RequestBody ArchivageDto archivageDto) {
        return ResponseEntity.ok(requestService.achivage(archivageDto));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/validate")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public void validate(@RequestParam(value = "id") Long id)  {
        requestService.validateRequest(id);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/download")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<Request> download(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(requestService.download(id));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/getRequestByStaffAndStatus")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<List<RequestInfo>> getRequestByStaffAndStatus(@RequestParam(value = "staff") String staff, @RequestParam(value = "status") String status) {
        return ResponseEntity.ok(requestService.getRequestByStaff(staff, status));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/getByReference")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<RequestInfo> getByReference(@RequestParam(value = "reference") String reference) {
        return ResponseEntity.ok(requestService.getRequestByReference(reference));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/getAll")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<List<RequestInfo>> getAll(HttpServletRequest request) {
        return ResponseEntity.ok(requestService.getRequestAll(request));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/getHistory")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<List<RequestInfo>> getHistory(HttpServletRequest request) {
        return ResponseEntity.ok(requestService.getRequestHistory(request));
    }


}
