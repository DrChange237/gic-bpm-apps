package com.ccabank.memoservice.controller.memo;


import com.ccabank.memoservice.domain.AppBaseResult;
import com.ccabank.memoservice.dto.HttpResponse;
import com.ccabank.memoservice.dto.HttpResponseError;
import com.ccabank.memoservice.dto.HttpResponseSuccess;
import com.ccabank.memoservice.dto.memo.DocumentTypeDto;
import com.ccabank.memoservice.dto.memo.ProcessUnityDto;
import com.ccabank.memoservice.dto.memo.RequestDto;
import com.ccabank.memoservice.service.faces.ProcessUnityService;
import com.ccabank.memoservice.service.faces.RequestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Paperless")
@RestController
@RequestMapping("/process-unity")
public class ProcessUnityController {

    @Autowired
    private ProcessUnityService processUnityService;


    @GetMapping("/getAll")
    @CrossOrigin()
    public ResponseEntity<HttpResponse> getAll() {
        try{
            List<ProcessUnityDto> processUnityDtos = processUnityService.getAll().getData();
            return ResponseEntity.ok(new HttpResponseSuccess<List<ProcessUnityDto>>(processUnityDtos));
        }catch (Exception exception){
            return   ResponseEntity.badRequest().body(new HttpResponseError(null, exception.getMessage()));

        }
    }


    @GetMapping("/getDetails")
    public ResponseEntity<HttpResponse> getTypeDetails(@RequestParam(value = "id") Long id) {

        try{
            ProcessUnityDto processUnityDto = processUnityService.getDetail(id).getData();
            return ResponseEntity.ok(new HttpResponseSuccess<ProcessUnityDto>(processUnityDto));
        }catch (Exception exception){
            return   ResponseEntity.badRequest().body(new HttpResponseError(null, exception.getMessage()));

        }
    }


    @PostMapping("/create")
    @CrossOrigin()
    public ResponseEntity<HttpResponse> create(@RequestBody ProcessUnityDto processUnityDto) {
        AppBaseResult result = processUnityService.create(processUnityDto);
        return result.isSuccess()
                ? ResponseEntity.ok(new HttpResponseSuccess<String>("Process Unity successfully added"))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @PostMapping("/update")
    @CrossOrigin()
    public ResponseEntity<HttpResponse> update(@RequestBody ProcessUnityDto processUnityDto) {
        AppBaseResult result = processUnityService.update(processUnityDto);
        return result.isSuccess()
                ? ResponseEntity.ok(new HttpResponseSuccess<String>("Process Unity successfully updated"))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

}
