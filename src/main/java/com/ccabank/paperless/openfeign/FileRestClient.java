package com.ccabank.paperless.openfeign;


import com.ccabank.paperless.config.FeignClientConfiguration;
import com.ccabank.paperless.constant.FeignHeader;
import com.ccabank.paperless.dto.memo.FileDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "FILE-SERVICE", configuration = FeignClientConfiguration.class)
public interface FileRestClient {

    @GetMapping(path = "/api/files/getB64FileById/{id}")
    String getB64FileById(@PathVariable("id") String id);

    @PostMapping(path = "/api/files/uploadFileToFolder", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileDto uploadFileToFolder(@RequestParam("project") String project, @RequestParam("path") String path, @RequestPart("file") MultipartFile file);

}
