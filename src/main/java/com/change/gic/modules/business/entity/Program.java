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
@Table(name = "PROGRAM")
public class Program {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "DESCRIPTION", nullable = true, columnDefinition = "text")
    private String description;

    @ManyToMany(mappedBy = "programs")
    @JsonIgnore
    private Set<Consultation> consultations = new HashSet<>();

    public Program(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public Program() {

    }
}
