package com.change.gic.modules.business.entity;

import com.change.gic.modules.business.enumeration.DiplomaStatus;
import com.change.gic.modules.business.enumeration.EquivalenceStatus;
import com.change.gic.modules.core.entity.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "CONTRACT"), nullable = false)
    private Contrat contract;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "ORGANISME"), nullable = false)
    private OrgEquivalence organisme;

    @Column(name = "DIPLOMA_STATUS", nullable = true)
    @Enumerated(EnumType.STRING)
    private DiplomaStatus diplomaStatus;

    @Column(name = "STATUS", nullable = true)
    @Enumerated(EnumType.STRING)
    private EquivalenceStatus status;


}
