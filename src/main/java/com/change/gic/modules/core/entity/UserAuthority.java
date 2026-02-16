package com.change.gic.modules.core.entity;


import com.change.gic.modules.core.enumeration.Habilitation;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "USER_AUTHORITY")
public class UserAuthority {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "CODE", nullable = false, unique = true)
    @Convert(converter = Habilitation.Converter.class)
    private Habilitation code;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "ROLE_AUTHORITY", // Nom de la table de jointure
            joinColumns = @JoinColumn(name = "AUTHORITY_ID"), // Colonne de clé étrangère pour Course
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID") // Colonne de clé étrangère pour Student
    )
    private Set<Role> roles = new HashSet<>();

}
