package com.change.gic.modules.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "TEST_LANG")
public class TestLang {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "SLUG", nullable = false, unique = true)
    private String slug;

    @Column(name = "DESCRIPTION", nullable = true, columnDefinition = "text")
    private String description;

    @ManyToMany(mappedBy = "testLangs")
    @JsonIgnore
    private Set<Consultation> consultations = new HashSet<>();

    public TestLang(String slug, String description) {
        this.slug = slug;
        this.description = description;
    }

    public TestLang() {

    }
}
