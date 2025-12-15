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


}
