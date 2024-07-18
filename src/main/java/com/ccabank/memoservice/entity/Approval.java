package com.ccabank.memoservice.entity;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_APPROVAL")
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "POSITION", nullable = false)
    private Integer position;

    @Basic(optional = true)
    @Column(name = "APPROVAL_DATE", nullable = true)
    private LocalDateTime approvalDate;

    @JoinColumn(name = "REQUEST", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Request request;

    @Column(name = "STAFF")
    @Size(max = 1000)
    private String staff;

    @Column(name = "STATUS")
    private ApprovalStatus status;

    @Basic(optional = true)
    @Column(name = "COMMENTS")
    @Size(max = 1000)
    private String comments;

    public Long getId() {
        return id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
