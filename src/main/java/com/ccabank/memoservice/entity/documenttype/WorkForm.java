package com.ccabank.memoservice.entity.documenttype;

import javax.persistence.*;

@Entity
@Table(name = "T_WORKFORM")
public class WorkForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;
    
}
