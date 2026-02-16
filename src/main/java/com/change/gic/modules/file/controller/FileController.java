package com.change.gic.modules.file.controller;



import com.change.gic.modules.file.dto.FileDto;
import com.change.gic.modules.file.dto.FilesUploadResponse;
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
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/getFileById/{id}")
    public ResponseEntity<FileDto> getFileById(@PathVariable String id){
        return ResponseEntity.ok(fileService.getFileById(id));
    }

    @GetMapping("/getB64FileById/{id}")
    public ResponseEntity<String> getB64FileById(@PathVariable String id){
        return ResponseEntity.ok(fileService.getB64FileById(id));
    }

    @GetMapping("/{url}")
    public ResponseEntity<?> downloadFile(@PathVariable String url) throws IOException {
        ResultFile result = fileService.downloadFile(url);
        return ResponseEntity.ok().contentType(MediaType.valueOf(result.getType())).body(result.getData());
    }

    @PostMapping("/uploadFileToTemp")
    public ResponseEntity<FileDto> uploadFileToTemp(@RequestParam String project, @RequestParam MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        return ResponseEntity.ok(fileService.uploadFileToTemp(project, file, false));
    }

    @PostMapping("/uploadFileToFolder")
    public ResponseEntity<FileDto> uploadFileToFolder(@RequestParam String project, @RequestParam(required = false, defaultValue = "") String path, @RequestParam MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        return ResponseEntity.ok(fileService.uploadFileToFolder(project, path, file));
    }

    @PutMapping("/updateFileOnFolder")
    public ResponseEntity<FileDto> updateFileOnFolder(@RequestParam String id, @RequestParam(required = false, defaultValue = "") String path, @RequestParam MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        return ResponseEntity.ok(fileService.updateFileOnFolder(id, path, file));
    }

    @PostMapping("/uploadFilesToFolder")
    public ResponseEntity<FilesUploadResponse> uploadFilesToFolder(@RequestParam String project, @RequestParam(required = false, defaultValue = "") String path, @RequestParam MultipartFile[] files) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        return ResponseEntity.ok(fileService.uploadFilesToFolder(project, path, files));
    }

    @PostMapping("/uploadFileToDatabase")
    public ResponseEntity<FileDto> uploadFileToDatabase(@RequestParam String project, @RequestParam MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        return ResponseEntity.ok(fileService.uploadFileToDatabase(project, file));
    }

    @PutMapping("/updateFileOnDatabase")
    public ResponseEntity<FileDto> updateFileOnDatabase(@RequestParam String id, @RequestParam MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        return ResponseEntity.ok(fileService.updateFileOnDatabase(id, file));
    }

    @PostMapping("/uploadFilesToDatabase")
    public ResponseEntity<List<FileDto>> uploadFilesToDatabase(@RequestParam String project, @RequestParam MultipartFile[] files) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        return ResponseEntity.ok(fileService.uploadFilesToDatabase(project, files));
    }

    @GetMapping("/getFilesByProject/{project}")
    public ResponseEntity<List<FileDto>> getFilesByProject(@PathVariable String project){
        return ResponseEntity.ok(fileService.getFilesByProject(project));
    }

    @GetMapping("/findAllByProjectAndPath")
    public ResponseEntity<List<FileDto>> findAllByProjectAndPath(@RequestParam(defaultValue = "CUSTOMER_REGISTER", required = false) String project, @RequestParam String path){
        return ResponseEntity.ok(fileService.findAllByProjectAndPath(project, path));
    }

    @DeleteMapping("/deleteFile/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id){
        return ResponseEntity.ok(fileService.deleteFileById(id));
    }

    @DeleteMapping("/deleteFileList")
    public ResponseEntity<Boolean> deleteFileList(@RequestParam Collection<String> fileIds){
        return ResponseEntity.ok(fileService.deleteFileList(fileIds));
    }
}
