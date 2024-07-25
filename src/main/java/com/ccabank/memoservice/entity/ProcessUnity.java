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

    @Column(name = "CODE", unique = true)
    private String code;

    @Column(name = "NAME")
    private String name;

    public Long getId() {
        return id;
    }


    public ProcessUnity() {
    }

    public ProcessUnity(String code, String name, String staffList) {
        this.code = code;
        this.name = name;
        this.staffList = staffList;
    }

    @Column(name = "STAFF_LIST")
    private String staffList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaffList() {
        return staffList;
    }

    public void setStaffList(String staffList) {
        this.staffList = staffList;
    }
}
