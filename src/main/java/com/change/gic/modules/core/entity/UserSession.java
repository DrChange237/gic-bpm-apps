package com.change.gic.modules.core.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "USER_SESSION")
public class UserSession extends Auditable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "CS_USER_ID"), nullable = false)
    private AppUser user;

    @Column(name = "DATE", nullable = false)
    private LocalDateTime date;

    @Column(name = "TOKEN", nullable = false, columnDefinition = "text")
    private String token;

}
