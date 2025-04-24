package com.ccabank.paperless.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "T_APPROBAL_KEY")
public class ApprovalKey {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "USERNAME")
    @Size(max = 255)
    private String username;

    @Column(name = "TASK_ID")
    @Size(max = 255)
    private String taskId;

    @Column(name = "REFERENCE")
    @Size(max = 255)
    private String reference;


    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public @Size(max = 255) String getReference() {
        return reference;
    }

    public void setReference(@Size(max = 255) String reference) {
        this.reference = reference;
    }


}
