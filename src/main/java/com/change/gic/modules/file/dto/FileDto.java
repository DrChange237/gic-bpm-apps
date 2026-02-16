package com.change.gic.modules.file.dto;


import lombok.Data;

import java.io.Serializable;


@Data
public class FileDto implements Serializable {

    private String id;
    private String name;
    private String url;
    private long size;
    private String type;
    private String project;

}
