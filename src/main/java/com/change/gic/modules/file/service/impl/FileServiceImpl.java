package com.change.gic.modules.file.service.impl;


import com.change.gic.modules.file.dto.FileDto;
import com.change.gic.modules.file.dto.FilesUploadResponse;
import com.change.gic.modules.file.dto.ResultFile;
import com.change.gic.modules.file.entity.File;
import com.change.gic.modules.file.exception.MaxFileSizeException;
import com.change.gic.modules.file.exception.NotFoundException;
import com.change.gic.modules.file.exception.UnsupportedFileTypeException;
import com.change.gic.modules.file.mapper.FileMapper;
import com.change.gic.modules.file.repository.FileRepository;
import com.change.gic.modules.file.service.FileService;
import com.change.gic.modules.file.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileMapper fileMapper;

    @Value("${base-folder}")
    private String BASE_FOLDER;

    @Override
    public FileDto getFileById(String fileId) {
        logger.info(" getFileById");
        File fileFromDatabase = fileRepository.findById(fileId).orElseThrow(() -> new NotFoundException("file not found"));
        return fileMapper.toDto(fileFromDatabase);
    }

    @Override
    public ResultFile downloadFile(String url) throws IOException {
        logger.info(" Downloading file : method invocation");
        File file = fileRepository.findById(url).orElseThrow(() -> new NotFoundException("File not found : " + url));
        String path = file.getPath();
        List<String> prefixes = List.of("E:\\FILE-SERVICE\\FOLDERS\\", "C:\\Windows\\system32\\config\\systemprofile\\FOLDERS\\");
        if(path != null && prefixes.stream().anyMatch(path::contains)){
            for(String prefix : prefixes){
                path = path.replace(prefix, "");
            }
            path = path.replace("\\", "/");
            file.setPath(path);
            fileRepository.save(file);
        }
        ResultFile result = new ResultFile();
        if(file.getIsInFolder()){
            java.io.File fileInFolder = FileUtil.getFileInFolder(BASE_FOLDER + "/" + file.getPath());
            if(fileInFolder.exists()){
                result.setType(file.getType());
                Resource resource = new UrlResource(fileInFolder.toURI());
                result.setData(resource.getInputStream().readAllBytes());
                return result;
            } else {
                throw new NotFoundException("File not found : " + file.getPath());
            }
        } else {
            result.setType(file.getType());
            result.setData(FileUtil.decompressFile(file.getData()));
            return result;
        }
    }

    @Override
    public List<FileDto> getFilesByProject(String project) {
       return fileRepository.findAllByProject(project)
                .stream().map(file -> fileMapper.toDto(file))
                .collect(Collectors.toList());
    }

    @Override
    public FileDto uploadFileToTemp(String project, MultipartFile file, Boolean exposed) throws UnsupportedFileTypeException, MaxFileSizeException, IOException, UnsupportedFileTypeException, MaxFileSizeException {
        logger.info(" uploadFileToTemp");
        FileUtil.checkIfCanUpload(file);
        File fileToSave = FileUtil.buildFileToSaveToTemp(project, file);
        fileRepository.save(fileToSave);
        fileToSave.setUrl(fileToSave.getId());
        fileRepository.save(fileToSave);
        return fileMapper.toDto(fileToSave);
    }

    @Override
    public FileDto uploadFileToFolder(String project, String path, MultipartFile file) throws IOException, UnsupportedFileTypeException, MaxFileSizeException {
        logger.info(" uploadFileToFolder");
        FileUtil.checkIfCanUpload(file);
        File fileToSave = FileUtil.buildFileToSaveToFolder(project, path, file, BASE_FOLDER);
        fileRepository.save(fileToSave);
        fileToSave.setUrl(fileToSave.getId());
        fileRepository.save(fileToSave);
        return fileMapper.toDto(fileToSave);
    }

    @Override
    public FileDto updateFileOnFolder(String fileId, String path, MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        logger.info(" updateFileOnFolder");
        File fileFromDatabase = fileRepository.findById(fileId).orElseThrow(() -> new NotFoundException("File not found : " + fileId));
        FileUtil.checkIfCanUpload(file);
        File fileToSave = FileUtil.buildFileToUpdateToFolder(fileFromDatabase, path, file, BASE_FOLDER);
        fileRepository.save(fileToSave);
        return fileMapper.toDto(fileFromDatabase);
    }

    @Override
    public FilesUploadResponse uploadFilesToFolder(String project, String path, MultipartFile[] files) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        List<FileDto> result = new ArrayList<>();
        for (MultipartFile file : files) {
            if(file == null) continue;
            FileUtil.checkIfCanUpload(file);
        }
        for(MultipartFile file : files){
            File fileToSave = FileUtil.buildFileToSaveToFolder(project, path, file, BASE_FOLDER);
            fileRepository.save(fileToSave);
            fileToSave.setUrl(fileToSave.getId());
            fileRepository.save(fileToSave);
            logger.info(fileToSave.toString());
            result.add(fileMapper.toDto(fileToSave));
        }

        FilesUploadResponse response = new FilesUploadResponse();
        response.setResult(result);
        return response;
    }

    @Override
    public FileDto uploadFileToDatabase(String project, MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        logger.info(" uploadFileToDatabase");
        FileUtil.checkIfCanUpload(file);
        File fileToSave = FileUtil.buildFileToSaveToDatabase(project, file);
        fileRepository.save(fileToSave);
        fileToSave.setUrl(fileToSave.getId());
        fileRepository.save(fileToSave);
        return fileMapper.toDto(fileToSave);
    }

    @Override
    public FileDto updateFileOnDatabase(String fileId, MultipartFile file) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        logger.info(" updateFileOnDatabase");
        File fileFromDatabase = fileRepository.findById(fileId).orElseThrow(() -> new NotFoundException("File not found : " + fileId));
        FileUtil.checkIfCanUpload(file);
        fileFromDatabase.setType(file.getContentType());
        fileFromDatabase.setSize(file.getSize());
        fileFromDatabase.setData(FileUtil.compressFile(file.getBytes()));
        fileRepository.save(fileFromDatabase);
        return fileMapper.toDto(fileFromDatabase);
    }

    @Override
    public List<FileDto> uploadFilesToDatabase(String project, MultipartFile[] files) throws UnsupportedFileTypeException, MaxFileSizeException, IOException {
        logger.info(" uploadFilesToDatabase");
        List<FileDto> result = new ArrayList<>();
        for(MultipartFile file : files) {
            FileUtil.checkIfCanUpload(file);
        }
        for(MultipartFile file : files) {
            File fileToSave = FileUtil.buildFileToSaveToDatabase(project, file);
            fileRepository.save(fileToSave);
            fileToSave.setUrl(fileToSave.getId());
            fileRepository.save(fileToSave);
            result.add(fileMapper.toDto(fileToSave));
        }
        return result;
    }

    @Override
    public String getB64FileById(String id) {
        File fileFromDatabase = fileRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found : " + id));

        String path = fileFromDatabase.getPath();
        List<String> prefixes = List.of("E:\\FILE-SERVICE\\FOLDERS\\", "C:\\Windows\\system32\\config\\systemprofile\\FOLDERS\\");
        if(path != null && prefixes.stream().anyMatch(path::contains)){
            for(String prefix : prefixes){
                path = path.replace(prefix, "");
            }
            path = path.replace("\\", "/");
            fileFromDatabase.setPath(path);
            fileRepository.save(fileFromDatabase);
        }

        if(fileFromDatabase.getIsInFolder()){
            logger.info(" getB64FileById {}", fileFromDatabase);
            java.io.File fileInFolder = FileUtil.getFileInFolder(BASE_FOLDER + "/" + fileFromDatabase.getPath());
            if(fileInFolder.exists()){
                return FileUtil.getBase64FromFile(fileInFolder.getPath());
            } else {
                throw new NotFoundException("File not found : " + fileFromDatabase.getPath());
            }
        } else {
            return Base64.getEncoder().encodeToString(fileFromDatabase.getData());
        }
    }

    @Override
    public Boolean deleteFileById(String id) {
        logger.info(" deleteFileById");
        File fileFromDatabase = fileRepository.findById(id).orElseThrow(() -> new NotFoundException("File not found : " + id));

        if(fileFromDatabase.getIsInFolder()){
            if(!FileUtil.deleteFile(BASE_FOLDER + fileFromDatabase.getPath())){
                throw new ValidationException("Could not delete file " + id);
            }
        }
        fileRepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean deleteFileList(Collection<String> fileIds) {
        logger.info(" deleteFileList - {}", fileIds.size());
        for(String id : fileIds){
            File fileFromDatabase = fileRepository.findById(id).orElse(null);
            if(fileFromDatabase == null){
                logger.warn(" File not found : {}", id);
            } else if(fileFromDatabase.getIsInFolder()){
                if(!FileUtil.deleteFile(BASE_FOLDER + fileFromDatabase.getPath())){
                    logger.warn(" Could not delete file : {}", id);
                }
            }
            fileRepository.deleteById(id);
        }
        return true;
    }

    @Override
    public List<FileDto> findAllByProjectAndPath(String project, String path) {
        return fileRepository.findAllByProjectAndPathContainingIgnoreCase(project, path)
                .stream().map(file -> fileMapper.toDto(file))
                .collect(Collectors.toList());
    }
}
