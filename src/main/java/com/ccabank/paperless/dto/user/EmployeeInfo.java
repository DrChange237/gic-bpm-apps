package com.ccabank.paperless.dto.user;



import com.ccabank.paperless.entity.user.Gender;
import com.ccabank.paperless.util.StringUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */

@Data
public class EmployeeInfo implements Serializable {

    private String id;
    private String userId;
    private String username;
    private Boolean firstLogin;
    private String category;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private Boolean enabled;
    private String matricule;
    private String functionalTitle;
    private String mobile;
    private int loginAttempts;
    private String avatarImg;
    private Boolean isStaff;
    private Date lastAccess;
    private Date loginTime;
    private String issueReason;
    private Date creationDate;
    private Date lastModifiedDate;
    private String createdBy;
    private String modifiedBy;
    private String codeCoreBanking;
    private Long ageInBank;
    private Long ageInFunction;
    private AgencyDto agency;
    private Date startDate;
    private String signature;
    private DepartmentInfo department;
    private EmployeeInfo supervisor;
    private EmployeeFunctionInfo function;
    private Gender gender;


    public String getReference(){

        return "DG/" + StringUtil.getFirstLetters(this.getDepartment().getDirection().getName()) + "/" + StringUtil.getFirstLetters(this.getDepartment().getName())  ;

    }

    public EmployeeInfo() {
    }


}

