package com.change.gic.modules.core.info;

import lombok.Data;

@Data
public class ProcessDefinitionInfo {
    private String id;
    private String key;
    private String name;
    private int version;
    private String deploymentId;
}
