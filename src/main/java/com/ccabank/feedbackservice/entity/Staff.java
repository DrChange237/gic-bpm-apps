package com.ccabank.feedbackservice.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Table(name = "T_STAFF")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @JoinColumn(name = "AGENCY", referencedColumnName = "ID", nullable = true)
    @ManyToOne(optional = true)
    private Agency agency;

    //Noms et Prénoms du Client
    @Basic(optional = false)
    @NotNull
    @Column(name = "USERNAME")
    private String username;

    //Poste du Staff
    @Basic(optional = true)
    @Column(name = "POSITION")
    private String position;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "staff", fetch = FetchType.LAZY)
    private Collection<Feedback> feedbackCollection;

    public Long getId() {
        return id;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Collection<Feedback> getFeedbackCollection() {
        return feedbackCollection;
    }

    public void setFeedbackCollection(Collection<Feedback> feedbackCollection) {
        this.feedbackCollection = feedbackCollection;
    }
}
