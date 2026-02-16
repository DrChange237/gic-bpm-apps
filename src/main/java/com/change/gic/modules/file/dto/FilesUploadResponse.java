package com.change.gic.modules.file.dto;

import lombok.Data;

import java.util.List;


@Data
public class FilesUploadResponse {

    private int code;
    private String message;
    private List<FileDto> result;

}
