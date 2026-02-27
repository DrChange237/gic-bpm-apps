package com.change.gic.modules.business.entity;

import com.change.gic.modules.business.enumeration.ConsultationStatus;
import com.change.gic.modules.core.entity.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "TEST_EXAM")
public class TestExam extends Auditable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "END_DATE", nullable = true)
    private LocalDate endDate;

    @Column(name = "EXAM", nullable = true)
    private String exam;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "CONTRACT"), nullable = false)
    private Contrat contract;

    @ManyToOne(optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "LISTENING"), nullable = true)
    private TestNotation listening;

    @ManyToOne(optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "READING"), nullable = true)
    private TestNotation reading;

    @ManyToOne(optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "WRITING"), nullable = true)
    private TestNotation writing;

    @ManyToOne(optional = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "SPEAKING"), nullable = true)
    private TestNotation speaking;

    @Column(name = "STATUS", nullable = true)
    @Enumerated(EnumType.STRING)
    private TestExamStatus status;

    public String getLabel() {
        return exam + " ( " + status + " )" ;
    }


}
