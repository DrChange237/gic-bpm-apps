package com.ccabank.paperless.entity.camunda;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


public class MemberShip {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Basic(optional = false)
    @Column(name = "id_")
    private String id;

    @Basic(optional = false)
    @Column(name = "user_id_")
    private String userId;


    @Basic(optional = false)
    @Column(name = "group_id_")
    private String groupId;


    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
