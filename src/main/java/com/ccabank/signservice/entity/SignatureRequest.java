package com.ccabank.signservice.entity;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "T_SIGNATURE_REQUEST")
public class SignatureRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "SIGNATURE_STATUS")
    @Enumerated(EnumType.STRING)
    private SignatureRequestStatus signatureRequestStatus;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "request", fetch = FetchType.LAZY)
    private Collection<Document> documents = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public SignatureRequestStatus getSignatureRequestStatus() {
        return signatureRequestStatus;
    }

    public void setSignatureRequestStatus(SignatureRequestStatus signatureRequestStatus) {
        this.signatureRequestStatus = signatureRequestStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public Collection<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Collection<Document> documents) {
        this.documents = documents;
    }


}
