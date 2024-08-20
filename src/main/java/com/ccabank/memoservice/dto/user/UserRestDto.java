package com.ccabank.memoservice.dto.user;

import com.ccabank.memoservice.util.StringUtil;

public class UserRestDto {

    private String username;

    private String function;

    private String agencyCode;

    private String agencyName;

    private String matricule;

    private String department;

    public String getEmail(){
        //return "";
        return  this.getUsername() + "@cca-bank.com";
    }

    public String getName(){
        return  StringUtil.transformUsernameToName(username);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
