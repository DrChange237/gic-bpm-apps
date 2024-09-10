package com.ccabank.memoservice.entity.documenttype;

import com.ccabank.memoservice.entity.documenttype.sub.Signatory;
import com.ccabank.memoservice.entity.documenttype.sub.Staff;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "T_PURCHASE")
public class Purchase {

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

    @Column(name = "OBJECT", nullable = false)
    private String object;

    @Column(name = "UNITY", nullable = false)
    private String unity;

    @Column(name = "SUPPLIER_SUBMITED", nullable = false)
    private String suppliersSubmited;

    @Column(name = "SUPPLIER", nullable = false)
    private String supplier;

    @Column(name = "DEADLINE", nullable = true)
    private LocalDate deadline;

    @Column(name = "TYPE_REGLEMENT", nullable = false)
    private String typeReglement;

    @Column(name = "ACCOUNT", nullable = true)
    private String account;



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

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public String getSuppliersSubmited() {
        return suppliersSubmited;
    }

    public void setSuppliersSubmited(String suppliersSubmited) {
        this.suppliersSubmited = suppliersSubmited;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getTypeReglement() {
        return typeReglement;
    }

    public void setTypeReglement(String typeReglement) {
        this.typeReglement = typeReglement;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
