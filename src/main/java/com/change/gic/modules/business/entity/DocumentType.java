package com.change.gic.modules.business.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "DOCUMENT_TYPE")
public class DocumentType {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "TAG", nullable = false, unique = true)
    private String tag;

    @Column(name = "MULTIPLE", nullable = true)
    private Boolean multiple;

    public DocumentType(String name, String tag, Boolean multiple) {
        this.name = name;
        this.tag = tag;
        this.multiple = multiple;
    }

    public DocumentType() {

    }
}
