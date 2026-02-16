package com.change.gic.modules.file.service;


import com.change.gic.modules.file.dto.FileDto;
import com.change.gic.modules.file.dto.FilesUploadResponse;
import com.change.gic.modules.file.dto.ResultFile;
import com.change.gic.modules.file.exception.MaxFileSizeException;
import com.change.gic.modules.file.exception.UnsupportedFileTypeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface FileService {
    FileDto uploadFileToTemp(String project, MultipartFile file, Boolean exposed) throws UnsupportedFileTypeException, MaxFileSizeException, IOException;
    FileDto uploadFileToFolder(String project, String customer, MultipartFile file) throws IOException, UnsupportedFileTypeException, MaxFileSizeException;
    FileDto updateFileOnFolder(String fileId, String customer, MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException, IOException;
    FilesUploadResponse uploadFilesToFolder(String project, String customer, MultipartFile[] files) throws UnsupportedFileTypeException, MaxFileSizeException, IOException;
    FileDto uploadFileToDatabase(String project, MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException, IOException;
    FileDto updateFileOnDatabase(String fileId, MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException, IOException;
    List<FileDto> uploadFilesToDatabase(String project, MultipartFile[] files) throws UnsupportedFileTypeException, MaxFileSizeException, IOException;

    FileDto getFileById(String fileId);

    ResultFile downloadFile(String url) throws IOException;
    List<FileDto> getFilesByProject(String project);

    String getB64FileById(String id);

    Boolean deleteFileById(String id);

    Boolean deleteFileList(Collection<String> fileIds);

    List<FileDto> findAllByProjectAndPath(String project, String path);
}
