package com.ccabank.memoservice.entity;


import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "T_FILE")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @JoinColumn(name = "FIELD", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Field field;

    @Column(name = "URL")
    @Size(max = 255)
    private String url;

    @Column(name = "NAME")
    @Size(max = 255)
    private String name;

    @Column(name = "TYPE")
    @Size(max = 255)
    private String type;

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
