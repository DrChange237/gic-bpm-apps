package com.change.gic.modules.business.entity;

import com.change.gic.modules.business.enumeration.MovementFlow;
import com.change.gic.modules.core.entity.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "MONEY_MOVEMENT")
public class MoneyMovement extends Auditable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "LABEL", nullable = false)
    private String label;

    @Column(name = "REFERENCE", nullable = true)
    private String reference;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "FEES", nullable = true)
    private Boolean fees;

    @Column(name = "FLOW", nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementFlow flow;

}
