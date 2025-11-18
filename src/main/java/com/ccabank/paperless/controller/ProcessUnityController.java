package com.ccabank.paperless.controller;

import com.ccabank.paperless.dto.memo.ProcessUnityDto;
import com.ccabank.paperless.service.faces.ProcessUnityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Paperless")
@RestController
@RequestMapping("/paperless")
public class ProcessUnityController {

    @Autowired
    private ProcessUnityService processUnityService;

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/processUnity/getAll")
    //@PreAuthorize(Authority.ProcessUnity.VIEWALL_PROCESSUNITY)
    public ResponseEntity<List<ProcessUnityDto>> getAll() {
        return ResponseEntity.ok(processUnityService.getAll());
    }


    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/processUnity/getDetails")
    //@PreAuthorize(Authority.ProcessUnity.VIEW_PROCESSUNITY)
    public ResponseEntity<ProcessUnityDto> getTypeDetails(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(processUnityService.getDetail(id));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PostMapping("/processUnity/create")
    //@PreAuthorize(Authority.ProcessUnity.ADD_PROCESSUNITY)
    public ResponseEntity<?> create(@RequestBody ProcessUnityDto processUnityDto) {
        AppBaseResult result = processUnityService.create(processUnityDto);
        return ResponseEntity.ok(new HttpResponseSuccess<String>("Process Unity successfully added"));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PutMapping("/processUnity/update")
    //@PreAuthorize(Authority.ProcessUnity.UPDATE_PROCESSUNITY)
    public ResponseEntity<?> update(@RequestBody ProcessUnityDto processUnityDto) {
        AppBaseResult result = processUnityService.update(processUnityDto);
        return ResponseEntity.ok(new HttpResponseSuccess<String>("Process Unity successfully updated"));
    }

}
