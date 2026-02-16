package com.change.gic.modules.business.entity;

import com.change.gic.modules.core.entity.Auditable;
import com.change.gic.modules.core.entity.Document;
import com.change.gic.modules.business.enumeration.ContratStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "CONTRAT")
public class Contrat extends Auditable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "CONSULTATION"), nullable = false)
    private Consultation consultation;

    @Column(name = "REFERENCE", nullable = false, unique = true)
    private String reference;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContratStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "CONTRAT_CONTRATTERM",
            joinColumns = @JoinColumn(name = "CONTRAT_ID"),
            inverseJoinColumns = @JoinColumn(name = "TERM_ID")
    )
    private Set<ContratTerm> terms = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "CONTRAT_DEBOURS",
            joinColumns = @JoinColumn(name = "CONTRAT_ID"),
            inverseJoinColumns = @JoinColumn(name = "DEBOUR_ID")
    )
    private Set<ContratTerm> debours = new HashSet<>();

    @Column(name = "FIRST_AMOUNT", nullable = false)
    private BigDecimal firstAmount;

    @Column(name = "SECOND_AMOUNT", nullable = false)
    private BigDecimal secondAmount;

    @Column(name = "LAST_AMOUNT", nullable = false)
    private BigDecimal lastAmount;

    public BigDecimal getTotalAmount() {
        return this.firstAmount.add(this.secondAmount).add(this.lastAmount);
    }


}
