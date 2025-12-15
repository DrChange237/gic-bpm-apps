package com.ccabank.paperless.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_REQUEST")
@Getter
@Setter
public class Request  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "STATUS")
    private RequestStatus status;

    @Column(name = "APPROBATION_LEVEL")
    private int approbationLevel = 0;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "VALIDATION_DATE", nullable = true)
    private LocalDateTime validationDate;

    @Column(name = "LAST_MODIFICATION", nullable = false)
    private LocalDateTime lastModification;

    @Column(name = "STAFF")
    @Size(max = 1000)
    private String staff;

    @Basic(optional = true)
    @Column(name = "REFERENCE")
    @Size(max = 50)
    private String reference;

    @JoinColumn(name = "TYPE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private DocumentType type;

    @Basic(optional = true)
    @Column(name = "DOCUMENT_ID")
    private Long documentId;

    @Basic(optional = true)
    @Column(name = "ARCHIVED")
    private Boolean archived = false;

    @Basic(optional = false)
    @Column(name = "INSTANCE_ID", nullable = false, unique = true)
    private String instanceId;

    @Basic(optional = true)
    @Column(name = "COMMENTS")
    @Size(max = 100)
    private String comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public int getApprobationLevel() {
        return approbationLevel;
    }

    public void setApprobationLevel(int approbationLevel) {
        this.approbationLevel = approbationLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(LocalDateTime validationDate) {
        this.validationDate = validationDate;
    }

    public LocalDateTime getLastModification() {
        return lastModification;
    }

    public void setLastModification(LocalDateTime lastModification) {
        this.lastModification = lastModification;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
