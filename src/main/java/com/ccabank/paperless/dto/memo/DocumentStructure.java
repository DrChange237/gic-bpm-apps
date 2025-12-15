package com.ccabank.paperless.dto.memo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentStructure {

    private String name;

    private List<FieldDto> fields;

    private List<ApprovalDto> approvals;
}
