package com.ccabank.memoservice.dto.email;

import com.ccabank.memoservice.dto.memo.RequestDto;

public class EmailAskApprovalDto {

    private String subject;

    private String sender;

    private String bCC ;

    private String approver;

    private String reference;

    private String role;

    private String type;

    private AttachmentDto[] attachments;


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AttachmentDto[] getAttachments() {
        return attachments;
    }

    public void setAttachments(AttachmentDto[] attachments) {
        this.attachments = attachments;
    }

    public String getbCC() {
        return bCC;
    }

    public void setbCC(String bCC) {
        this.bCC = bCC;
    }
}
