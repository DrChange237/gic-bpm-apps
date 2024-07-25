package com.ccabank.memoservice.entity.documenttype.sub;


import javax.persistence.*;

@Entity
@Table(name = "T_SETTLEMENT")
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "PAID", nullable = false)
    private Double paid;

    @Column(name = "PAID", nullable = false)
    private Double unpaid;

    public Long getId() {
        return id;
    }

    public Double getPaid() {
        return paid;
    }

    public void setPaid(Double paid) {
        this.paid = paid;
    }

    public Double getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(Double unpaid) {
        this.unpaid = unpaid;
    }
}
