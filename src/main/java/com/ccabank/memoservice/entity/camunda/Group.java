package com.ccabank.memoservice.entity.camunda;

import javax.persistence.*;

@Entity
@Table(name = "act_id_group")
public class Group {

    @Id
    @Basic(optional = false)
    @Column(name = "id_", columnDefinition = "serial")
    private String id;

    @Basic(optional = true)
    @Column(name = "name_")
    private String name;

    @Basic(optional = true)
    @Column(name = "type_")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
