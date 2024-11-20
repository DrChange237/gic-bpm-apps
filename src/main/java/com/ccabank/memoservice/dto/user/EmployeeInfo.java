package com.ccabank.memoservice.dto.user;



import com.ccabank.memoservice.entity.user.Gender;
import com.ccabank.memoservice.util.StringUtil;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : cca-bank-microservices
 * @Package : com.ccabank.userservice.dto.employee
 * <p>
 * @date: 18/12/2023
 * @time: 19:24
 * <p>
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

