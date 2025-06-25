package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.dto.memo.FileDto;
import com.ccabank.paperless.entity.File;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.mappers.FileMapper;
import com.ccabank.paperless.openfeign.FileRestClient;
import com.ccabank.paperless.repository.FileRepository;
import com.ccabank.paperless.repository.RequestRepository;
import com.ccabank.paperless.service.faces.FileService;
import com.ccabank.paperless.specification.FileSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Autowired
    private FileMapper fileMapper;


    @Value("${server_url}")
    private String serverUrl;

    private final String pathFile = "/api/files/";


    @Override
    public Page<FileDto> search(String reference, String type, String staff, int page, int size) {
        // Commencez avec une spécification "vide" ou "vraie"
        Specification<File> spec = Specification.where(null);
        spec = FileSpecifications.withDynamicQuery(reference, type, staff);
        Pageable pageable = PageRequest.of(page, size);
        Page<File> files = fileRepository.findAll(pageable);
        if(spec != null){
            files = fileRepository.findAll(spec, pageable);
        }
        // Convert Page<Element> to Page<ElementDto> using the map() method
        return files.map(fileMapper::toDto);
    }


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
            file.setUrl(serverUrl + pathFile + fileFinal.getUrl());
            file.setAddDate(LocalDateTime.now());
            fileRepository.save(file);


    }
}
