package com.ccabank.memoservice.controller.memo;


import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.HttpResponse;
import com.ccabank.memoservice.dto.HttpResponseError;
import com.ccabank.memoservice.dto.HttpResponseSuccess;
import com.ccabank.memoservice.dto.memo.*;
import com.ccabank.memoservice.service.faces.ApprovalService;
import com.ccabank.memoservice.service.faces.DocumentTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Paperless")
@RestController
@RequestMapping("/paperless")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @PostMapping("/approval/decision")
    public ResponseEntity<?> decision(@RequestBody AcceptedApprovalDto acceptedApprovalDto) {
        AppServiceResult<?> result = approvalService.decision(acceptedApprovalDto);
        return result.isSuccess() ? ResponseEntity.ok(result.getData())
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @PostMapping("/approval/reassign")
    public ResponseEntity<?> reassign(@RequestBody ReassignDto reassignDto) {
        AppServiceResult<?> result = approvalService.reassign(reassignDto);
        return result.isSuccess() ? ResponseEntity.ok(result.getData())
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/approval/getDetails")
    public ResponseEntity<?> getDetails(@RequestParam(value = "id") Long id) {
        AppServiceResult<ApprovalDto> result = approvalService.getApprovalDetail(id);
        return result.isSuccess() ? ResponseEntity.ok(result.getData())
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/approval/getApprovalByStaffAndStatus")
    public ResponseEntity<?> getApprovalByStaffAndStatus(@RequestParam(value = "staff") String staff, @RequestParam(value = "status") String status) {
        AppServiceResult<List<ApprovalListDto>> result = approvalService.getApprovalByStaff(staff, status);
        return result.isSuccess() ? ResponseEntity.ok(result.getData())
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }




}
