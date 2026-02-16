package com.change.gic.modules.core.entity;

import com.change.gic.modules.file.entity.File;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "DOCUMENT")
public class Document extends Auditable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(nullable = false)
    private String label;

    @Column(nullable = true)
    private String tag;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FILE"), nullable = false)
    private File file;

    @Column(nullable = false)
    private String businessKey;

}
