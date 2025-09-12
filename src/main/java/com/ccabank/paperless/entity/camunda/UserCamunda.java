package com.ccabank.paperless.entity.camunda;


import javax.persistence.*;


public class UserCamunda  {

    @Id
    @Basic(optional = false)
    @Column(name = "id_")
    private String userName;

    @Basic(optional = true)
    @Column(name = "first_")
    private String firstName;

    @Basic(optional = true)
    @Column(name = "last_")
    private String lastName;

    @Basic(optional = true)
    @Column(name = "email_")
    private String email;

    @Basic(optional = true)
    @Column(name = "pwd_")
    private String password;

    public String getId() {
        return userName;
    }

    public void setId(String userName) {
        this.userName= userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
