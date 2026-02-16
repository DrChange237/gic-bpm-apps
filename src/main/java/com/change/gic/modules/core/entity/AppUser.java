package com.change.gic.modules.core.entity;

import com.change.gic.modules.business.entity.Agency;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "APP_USER")
@Getter
@Setter
public class AppUser extends Auditable{

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "AGENCY"), nullable = false)
    private Agency agency;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "ROLE"), nullable = false)
    private Role role;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private boolean enabled = true;


}
