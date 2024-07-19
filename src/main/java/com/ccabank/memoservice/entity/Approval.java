package com.ccabank.memoservice.entity;


import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

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

    @JoinColumn(name = "PROCESS_UNITY", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private ProcessUnity processUnity;


    @Basic(optional = true)
    @Column(name = "STAFF")
    @Size(max = 1000)
    private String staff;

    @Basic(optional = true)
    @Column(name = "ROLE")
    @Size(max = 1000)
    private String role;

    @Column(name = "STATUS")
    private ApprovalStatus status;

    @Column(name = "TYPE")
    private ApprovalType type = ApprovalType.OPEN;


    @Basic(optional = true)
    @Column(name = "COMMENTS")
    @Size(max = 1000)
    private String comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "request", fetch = FetchType.LAZY)
    private Collection<Field> fields = new ArrayList<>();

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

    public ProcessUnity getProcessUnity() {
        return processUnity;
    }

    public void setProcessUnity(ProcessUnity processUnity) {
        this.processUnity = processUnity;
    }

    public Collection<Field> getFields() {
        return fields;
    }

    public void setFields(Collection<Field> fields) {
        this.fields = fields;
    }
}
