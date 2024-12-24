package com.ccabank.memoservice.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "T_REQUEST")
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



    public Long getId() {
        return id;
    }


    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public int getApprobationLevel() {
        return approbationLevel;
    }

    public void setApprobationLevel(int approbationLevel) {
        this.approbationLevel = approbationLevel;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public LocalDateTime getLastModification() {
        return lastModification;
    }

    public void setLastModification(LocalDateTime lastModification) {
        this.lastModification = lastModification;
    }
}
