package com.ccabank.feedbackservice.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_FORM")
public class Form implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    public Long getId() {
        return id;
    }
}
