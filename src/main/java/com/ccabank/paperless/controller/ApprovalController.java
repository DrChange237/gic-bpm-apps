package com.ccabank.paperless.controller;


import com.ccabank.paperless.dto.memo.*;
import com.ccabank.paperless.security.Authority;
import com.ccabank.paperless.service.faces.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/paperless")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping("/approval/decision")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> decision(HttpServletRequest request, @RequestBody AcceptedApprovalDto acceptedApprovalDto) {
        AppServiceResult<?> result = approvalService.decision(request, acceptedApprovalDto);
        return ResponseEntity.ok(result.getData());
    }



    @PostMapping("/approval/take")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> take(HttpServletRequest request, @RequestBody TakeLeaveDto takeLeaveDto) {
        AppServiceResult<?> result = approvalService.freeless(request, takeLeaveDto);
        return ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @PostMapping("/approval/reassign")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> reassign(@RequestBody ReassignDto reassignDto) {
        AppServiceResult<?> result = approvalService.reassign(reassignDto);
        return ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/approval/getDetails")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> getDetails(@RequestParam(value = "id") String id) {
        AppServiceResult<ApprovalListDto> result = approvalService.getApprovalDetail(id);
        return ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/approval/follow-up")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> followUp(@RequestParam(value = "id") String id) {
        AppServiceResult<?> result = approvalService.relanceApprobation(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/approval/getApprovalByStaffAndStatus")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> getApprovalByStaffAndStatus(HttpServletRequest request, @RequestParam(value = "status") String status) {
        AppServiceResult<List<ApprovalListDto>> result = approvalService.getApprovalByStaff(request, status);
        return ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/approval/getAll")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        AppServiceResult<List<ApprovalListDto>> result = approvalService.getAllApprobations(request);
        return ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }
}
