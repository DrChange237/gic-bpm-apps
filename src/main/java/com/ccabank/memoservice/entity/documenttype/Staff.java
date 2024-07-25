package com.ccabank.memoservice.entity.documenttype;


import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "T_STAFF_REQUESTER")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "NAME")
    @Size(max = 100)
    private String name;

    @Column(name = "FUNCTION")
    @Size(max = 100)
    private String function;

    @Column(name = "UNITY")
    @Size(max = 100)
    private String unity;

    @Column(name = "MATRICULE")
    @Size(max = 100)
    private String matricule;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

}
