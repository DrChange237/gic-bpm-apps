package com.ccabank.feedbackservice.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Table(name = "T_AGENCY")
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "AGENCY_NAME")
    private String agencyName;

    @Column(name = "AGENCY_CODE")
    @Size(max = 1000)
    private String agencyCode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agency", fetch = FetchType.LAZY)
    private Collection<Feedback> feedbackCollection;

    public Long getId() {
        return id;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public Collection<Feedback> getFeedbackCollection() {
        return feedbackCollection;
    }

    public void setFeedbackCollection(Collection<Feedback> feedbackCollection) {
        this.feedbackCollection = feedbackCollection;
    }
}
