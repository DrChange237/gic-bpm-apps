package com.ccabank.memoservice.entity;

import com.ccabank.memoservice.security.Authority;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "T_DOCUMENT_TYPE")
public class DocumentType {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "NAME")
    @Size(max = 1000)
    private String name;

    @Column(name = "STRUCTURE", unique = true)
    @Size(max = 1000)
    private String structure;

    public DocumentType(String name, String structure) {
        this.name = name;
        this.structure = structure;
    }

    public DocumentType() {

    }

    public String getAuthority(){
        return "ADD_" + this.name.toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }
}
