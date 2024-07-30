package com.ccabank.memoservice.dto.response;

import com.ccabank.memoservice.entity.ApprovalType;

public class ApprovalList {

    private  Long id;

    private int position;

    private String staff;

    private  String role;

    private ApprovalType type;

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
}
