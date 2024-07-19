package com.ccabank.memoservice.dto.memo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentStructure {
    private String name;

    private String generateUrl;

    private List<FieldDto> fields;

    private List<ApprovalDto> approvals;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenerateUrl() {
        return generateUrl;
    }

    public void setGenerateUrl(String generateUrl) {
        this.generateUrl = generateUrl;
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
