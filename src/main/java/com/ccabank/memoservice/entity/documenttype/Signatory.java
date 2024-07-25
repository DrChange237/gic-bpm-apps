package com.ccabank.memoservice.entity.documenttype;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "T_SIGNATORY")
public class Signatory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @JoinColumn(name = "OWNER", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Staff owner;

    @Column(name = "SIGNATURE")
    @Size(max = 100)
    private String signature;

    public Long getId() {
        return id;
    }

    public Staff getOwner() {
        return owner;
    }

    public void setOwner(Staff owner) {
        this.owner = owner;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
