package com.change.gic.modules.core.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "MODULE")
@Getter
@Setter
public class Module extends  Auditable{

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String key;

    @Column(nullable = false)
    private int position;

    @Column(columnDefinition = "text", nullable = true)
    private String description;

    public Module(String name, String key, int position) {
        this.name = name;
        this.key = key;
        this.position = position;
    }

    public Module() {

    }
}
