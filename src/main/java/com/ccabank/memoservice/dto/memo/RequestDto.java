package com.ccabank.memoservice.dto.memo;

import com.ccabank.memoservice.entity.RequestStatus;

import java.util.List;

public class RequestDto {


    private Long id;

    private String staff;

    private int approbationLevel;

    private String documentType;

    private RequestStatus status;

    private List<ApprovalDto> approvals;

    private List<FieldDto> fields;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public int getApprobationLevel() {
        return approbationLevel;
    }

    public void setApprobationLevel(int approbationLevel) {
        this.approbationLevel = approbationLevel;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public List<ApprovalDto> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<ApprovalDto> approvals) {
        this.approvals = approvals;
    }

    public List<FieldDto> getFields() {
        return fields;
    }

    public void setFields(List<FieldDto> fields) {
        this.fields = fields;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
