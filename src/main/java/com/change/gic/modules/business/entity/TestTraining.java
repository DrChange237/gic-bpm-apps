package com.change.gic.modules.business.entity;

import com.change.gic.modules.core.entity.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "TEST_TRAINING")
public class TestTraining extends Auditable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "CONTRACT"), nullable = false)
    private Contrat contract;

    @Column(name = "LEVELUP", nullable = false)
    private Boolean levelup;

    @Column(name = "NB_HOUR", nullable = false)
    private int nbHour;

    @Column(name = "OBSERVATION", nullable = true, columnDefinition = "text")
    private String observation;
}
