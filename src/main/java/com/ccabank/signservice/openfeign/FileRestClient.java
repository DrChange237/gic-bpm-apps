package com.ccabank.signservice.openfeign;


import com.ccabank.signservice.dto.sign.FileDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "FILE-SERVICE", configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface FileRestClient {
    @GetMapping(path = "/api/files/getFileById/{id}")
    FileDto getFileById(String id);

    @PostMapping(path="/api/files/uploadFileToFolder")
    FileDto uploadFileToFolder(@RequestParam("project") String project, @RequestParam(value = "path", required = false, defaultValue = "") String path, @RequestParam("file") MultipartFile file);
}
