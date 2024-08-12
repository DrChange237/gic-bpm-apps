package com.ccabank.memoservice.entity.documenttype;


import com.ccabank.memoservice.entity.documenttype.sub.Deduction;
import com.ccabank.memoservice.entity.documenttype.sub.ResumptionReason;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "T_RESUMPTION")
public class Resumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "CREATED_DATE", nullable = true)
    private LocalDate date;

    @JoinColumn(name = "REQUESTER", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Staff requester;


    @JoinColumn(name = "OWNER", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory owner;

    @Column(name = "PLACE")
    @Size(max = 100)
    private String place;

    @Column(name = "START_DATE", nullable = true)
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = true)
    private LocalDate endDate;

    @Column(name = "REAL_END_DATE", nullable = true)
    private LocalDate realEndDate;

    @Column(name = "REASON")
    private ResumptionReason reason;

    @JoinColumn(name = "SUPERVISOR", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory supervisor;

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Staff getRequester() {
        return requester;
    }

    public void setRequester(Staff requester) {
        this.requester = requester;
    }

    public Signatory getOwner() {
        return owner;
    }

    public void setOwner(Signatory owner) {
        this.owner = owner;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getRealEndDate() {
        return realEndDate;
    }

    public void setRealEndDate(LocalDate realEndDate) {
        this.realEndDate = realEndDate;
    }

    public ResumptionReason getReason() {
        return reason;
    }

    public void setReason(ResumptionReason reason) {
        this.reason = reason;
    }

    public Signatory getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Signatory supervisor) {
        this.supervisor = supervisor;
    }
}
