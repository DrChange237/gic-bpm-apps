package com.ccabank.memoservice.controller.memo;


import com.ccabank.memoservice.domain.AppBaseResult;
import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.HttpResponse;
import com.ccabank.memoservice.dto.HttpResponseError;
import com.ccabank.memoservice.dto.HttpResponseSuccess;
import com.ccabank.memoservice.dto.memo.RequestDto;
import com.ccabank.memoservice.service.faces.RequestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;

@Api(tags = "Memo")
@RestController
@RequestMapping("/request")
public class MemoController {

    @Autowired
    private RequestService requestService;

    @PostMapping("/new")
    @CrossOrigin()
    public ResponseEntity<HttpResponse> newRequest(@RequestBody RequestDto requestDto) {
        AppBaseResult result = requestService.newRequest(requestDto);
        return result.isSuccess()
                ? ResponseEntity.ok(new HttpResponseSuccess<String>("Request successfully added"))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/validate")
    //@CrossOrigin()
    public ResponseEntity<HttpResponse> validate(@RequestParam(value = "id") Long id) {
        AppBaseResult result = requestService.validateRequest(id);
        return result.isSuccess()
                ? ResponseEntity.ok(new HttpResponseSuccess<String>("Request successfully validated"))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/download")
    //@CrossOrigin()
    public ResponseEntity<InputStreamResource> download(@RequestParam(value = "id") Long id) {

        InputStream input = requestService.downloadRequest(id);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(input));
    }

   @GetMapping("/getRequestByStaffAndStatus")
    //@CrossOrigin()
    public ResponseEntity<HttpResponse> getRequestByStaffAndStatus(@RequestParam(value = "staff") String staff, @RequestParam(value = "status") String status) {
        AppServiceResult<List<RequestDto>> result = requestService.getRequestByStaff(staff, status);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<RequestDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }


}
