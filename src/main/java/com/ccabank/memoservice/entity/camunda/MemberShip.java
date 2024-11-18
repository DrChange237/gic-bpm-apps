package com.ccabank.memoservice.entity.camunda;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "act_id_membership")
public class MemberShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;


    @Basic(optional = false)
    @Column(name = "user_id_")
    private String userId;


    @Basic(optional = false)
    @Column(name = "group_id_")
    private String groupId;


    public Long getId() {
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
