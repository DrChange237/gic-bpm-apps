package com.ccabank.paperless.controller;

import com.ccabank.paperless.dto.memo.ProcessUnityDto;
import com.ccabank.paperless.service.faces.ProcessUnityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paperless")
@RequiredArgsConstructor
public class ProcessUnityController {

    private final ProcessUnityService processUnityService;

    @GetMapping("/processUnity/getAll")
    //@PreAuthorize(Authority.ProcessUnity.VIEWALL_PROCESSUNITY)
    public ResponseEntity<List<ProcessUnityDto>> getAll() {
        return ResponseEntity.ok(processUnityService.getAll());
    }


    @GetMapping("/processUnity/getDetails")
    //@PreAuthorize(Authority.ProcessUnity.VIEW_PROCESSUNITY)
    public ResponseEntity<ProcessUnityDto> getTypeDetails(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(processUnityService.getDetail(id));
    }

    @PostMapping("/processUnity/create")
    //@PreAuthorize(Authority.ProcessUnity.ADD_PROCESSUNITY)
    public void create(@RequestBody ProcessUnityDto processUnityDto) {
        processUnityService.create(processUnityDto);
    }

    @PutMapping("/processUnity/update")
    //@PreAuthorize(Authority.ProcessUnity.UPDATE_PROCESSUNITY)
    public void update(@RequestBody ProcessUnityDto processUnityDto) {
        processUnityService.update(processUnityDto);
    }

}
