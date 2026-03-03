package com.change.gic.modules.business.entity;

import com.change.gic.modules.business.enumeration.ConsultationStatus;
import com.change.gic.modules.core.entity.Document;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "CONSULTATION")
public class Consultation {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "END_DATE", nullable = true)
    private LocalDate endDate;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "INSCRIPTION"), nullable = false)
    private Inscription inscription;

    @Column(name = "OBSERVATION", nullable = true, columnDefinition = "text")
    private String observation;

    @Column(name = "FIRST_AMOUNT", nullable = false)
    private BigDecimal firstAmount;

    @Column(name = "SECOND_AMOUNT", nullable = false)
    private BigDecimal secondAmount;

    @Column(name = "LAST_AMOUNT", nullable = false)
    private BigDecimal lastAmount;

    public BigDecimal getTotalAmount() {
        return this.firstAmount.add(this.secondAmount).add(this.lastAmount);
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "CONSULTATION_PROGRAM",
            joinColumns = @JoinColumn(name = "CONSULTATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "PROGRAM_ID")
    )
    private Set<Program> programs = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "CONSULTATION_TEST_LANG",
            joinColumns = @JoinColumn(name = "CONSULTATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "TEST_LANG_ID")
    )
    private Set<TestLang> testLangs = new HashSet<>();

    @Column(name = "EQUIVALENCE", nullable = true)
    private Boolean equivalence;

    @Column(name = "TEST_LANG", nullable = true)
    private Boolean testLang;

    @Column(name = "ELIGIBLE", nullable = false)
    private Boolean eligible;

    @Column(name = "STATUS", nullable = true)
    @Enumerated(EnumType.STRING)
    private ConsultationStatus status;


    @ManyToOne(optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "MANUSCRIT"), nullable = true)
    private Document manuscrit;


}
