package com.ccabank.paperless.dto.memo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FileDto implements Serializable {

    private String id;

    private String url;

    private String name;

    private String type;

    private long size;

    private LocalDateTime addDate;

    private String project;

    private String file;

    private RequestDto request;

    private MultipartFile multipartFile;
}
