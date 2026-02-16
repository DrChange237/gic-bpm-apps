package com.change.gic.modules.file.controller;



import com.change.gic.modules.file.dto.FileDto;
import com.change.gic.modules.file.dto.ResultFile;
import com.change.gic.modules.file.exception.MaxFileSizeException;
import com.change.gic.modules.file.exception.UnsupportedFileTypeException;
import com.change.gic.modules.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/files/exposed")
@RequiredArgsConstructor
public class ExposedFileController {
    private final FileService fileService;

    @GetMapping("/{url}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String url) throws IOException {
        ResultFile result = fileService.downloadFile(url);
        return ResponseEntity.ok().contentType(MediaType.valueOf(result.getType())).body(result.getData());
    }

    @PostMapping("/uploadFileToTemp")
    public ResponseEntity<FileDto> uploadFileToTemp(@RequestParam String project, @RequestParam MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        return ResponseEntity.ok(fileService.uploadFileToTemp(project, file, true));
    }
}
