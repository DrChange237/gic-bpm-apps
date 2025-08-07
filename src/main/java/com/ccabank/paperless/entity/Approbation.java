package com.ccabank.paperless.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_APPROBATION")
public class Approbation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "REQUEST_REF", nullable = true)
    private String reference;

    @Column(name = "TASK_ID")
    private String taskId;

    @Column(name = "STATUS")
    private ApprovalStatus status;

    @Column(name = "COMMENTS")
    @Basic
    private String comments;

    @Column(name = "CREATED_AT", nullable = true)
    private LocalDateTime createdAt = LocalDateTime.now();


    public Long getId() {
        return id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
