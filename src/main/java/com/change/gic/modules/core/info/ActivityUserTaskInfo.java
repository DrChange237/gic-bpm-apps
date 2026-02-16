package com.change.gic.modules.core.info;

import lombok.Data;

import java.util.HashMap;

@Data
public class ActivityUserTaskInfo {
    private String id;
    private String taskDefinitionKey;
    private String documentation;
    private HashMap<String, String> mapVariable;
}
