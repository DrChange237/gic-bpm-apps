package com.ccabank.memoservice.controller.memo;


import com.ccabank.memoservice.domain.AppBaseResult;
import com.ccabank.memoservice.dto.HttpResponse;
import com.ccabank.memoservice.dto.HttpResponseError;
import com.ccabank.memoservice.dto.HttpResponseSuccess;
import com.ccabank.memoservice.dto.memo.DocumentTypeDto;
import com.ccabank.memoservice.dto.memo.ProcessUnityDto;
import com.ccabank.memoservice.dto.memo.RequestDto;
import com.ccabank.memoservice.security.Authority;
import com.ccabank.memoservice.service.faces.ProcessUnityService;
import com.ccabank.memoservice.service.faces.RequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<?> getAll() {
        try{
            List<ProcessUnityDto> processUnityDtos = processUnityService.getAll().getData();
            return ResponseEntity.ok(processUnityDtos);
        }catch (Exception exception){
            return   ResponseEntity.badRequest().body(new HttpResponseError(null, exception.getMessage()));

        }
    }


    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @GetMapping("/processUnity/getDetails")
    //@PreAuthorize(Authority.ProcessUnity.VIEW_PROCESSUNITY)
    public ResponseEntity<?> getTypeDetails(@RequestParam(value = "id") Long id) {

        try{
            ProcessUnityDto processUnityDto = processUnityService.getDetail(id).getData();
            return ResponseEntity.ok(processUnityDto);
        }catch (Exception exception){
            return   ResponseEntity.badRequest().body(new HttpResponseError(null, exception.getMessage()));

        }
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PostMapping("/processUnity/create")
    //@PreAuthorize(Authority.ProcessUnity.ADD_PROCESSUNITY)
    public ResponseEntity<?> create(@RequestBody ProcessUnityDto processUnityDto) {
        AppBaseResult result = processUnityService.create(processUnityDto);
        return result.isSuccess()
                ? ResponseEntity.ok(new HttpResponseSuccess<String>("Process Unity successfully added"))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer <access-token>")})
    @PutMapping("/processUnity/update")
    //@PreAuthorize(Authority.ProcessUnity.UPDATE_PROCESSUNITY)
    public ResponseEntity<?> update(@RequestBody ProcessUnityDto processUnityDto) {
        AppBaseResult result = processUnityService.update(processUnityDto);
        return result.isSuccess()
                ? ResponseEntity.ok(new HttpResponseSuccess<String>("Process Unity successfully updated"))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

}
