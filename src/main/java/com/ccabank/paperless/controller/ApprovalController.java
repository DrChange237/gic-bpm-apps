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
    public void decision(HttpServletRequest request, @RequestBody AcceptedApprovalDto acceptedApprovalDto) {
        approvalService.decision(request, acceptedApprovalDto);
    }

    @PostMapping("/approval/take")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public void take(HttpServletRequest request, @RequestBody TakeLeaveDto takeLeaveDto) {
        approvalService.freeless(request, takeLeaveDto);
    }

    @PostMapping("/approval/reassign")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public void reassign(@RequestBody ReassignDto reassignDto) {
        approvalService.reassign(reassignDto);
    }

    @GetMapping("/approval/getDetails")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<ApprovalListDto> getDetails(@RequestParam(value = "id") String id) {
        return ResponseEntity.ok(approvalService.getApprovalDetail(id));
    }

    @GetMapping("/approval/follow-up")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public void followUp(@RequestParam(value = "id") String id) {
        approvalService.relanceApprobation(id);
    }

    @GetMapping("/approval/getApprovalByStaffAndStatus")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> getApprovalByStaffAndStatus(HttpServletRequest request, @RequestParam(value = "status") String status) {
        return ResponseEntity.ok(approvalService.getApprovalByStaff(request, status));
    }

    @GetMapping("/approval/getAll")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<List<ApprovalListDto>> getAll(HttpServletRequest request) {
        return ResponseEntity.ok(approvalService.getAllApprobations(request));
    }
}
