package com.ccabank.paperless.entity.bug;

import com.ccabank.paperless.entity.Auditable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "T_BUG")
@Getter
@Setter
public class Bug extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "LABEL")
    private String label;

    @Column(name = "TRACES", columnDefinition = "text")
    private String traces;

}
