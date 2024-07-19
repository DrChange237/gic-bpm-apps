package com.ccabank.memoservice.entity;


import javax.persistence.*;

@Entity
@Table(name = "T_PROCESS_UNITY")
public class ProcessUnity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String Name;

    public Long getId() {
        return id;
    }

    @Column(name = "STAFF_LIST")
    private String StaffList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStaffList() {
        return StaffList;
    }

    public void setStaffList(String staffList) {
        StaffList = staffList;
    }
}
