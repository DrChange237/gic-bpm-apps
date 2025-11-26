package com.ccabank.paperless.controller;

import com.ccabank.paperless.dto.memo.ArchivageDto;
import com.ccabank.paperless.dto.memo.RequestDto;
import com.ccabank.paperless.dto.memo.RequestInfo;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.security.Authority;
import com.ccabank.paperless.service.faces.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paperless")
public class RequestController {

    private final RequestService requestService;

    @PostMapping("/request/create")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public void newRequest(HttpServletRequest request, @RequestBody RequestDto requestDto) {
        requestService.newRequest(requestDto, request);
    }

    @PutMapping("/request/update")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public void update(@RequestBody RequestDto requestDto) {
        requestService.update(requestDto);
    }

    @GetMapping("/request/details")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> details(@RequestParam(value = "id") Long id) {
        HashMap<String, Object> details = new HashMap<>();
        details.put("data", requestService.details(id));
        return ResponseEntity.ok(details);
    }

    @GetMapping("/request/detailForUpdate")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<RequestInfo> detailForUpdate(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(requestService.detailForUpdate(id));
    }

    @GetMapping("/request/suspend")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<RequestInfo> suspend(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(requestService.suspend(id, "Auto-Suspension"));
    }

    @PostMapping("/request/archived")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<RequestInfo> archived(@RequestBody ArchivageDto archivageDto) {
        return ResponseEntity.ok(requestService.achivage(archivageDto));
    }

    @GetMapping("/request/validate")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public void validate(@RequestParam(value = "id") Long id)  {
        requestService.validateRequest(id);
    }

    @GetMapping("/request/download")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<Request> download(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(requestService.download(id));
    }

    @GetMapping("/request/getRequestByStaffAndStatus")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<List<RequestInfo>> getRequestByStaffAndStatus(@RequestParam(value = "staff") String staff, @RequestParam(value = "status") String status) {
        return ResponseEntity.ok(requestService.getRequestByStaff(staff, status));
    }

    @GetMapping("/request/getByReference")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<RequestInfo> getByReference(@RequestParam(value = "reference") String reference) {
        return ResponseEntity.ok(requestService.getRequestByReference(reference));
    }

    @GetMapping("/request/getAll")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        HashMap<String, Object> details = new HashMap<>();
        details.put("data", requestService.getRequestAll(request));
        return ResponseEntity.ok(details);
    }

    @GetMapping("/request/getHistory")
    @PreAuthorize(Authority.IS_AUTHENTICATED)
    public ResponseEntity<List<RequestInfo>> getHistory(HttpServletRequest request) {
        return ResponseEntity.ok(requestService.getRequestHistory(request));
    }
}
