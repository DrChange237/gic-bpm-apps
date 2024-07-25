package com.ccabank.memoservice.entity.documenttype;


import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;
import com.ccabank.memoservice.entity.documenttype.sub.Transport;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "T_ORDRE_MISSION")
public class OrdreMission  {

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

    @Column(name = "PLACE")
    @Size(max = 100)
    private String place;

    @Column(name = "LOCATION")
    @Size(max = 100)
    private String location;

    @Column(name = "OBJECT")
    @Size(max = 100)
    private String object;

    @Column(name = "START_DATE", nullable = true)
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = true)
    private LocalDate endDate;

    @Column(name = "NIGHTS", nullable = false)
    private Integer nights;

    @Column(name = "ACCOUNT_NUMBER", nullable = false)
    private String accountNumber;

    @Column(name = "DECISION", nullable = false)
    private String decision;

    @JoinColumn(name = "TRANSPORT", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Transport transport;

    @JoinColumn(name = "OWNER", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory owner;


    @JoinColumn(name = "SUPERVISOR", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory supervisor;

    @JoinColumn(name = "SUPERVISOR_NEXT", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory supervisorNext;

    @JoinColumn(name = "UCH", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory uch;

    @JoinColumn(name = "ORDER_GIVEN", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory orderGiven;

    @Column(name = "CHARGE_SUPPORT", nullable = false)
    private Double chargeSupport;

    @Column(name = "MISSION_FEES", nullable = false)
    private Double missionFees;

    @Column(name = "TRANSPORT_FEES", nullable = false)
    private Double transportFees;

    @Column(name = "AUTHORISATION_NUMBER")
    @Size(max = 100)
    private String authorisationNumber;

    @Column(name = "RECEIPT_NUMBER")
    @Size(max = 100)
    private String receiptNumber;


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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
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

    public Integer getNights() {
        return nights;
    }

    public void setNights(Integer nights) {
        this.nights = nights;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
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

    public Signatory getSupervisorNext() {
        return supervisorNext;
    }

    public void setSupervisorNext(Signatory supervisorNext) {
        this.supervisorNext = supervisorNext;
    }

    public Signatory getUch() {
        return uch;
    }

    public void setUch(Signatory uch) {
        this.uch = uch;
    }

    public Signatory getOrderGiven() {
        return orderGiven;
    }

    public void setOrderGiven(Signatory orderGiven) {
        this.orderGiven = orderGiven;
    }

    public Double getChargeSupport() {
        return chargeSupport;
    }

    public void setChargeSupport(Double chargeSupport) {
        this.chargeSupport = chargeSupport;
    }

    public Double getMissionFees() {
        return missionFees;
    }

    public void setMissionFees(Double missionFees) {
        this.missionFees = missionFees;
    }

    public Double getTransportFees() {
        return transportFees;
    }

    public void setTransportFees(Double transportFees) {
        this.transportFees = transportFees;
    }

    public String getAuthorisationNumber() {
        return authorisationNumber;
    }

    public void setAuthorisationNumber(String authorisationNumber) {
        this.authorisationNumber = authorisationNumber;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }
}
