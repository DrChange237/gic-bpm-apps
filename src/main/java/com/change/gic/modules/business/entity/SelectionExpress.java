package com.change.gic.modules.business.entity;

import com.change.gic.modules.business.enumeration.SelectionArrimaStatus;
import com.change.gic.modules.business.enumeration.SelectionExpressStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SELECTION_EXPRESS")
public class SelectionExpress {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "END_DATE", nullable = true)
    private LocalDate endDate;

    @Column(name = "POINTS", nullable = true)
    private BigDecimal points;

    @Column(name = "NUMERO", nullable = true)
    private String numero;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "CONTRACT"), nullable = false)
    private Contrat contract;

    @Column(name = "STATUS", nullable = true)
    @Enumerated(EnumType.STRING)
    private SelectionExpressStatus status;

    public String getLabel() {
        return "EXPRESS ( " + status + " ) ";
    }
}
