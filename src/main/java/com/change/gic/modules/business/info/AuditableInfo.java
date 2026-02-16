package com.change.gic.modules.business.info;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class AuditableInfo {

    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private String createdBy;
    private String modifiedBy;
}
