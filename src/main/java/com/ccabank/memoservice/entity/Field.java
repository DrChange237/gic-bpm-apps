package com.ccabank.memoservice.entity;


import com.ccabank.memoservice.util.field.FieldUtils;
import com.sun.jersey.core.impl.provider.entity.XMLRootObjectProvider;
import org.apache.commons.lang3.StringUtils;

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

    @JoinColumn(name = "REQUEST", referencedColumnName = "ID",  nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Request request;

    @JoinColumn(name = "APPROVAL", referencedColumnName = "ID", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Approval approval;

    @Column(name = "KEY")
    private String key;

    @Column(name = "VALUE")
    private String value;

    @Transient
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        String name = FieldUtils.getNameOfField(this.request.getType().getStructure(), this.getKey());
        if(name == null){
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = StringUtils.defaultString(name);
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Approval getApproval() {
        return approval;
    }

    public void setApproval(Approval approval) {
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
