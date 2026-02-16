package com.change.gic.modules.core.info;

import com.change.gic.modules.file.dto.FileDto;
import lombok.Data;

@Data
public class DocumentInfo extends AuditableInfo {
    private String id;
    private String label;
    private FileDto file;
    private String businessKey;
}
