package com.change.gic.modules.core.dto.camunda;


import lombok.Data;

@Data
public class ProcessDefinitionDto {

    private String id;
    private String key;
    private String name;
    private int version;
    private String deploymentId;
    private String description;
    private String category;
    private String resourceName;
    private String resourceId;


    public ProcessDefinitionDto(String id, String key, String name, String description, String category) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.description = description;
        this.category = category;
    }
}
