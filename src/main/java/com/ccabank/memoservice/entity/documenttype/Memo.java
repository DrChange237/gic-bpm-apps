package com.ccabank.memoservice.entity.documenttype;


import com.ccabank.memoservice.entity.Field;
import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "T_MEMO")
public class Memo {

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

    @Column(name = "SUBJECT")
    @Size(max = 100)
    private String subject;

    @Column(name = "MATERIAL")
    @Size(max = 100)
    private String material;

    @Column(name = "BODY")
    @Size(max = 5000)
    private String body;

    @Column(name = "RECEIVER")
    @Size(max = 255)
    private String receiver;

    @JoinColumn(name = "SUPERVISOR", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Signatory supervisor;

    public Long getId() {
        return id;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memo", fetch = FetchType.LAZY)
    private Collection<Signatory> signatories = new ArrayList<>();

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Collection<Signatory> getSignatories() {
        return signatories;
    }

    public void setSignatories(Collection<Signatory> signatories) {
        this.signatories = signatories;
    }

    public Signatory getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Signatory supervisor) {
        this.supervisor = supervisor;
    }
}
