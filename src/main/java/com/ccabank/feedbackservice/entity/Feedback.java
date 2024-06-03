package com.ccabank.feedbackservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : feedbackservice
 * @Package : com.ccabank.feedbackservice.entity
 * <p>
 * @date: 08/08/2023
 * @time: 11:05
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_FEEDBACK")
public class Feedback implements Serializable {

    // `private Long id;` déclare une variable d'instance privée `id` de type `Long` dans la classe
    // `Country`. Cette variable représente l'identifiant unique d'un objet `Country` et est annotée par
    // `@Id` pour indiquer qu'il s'agit de la clé primaire de la table de base de données correspondante.
    // Il est également annoté avec `@GeneratedValue` pour spécifier que la valeur de ce champ sera
    // automatiquement générée par la base de données lorsqu'un nouvel objet `Country` est persisté.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;


    //Noms et Prénoms du Client
    @Basic(optional = false)
    @NotNull
    @Column(name = "STAFF_USERNAME")
    private String staffUsername;

    @JoinColumn(name = "AGENCY", referencedColumnName = "ID", nullable = true)
    @ManyToOne(optional = true)
    private Agency agency;


    //Noms et Prénoms du Client
    @Basic(optional = false)
    @NotNull
    @Column(name = "FULLNAME")
    private String fullname;


    //Numero de Téléphone du Client
    @Basic(optional = false)
    @NotNull
    @Column(name = "MOBILE")
    private String mobile;

    //Numero de Téléphone du Client
    @Basic(optional = true)
    @Column(name = "EMAIL")
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feedback", fetch = FetchType.LAZY)
    private Collection<Answer> answerCollection;

    public Feedback() {
    }

    /**
     * Gets id.
     *
     * @return value of id
     */
    public Long getId() {
        return id;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStaffUsername() {
        return staffUsername;
    }

    public void setStaffUsername(String staffUsername) {
        this.staffUsername = staffUsername;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<Answer> getAnswerCollection() {
        return answerCollection;
    }

    public void setAnswerCollection(Collection<Answer> answerCollection) {
        this.answerCollection = answerCollection;
    }

}
