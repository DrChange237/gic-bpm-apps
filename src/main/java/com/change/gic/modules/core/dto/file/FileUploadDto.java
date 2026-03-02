package com.change.gic.modules.core.dto.file;


import lombok.Data;

@Data
public class FileUploadDto {
    private String content;
    private String name;
    private int size;
    private String type;
}
