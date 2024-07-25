package com.ccabank.memoservice.entity.documenttype;


import com.ccabank.memoservice.entity.documenttype.sub.Deduction;
import com.ccabank.memoservice.entity.documenttype.sub.Settlement;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "T_ABSENCE")
public class Absence {

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

    @Column(name = "START_DATE", nullable = true)
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = true)
    private LocalDate endDate;

    @Column(name = "DAYS", nullable = false)
    private Integer days;

    @Column(name = "REASON")
    @Size(max = 100)
    private String reason;

    @JoinColumn(name = "INTERIM", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Staff interim;

    @Column(name = "DEDUCTION")
    @Size(max = 100)
    private Deduction deduction;

    @JoinColumn(name = "SUPERVISOR", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory supervisor;

    @JoinColumn(name = "SUPERVISOR2", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory supervisor2;

    @JoinColumn(name = "HEADOFFICE", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory headOffice;

    @JoinColumn(name = "ABSENCE", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Settlement absence;

    @JoinColumn(name = "STOCK", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Settlement stock;

    @JoinColumn(name = "ADVICE", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Settlement advice;

    @JoinColumn(name = "RIGHTS", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Settlement rights;

    @JoinColumn(name = "SALARY", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Settlement salary;

    @JoinColumn(name = "VACATION", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Settlement vacation;

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

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Staff getInterim() {
        return interim;
    }

    public void setInterim(Staff interim) {
        this.interim = interim;
    }

    public Deduction getDeduction() {
        return deduction;
    }

    public void setDeduction(Deduction deduction) {
        this.deduction = deduction;
    }

    public Signatory getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Signatory supervisor) {
        this.supervisor = supervisor;
    }

    public Signatory getSupervisor2() {
        return supervisor2;
    }

    public void setSupervisor2(Signatory supervisor2) {
        this.supervisor2 = supervisor2;
    }

    public Signatory getHeadOffice() {
        return headOffice;
    }

    public void setHeadOffice(Signatory headOffice) {
        this.headOffice = headOffice;
    }

    public Settlement getAbsence() {
        return absence;
    }

    public void setAbsence(Settlement absence) {
        this.absence = absence;
    }

    public Settlement getStock() {
        return stock;
    }

    public void setStock(Settlement stock) {
        this.stock = stock;
    }

    public Settlement getAdvice() {
        return advice;
    }

    public void setAdvice(Settlement advice) {
        this.advice = advice;
    }

    public Settlement getRights() {
        return rights;
    }

    public void setRights(Settlement rights) {
        this.rights = rights;
    }

    public Settlement getSalary() {
        return salary;
    }

    public void setSalary(Settlement salary) {
        this.salary = salary;
    }

    public Settlement getVacation() {
        return vacation;
    }

    public void setVacation(Settlement vacation) {
        this.vacation = vacation;
    }
}
