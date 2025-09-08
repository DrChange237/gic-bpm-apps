package com.ccabank.paperless.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_APPROBATION")
@Getter
@Setter
public class Approbation extends Auditable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "REQUEST_REF", nullable = true)
    private String reference;

    @Column(name = "TASK_ID")
    private String taskId;

    @Column(name = "STAFF", nullable = true)
    private String staff;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;



    @Column(name = "COMMENTS")
    @Basic
    private String comments;

}
