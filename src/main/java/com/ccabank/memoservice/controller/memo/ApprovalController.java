package com.ccabank.memoservice.controller.memo;


import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.HttpResponse;
import com.ccabank.memoservice.dto.HttpResponseError;
import com.ccabank.memoservice.dto.HttpResponseSuccess;
import com.ccabank.memoservice.dto.memo.ApprovalDto;
import com.ccabank.memoservice.dto.memo.RequestDto;
import com.ccabank.memoservice.service.faces.ApprovalService;
import com.ccabank.memoservice.service.faces.DocumentTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "Memo")
@RestController
@RequestMapping("/approval")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @GetMapping("/accepted")
    //@CrossOrigin()
    public ResponseEntity<HttpResponse> accepted(@RequestParam(value = "id") Long id) {
        AppServiceResult<ApprovalDto> result = approvalService.approve(id);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<ApprovalDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/rejected")
    //@CrossOrigin()
    public ResponseEntity<HttpResponse> rejected(@RequestParam(value = "id") Long id) {
        AppServiceResult<ApprovalDto> result = approvalService.rejected(id);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<ApprovalDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/getApprovalByStaffAndStatus")
    //@CrossOrigin()
    public ResponseEntity<HttpResponse> getRequestByStaffAndStatus(@RequestParam(value = "staff") String staff, @RequestParam(value = "status") String status) {
        AppServiceResult<List<ApprovalDto>> result = approvalService.getApprovalByStaff(staff, status);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<ApprovalDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }




}
