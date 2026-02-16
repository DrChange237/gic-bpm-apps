package com.change.gic.modules.business.entity;

import com.change.gic.modules.core.entity.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "CONTRAT_TERM")
public class ContratTerm extends Auditable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "CONTRAT_TERM_GROUP"), nullable = false)
    private ContratTermGroup group;

    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

    @Column(name = "LABEL", nullable = false)
    private String label;

    @Column(name = "POSITION", nullable = false)
    private Integer position;

    @ManyToMany(mappedBy = "terms")
    private Set<Contrat> contrats = new HashSet<>();

    @ManyToMany(mappedBy = "debours")
    private Set<Contrat> contratsForDebours = new HashSet<>();

    public ContratTerm(ContratTermGroup group, String code, String label, Integer position) {
        this.group = group;
        this.code = code;
        this.label = label;
        this.position = position;
    }

    public ContratTerm() {

    }
}
