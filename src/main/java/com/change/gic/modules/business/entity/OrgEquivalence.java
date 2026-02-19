package com.change.gic.modules.business.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ORG_EQUIVALENCE")
public class OrgEquivalence {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

    @Column(name = "NAME", nullable = false)
    private String name;


}
