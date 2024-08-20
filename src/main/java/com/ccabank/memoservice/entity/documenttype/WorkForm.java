package com.ccabank.memoservice.entity.documenttype;

import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "T_WORKFORM")
public class WorkForm {

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

    @JoinColumn(name = "SUPERVISOR", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory supervisor;

    @JoinColumn(name = "ACCOUNTANT", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory accountant;

    @JoinColumn(name = "HEAD", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory head;

    @Column(name = "WORKS")
    @Size(max = 2500)
    private String workToSolve;

    @Column(name = "PROVIDER")
    @Size(max = 2500)
    private String provider;

    @Column(name = "DESIGNATION")
    @Size(max = 255)
    private String designation;

    @Column(name = "BRAND")
    @Size(max = 255)
    private String brand;

    @Column(name = "CODE")
    @Size(max = 255)
    private String code;

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

    public Signatory getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Signatory supervisor) {
        this.supervisor = supervisor;
    }

    public Signatory getAccountant() {
        return accountant;
    }

    public void setAccountant(Signatory accountant) {
        this.accountant = accountant;
    }

    public Signatory getHead() {
        return head;
    }

    public void setHead(Signatory head) {
        this.head = head;
    }

    public String getWorkToSolve() {
        return workToSolve;
    }

    public void setWorkToSolve(String workToSolve) {
        this.workToSolve = workToSolve;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
