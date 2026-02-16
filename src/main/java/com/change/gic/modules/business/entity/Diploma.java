package com.change.gic.modules.business.entity;

import com.change.gic.modules.core.entity.Auditable;
import com.change.gic.modules.business.enumeration.DiplomaLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "DIPLOMA")
public class Diploma extends Auditable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "LABEL", nullable = false)
    private String label;

    @Column(name = "LEVEL", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiplomaLevel level;

}
