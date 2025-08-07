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

    @JoinColumn(name = "REQUEST", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Request request;

    @Column(name = "TASK_ID")
    private String taskId;

    @Column(name = "STATUS")
    private ApprovalStatus status;

    @Column(name = "COMMENTS")
    @Basic
    private String comments;

    @Column(name = "CREATED_AT", nullable = true)
    private LocalDateTime createdAt;


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

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
