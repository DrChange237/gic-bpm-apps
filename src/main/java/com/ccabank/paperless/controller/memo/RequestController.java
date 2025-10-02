package com.ccabank.paperless.controller.memo;


import com.ccabank.paperless.domain.AppBaseResult;
import com.ccabank.paperless.domain.AppServiceResult;
import com.ccabank.paperless.dto.HttpResponseError;
import com.ccabank.paperless.dto.HttpResponseSuccess;
import com.ccabank.paperless.dto.memo.ArchivageDto;
import com.ccabank.paperless.dto.memo.RequestDto;
import com.ccabank.paperless.dto.memo.RequestInfo;
import com.ccabank.paperless.security.Authority;
import com.ccabank.paperless.service.faces.RequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "Paperless")
@RestController
@RequestMapping("/paperless")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PostMapping("/request/create")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> newRequest(HttpServletRequest request, @RequestBody RequestDto requestDto) {
        AppBaseResult result = requestService.newRequest(requestDto, request);
        return ResponseEntity.ok(new HttpResponseSuccess<String>("Request successfully added"));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PutMapping("/request/update")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> update(@RequestBody RequestDto requestDto) {
        AppBaseResult result = requestService.update(requestDto);
        return result.isSuccess()
                ? ResponseEntity.ok(new HttpResponseSuccess<String>("Request successfully added"))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/details")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> details(@RequestParam(value = "id") Long id) {
        AppServiceResult<RequestInfo> result = requestService.details(id);
        return result.isSuccess()
                ? ResponseEntity.ok(new HttpResponseSuccess<RequestInfo>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/detailForUpdate")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> detailForUpdate(@RequestParam(value = "id") Long id) {
        AppServiceResult<RequestInfo> result = requestService.detailForUpdate(id);
        return result.isSuccess()
                ? ResponseEntity.ok(new HttpResponseSuccess<RequestInfo>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/suspend")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> suspend(@RequestParam(value = "id") Long id) {
        AppServiceResult<RequestInfo> result = requestService.suspend(id, "Auto-Suspension");
        return result.isSuccess()
                ? ResponseEntity.ok(new HttpResponseSuccess<RequestInfo>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PostMapping("/request/archived")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> archived(@RequestBody ArchivageDto archivageDto) {
        AppServiceResult<RequestInfo> result = requestService.achivage(archivageDto);
        return ResponseEntity.ok(new HttpResponseSuccess<RequestInfo>(result.getData()));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/validate")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> validate(@RequestParam(value = "id") Long id)  {
        AppBaseResult result = requestService.validateRequest(id);
        return ResponseEntity.ok(new HttpResponseSuccess<String>("Request successfully validated"));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/download")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> download(@RequestParam(value = "id") Long id) {
        AppBaseResult result = requestService.download(id);
        return ResponseEntity.ok(new HttpResponseSuccess<String>("Request successfully downloaded"));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/getRequestByStaffAndStatus")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> getRequestByStaffAndStatus(@RequestParam(value = "staff") String staff, @RequestParam(value = "status") String status) {
        AppServiceResult<List<RequestInfo>> result = requestService.getRequestByStaff(staff, status);
        return ResponseEntity.ok(result.getData());
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/getByReference")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> getByReference(@RequestParam(value = "reference") String reference) {
        AppServiceResult<RequestInfo> result = requestService.getRequestByReference(reference);
        return ResponseEntity.ok(result.getData());
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/getAll")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        AppServiceResult<List<RequestInfo>> result = requestService.getRequestAll(request);
        return ResponseEntity.ok(result.getData());
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/request/getHistory")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> getHistory(HttpServletRequest request) {
        AppServiceResult<List<RequestInfo>> result = requestService.getRequestHistory(request);
        return ResponseEntity.ok(result.getData());
    }


}
