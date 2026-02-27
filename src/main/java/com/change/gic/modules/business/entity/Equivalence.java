package com.change.gic.modules.business.entity;

import com.change.gic.modules.business.enumeration.DiplomaStatus;
import com.change.gic.modules.business.enumeration.EquivalenceStatus;
import com.change.gic.modules.core.entity.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "EQUIVALENCE")
public class Equivalence extends Auditable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "END_DATE", nullable = true)
    private LocalDate endDate;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "CONTRACT"), nullable = false)
    private Contrat contract;

    @Column(name = "ORGANISM", nullable = true)
    private String organisme;

    @Column(name = "REF", nullable = true)
    private String ref;

    @Column(name = "DIPLOMA_STATUS", nullable = true)
    @Enumerated(EnumType.STRING)
    private DiplomaStatus diplomaStatus;

    @Column(name = "STATUS", nullable = true)
    @Enumerated(EnumType.STRING)
    private EquivalenceStatus status;

    public String getLabel() {
        return " ( " + status + " ) - " + diplomaStatus;
    }


}
