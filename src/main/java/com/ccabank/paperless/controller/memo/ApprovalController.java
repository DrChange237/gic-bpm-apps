package com.ccabank.paperless.controller.memo;


import com.ccabank.paperless.domain.AppServiceResult;
import com.ccabank.paperless.dto.HttpResponseError;
import com.ccabank.paperless.dto.memo.*;
import com.ccabank.paperless.security.Authority;
import com.ccabank.paperless.service.faces.ApprovalService;
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
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PostMapping("/approval/decision")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> decision(HttpServletRequest request, @RequestBody AcceptedApprovalDto acceptedApprovalDto) {
        AppServiceResult<?> result = approvalService.decision(request, acceptedApprovalDto);
        return result.isSuccess() ? ResponseEntity.ok(result.getData())
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }



    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PostMapping("/approval/take")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> take(HttpServletRequest request, @RequestBody TakeLeaveDto takeLeaveDto) {
        AppServiceResult<?> result = approvalService.freeless(request, takeLeaveDto);
        return result.isSuccess() ? ResponseEntity.ok(result.getData())
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PostMapping("/approval/reassign")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> reassign(@RequestBody ReassignDto reassignDto) {
        AppServiceResult<?> result = approvalService.reassign(reassignDto);
        return result.isSuccess() ? ResponseEntity.ok(result.getData())
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/approval/getDetails")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> getDetails(@RequestParam(value = "id") String id) {
        AppServiceResult<ApprovalListDto> result = approvalService.getApprovalDetail(id);
        return result.isSuccess() ? ResponseEntity.ok(result.getData())
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/approval/follow-up")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> followUp(@RequestParam(value = "id") String id) {
        AppServiceResult<?> result = approvalService.relanceApprobation(id);
        return result.isSuccess() ? ResponseEntity.ok(result.getData())
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/approval/getApprovalByStaffAndStatus")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> getApprovalByStaffAndStatus(HttpServletRequest request, @RequestParam(value = "status") String status) {
        AppServiceResult<List<ApprovalListDto>> result = approvalService.getApprovalByStaff(request, status);
        return result.isSuccess() ? ResponseEntity.ok(result.getData())
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/approval/getAll")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        AppServiceResult<List<ApprovalListDto>> result = approvalService.getAllApprobations(request);
        return result.isSuccess() ? ResponseEntity.ok(result.getData())
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }
}
