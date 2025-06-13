package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.entity.File;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.openfeign.FileRestClient;
import com.ccabank.paperless.repository.FileRepository;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    private FileRestClient fileRestClient;


    @Value("${file_service_url}")
    private String fileUrl;


    @Override
    public List<FileDto> getAllFiles(String businessKey, String staff) {

        Request request = requestRepository.findOneByReference(businessKey);

        if(request != null){
            List<FileDto> fileDtos = new ArrayList<>();
            List<File> files = fileRepository.findByRequest(request);
            for(File file : files){
                FileDto fileDto = new FileDto();
                fileDto.setAddDate(file.getAddDate());
                fileDto.setName(file.getName());
                fileDto.setUrl(file.getUrl());
                fileDtos.add(fileDto);
            }
            return fileDtos;
        }
        return null;
    }

    @Override
    public void saveFile(Request request, FileDto fileDto) throws IOException {

            FileDto fileFinal = fileRestClient.uploadFileToFolder("paperless", "paperless", fileDto.getMultipartFile());
            File file = new File();
            file.setRequest(request);
            file.setName(fileDto.getName());
            file.setType(fileDto.getType());
            file.setUrl(fileUrl + fileFinal.getUrl());
            file.setAddDate(LocalDateTime.now());
            fileRepository.save(file);


    }
}
