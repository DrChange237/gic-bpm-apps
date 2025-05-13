package com.ccabank.paperless.dto.memo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentStructure {

    private String name;

    private List<FieldDto> fields;

    private List<ApprovalDto> approvals;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<FieldDto> getFields() {
        return fields;
    }

    public void setFields(List<FieldDto> fields) {
        this.fields = fields;
    }

    public List<ApprovalDto> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<ApprovalDto> approvals) {
        this.approvals = approvals;
    }
}
