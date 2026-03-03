package com.change.gic.modules.core.dto.camunda;

import com.change.gic.modules.core.dto.file.FileUploadDto;
import lombok.Data;

import java.util.Map;

@Data
public class CompleteTask {
    private Map<String, Object> formData;
    private Map<String, FileUploadDto> files;
    private String taskId;
}
