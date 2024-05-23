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

    //Avez vous un compte au CCA-BANK
    @Column(name = "HAVE_ACCOUNT")
    private boolean haveAccount;

    //Agence ou est domicilié le compte en question
    @Basic(optional = true)
    @Column(name = "BRANCH")
    private String branch;

    //Objet de la visite
    @Basic(optional = false)
    @NotNull
    @Column(name = "VISIT_CAUSE")
    private String visitCause;

    // Comment évaluerez-vous la clarté des infos et des instructions fournis par l’agent qui vous a reçu ? Note de 1 à 5
    @Column(name = "CLARTE_INSTRUCTIONS")
    private int clarteInstructions;


    // Avez-vous été satisfait de la qualité de l’accueil de cet agent ? Note de 1 à 5
    @Column(name = "QUALITY_WELCOME")
    private int qualityOfWelcome;


    // Sur une échelle de 0 à 5, comment évaluez-vous le Professionnalisme, la convivialité et la courtoisie de cet agent ?
    @Column(name = "PROFESSIONALISM")
    private int professionalism;

    //Comment évalueriez-vous la rapidité de réponse de l’agent à vos demandes ou questions ?
    @Column(name = "SPEED")
    private int speed;

    //Disponibilité des fournitures formulaires de guichet, stylos, eau potable, etc.
    @Column(name = "FORM_AVAIBILITY")
    private int formAvaibility;

    //Climatisation
    @Column(name = "CLIMATISATION")
    private int climatisation;

    //Temps d'attente
    @Column(name = "WAITING_TIME")
    private int waitingTime;

    //Confort
    @Column(name = "COMFORT")
    private int comfort;

    //Propreté des toilettes
    @Column(name = "TOILET_CLEANLINESS")
    private int toiletCleanliness;

    //Propreté Générale
    @Column(name = "CLEANLINESS")
    private int cleanliness;

    //Sur une échelle allant de 1 à 5, comment évaluerez-vous tout autre membre du personnel avec qui vous avez interagit ;
    @Column(name = "OTHER_STAFF_MEMBER")
    private int otherStaffMember;

    //Avez-vous des commentaires ou des suggestions ?
    @Basic(optional = true)
    @Column(name = "COMMENTS")
    private String comments;

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

    public boolean isHaveAccount() {
        return haveAccount;
    }

    public void setHaveAccount(boolean haveAccount) {
        this.haveAccount = haveAccount;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getVisitCause() {
        return visitCause;
    }

    public void setVisitCause(String visitCause) {
        this.visitCause = visitCause;
    }

    public int getClarteInstructions() {
        return clarteInstructions;
    }

    public void setClarteInstructions(int clarteInstructions) {
        this.clarteInstructions = clarteInstructions;
    }

    public int getQualityOfWelcome() {
        return qualityOfWelcome;
    }

    public void setQualityOfWelcome(int qualityOfWelcome) {
        this.qualityOfWelcome = qualityOfWelcome;
    }

    public int getProfessionalism() {
        return professionalism;
    }

    public void setProfessionalism(int professionalism) {
        this.professionalism = professionalism;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getFormAvaibility() {
        return formAvaibility;
    }

    public void setFormAvaibility(int formAvaibility) {
        this.formAvaibility = formAvaibility;
    }

    public int getClimatisation() {
        return climatisation;
    }

    public void setClimatisation(int climatisation) {
        this.climatisation = climatisation;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getComfort() {
        return comfort;
    }

    public void setComfort(int comfort) {
        this.comfort = comfort;
    }

    public int getToiletCleanliness() {
        return toiletCleanliness;
    }

    public void setToiletCleanliness(int toiletCleanliness) {
        this.toiletCleanliness = toiletCleanliness;
    }

    public int getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(int cleanliness) {
        this.cleanliness = cleanliness;
    }

    public int getOtherStaffMember() {
        return otherStaffMember;
    }

    public void setOtherStaffMember(int otherStaffMember) {
        this.otherStaffMember = otherStaffMember;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
