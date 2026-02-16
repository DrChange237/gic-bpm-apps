package com.change.gic.modules.core.info;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditableInfo {

    public LocalDateTime creationDate;

    public LocalDateTime lastModifiedDate;

    public String createdBy;

    public String modifiedBy;
}
