package com.ccabank.memoservice.entity.documenttype;


import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "T_TRANSPORT")
public class Transport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "COMMON")
    private Boolean common;

    @Basic(optional = true)
    @Column(name = "IMMATRICULATION")
    @Size(max = 100)
    private String immatriculation;

    @Basic(optional = true)
    @Column(name = "COURSIER")
    @Size(max = 100)
    private String coursier;

    public Long getId() {
        return id;
    }

    public Boolean getCommon() {
        return common;
    }

    public void setCommon(Boolean common) {
        this.common = common;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getCoursier() {
        return coursier;
    }

    public void setCoursier(String coursier) {
        this.coursier = coursier;
    }
}
