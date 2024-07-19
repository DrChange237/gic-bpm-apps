package com.ccabank.memoservice.dto.memo;

import com.ccabank.memoservice.entity.ApprovalStatus;
import com.ccabank.memoservice.entity.ApprovalType;
import com.ccabank.memoservice.entity.ProcessUnity;

import java.time.LocalDateTime;
import java.util.List;

public class ApprovalDto {


    private  Long id;

    private int position;

    private String staff;

    private  String role;

    private ApprovalType type;

    private LocalDateTime approvalDate;

    private ApprovalStatus status;

    private String comments;

    private String unity;

    private List<FieldDto> fields;

    private boolean required;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ApprovalType getType() {
        return type;
    }

    public void setType(ApprovalType type) {
        this.type = type;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }



    public List<FieldDto> getFields() {
        return fields;
    }

    public void setFields(List<FieldDto> fields) {
        this.fields = fields;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
