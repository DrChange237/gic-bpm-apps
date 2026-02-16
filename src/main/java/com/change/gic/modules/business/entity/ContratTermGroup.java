package com.change.gic.modules.business.entity;


import com.change.gic.modules.core.entity.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "CONTRAT_TERM_GROUP")
public class ContratTermGroup extends Auditable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

    @Column(name = "LABEL", nullable = false)
    private String label;

    @Column(name = "POSITION", nullable = false)
    private Integer position;

    public ContratTermGroup(String code, String label, Integer position) {
        this.code = code;
        this.label = label;
        this.position = position;
    }

    public ContratTermGroup() {

    }
}
