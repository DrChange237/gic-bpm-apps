package com.ccabank.memoservice.entity;


import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "T_FIELD")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @JoinColumn(name = "REQUEST", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Request request;

    @JoinColumn(name = "APPROVAL", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Request approval;

    @Column(name = "KEY")
    private String key;

    @Column(name = "VALUE")
    private String value;

    public Long getId() {
        return id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getApproval() {
        return approval;
    }

    public void setApproval(Request approval) {
        this.approval = approval;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
