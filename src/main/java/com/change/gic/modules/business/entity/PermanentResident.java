package com.change.gic.modules.business.entity;

import com.change.gic.modules.business.enumeration.PermanentResidentStatus;
import com.change.gic.modules.core.entity.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "PERMANENT_RESIDENT")
public class PermanentResident extends Auditable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "CONTRACT"), nullable = false)
    private Contrat contract;

    @Column(name = "STATUS", nullable = true)
    @Enumerated(EnumType.STRING)
    private PermanentResidentStatus status;

}
