package com.ccabank.paperless.dto.memo;

import com.ccabank.paperless.entity.RequestStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class RequestInfo implements Serializable {

    private Long id;

    private  String reference;

    private String staff;

    private LocalDateTime createdAt;

    private int approbationLevel;

    private String documentType;

    private RequestStatus status;

    private List<FieldDto> fields;

    private List<FileDto> files;

    private List<ApprovalDto> approvals;

    private String comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public List<FieldDto> getFields() {
        return fields;
    }

    public void setFields(List<FieldDto> fields) {
        this.fields = fields;
    }

    public List<FileDto> getFiles() {
        return files;
    }

    public void setFiles(List<FileDto> files) {
        this.files = files;
    }

    public List<ApprovalDto> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<ApprovalDto> approvals) {
        this.approvals = approvals;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
