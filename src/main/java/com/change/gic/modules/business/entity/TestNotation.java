package com.change.gic.modules.business.entity;

import com.change.gic.modules.business.enumeration.Matiere;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "TEST_NOTATION")
public class TestNotation {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "EXAM"), nullable = false)
    private TestLang exam;

    @Column(name = "MINIMAL", nullable = false)
    private Integer min;

    @Column(name = "MAXIMAL", nullable = false)
    private Integer max;

    @Column(name = "LEVEL", nullable = false)
    private Integer level;

    @Column(name = "MENTION", nullable = false)
    private String mention;

    @Column(name = "MATIERE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Matiere matiere;

    public String getLabel() {
        return this.min + "pts - " + this.max + "pts, Niveau : " + this.level;
    }

}
