package com.ccabank.memoservice.entity;


import com.ccabank.memoservice.util.field.FieldUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

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

    @Column(name = "VALUE", length = 5000)
    private String value;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "POSITION")
    private int position;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "field", fetch = FetchType.LAZY)
    private Collection<File> files = new ArrayList<>();

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Collection<File> getFiles() {
        return files;
    }

    public void setFiles(Collection<File> files) {
        this.files = files;
    }
}
