package com.ccabank.paperless.openfeign;


import com.ccabank.paperless.dto.memo.FileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "FILE-SERVICE")
public interface FileRestClient {
    @PostMapping(path = "/api/files/uploadFileToFolder", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileDto uploadFileToFolder(@RequestParam("project") String project, @RequestParam("path") String path, @RequestPart("file") MultipartFile file);

}
