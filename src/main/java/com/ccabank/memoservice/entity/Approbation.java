package com.ccabank.memoservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "T_APPROBATION")
public class Approbation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "TASK_ID")
    private String taskId;

    @Column(name = "STATUS")
    private ApprovalStatus status;

    @Column(name = "COMMENTS", nullable = true)
    @Basic(optional = true)
    private String comments;

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
}
