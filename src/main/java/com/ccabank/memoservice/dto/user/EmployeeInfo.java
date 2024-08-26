package com.ccabank.memoservice.dto.user;



import com.ccabank.memoservice.util.StringUtil;

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

    public String getReference(){

        return "DG/" + StringUtil.getFirstLetters(this.getDepartment().getDirection().getName()) + "/" + StringUtil.getFirstLetters(this.getDepartment().getName()) + "/" + LocalDate.now().getYear() ;

    }

    public EmployeeInfo() {
    }

    /**
     * Gets id.
     *
     * @return value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id value of id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets userId.
     *
     * @return value of userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets userId.
     *
     * @param userId value of userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets username.
     *
     * @return value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets firstLogin.
     *
     * @return value of firstLogin
     */
    public Boolean getFirstLogin() {
        return firstLogin;
    }

    /**
     * Sets firstLogin.
     *
     * @param firstLogin value of firstLogin
     */
    public void setFirstLogin(Boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    /**
     * Gets firstName.
     *
     * @return value of firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets firstName.
     *
     * @param firstName value of firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets lastName.
     *
     * @return value of lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets lastName.
     *
     * @param lastName value of lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets dateOfBirth.
     *
     * @return value of dateOfBirth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets dateOfBirth.
     *
     * @param dateOfBirth value of dateOfBirth
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets category.
     *
     * @return value of category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category value of category
     */
    public void setCategory(String category) {
        this.category = category;
    }


    /**
     * Gets email.
     *
     * @return value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email value of email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets enabled.
     *
     * @return value of enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Sets enabled.
     *
     * @param enabled value of enabled
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }


    /**
     * Gets matricule.
     *
     * @return value of matricule
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * Sets matricule.
     *
     * @param matricule value of matricule
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * Gets functionalTitle.
     *
     * @return value of functionalTitle
     */
    public String getFunctionalTitle() {
        return functionalTitle;
    }

    /**
     * Sets functionalTitle.
     *
     * @param functionalTitle value of functionalTitle
     */
    public void setFunctionalTitle(String functionalTitle) {
        this.functionalTitle = functionalTitle;
    }

    /**
     * Gets mobile.
     *
     * @return value of mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets mobile.
     *
     * @param mobile value of mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets loginAttempts.
     *
     * @return value of loginAttempts
     */
    public int getLoginAttempts() {
        return loginAttempts;
    }

    /**
     * Sets loginAttempts.
     *
     * @param loginAttempts value of loginAttempts
     */
    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    /**
     * Gets avatarImg.
     *
     * @return value of avatarImg
     */
    public String getAvatarImg() {
        return avatarImg;
    }

    /**
     * Sets avatarImg.
     *
     * @param avatarImg value of avatarImg
     */
    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }

    /**
     * Gets isStaff.
     *
     * @return value of isStaff
     */
    public Boolean getStaff() {
        return isStaff;
    }

    /**
     * Sets isStaff.
     *
     * @param isStaff value of isStaff
     */
    public void setStaff(Boolean staff) {
        isStaff = staff;
    }

    /**
     * Gets lastAccess.
     *
     * @return value of lastAccess
     */
    public Date getLastAccess() {
        return lastAccess;
    }

    /**
     * Sets lastAccess.
     *
     * @param lastAccess value of lastAccess
     */
    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    /**
     * Gets loginTime.
     *
     * @return value of loginTime
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * Sets loginTime.
     *
     * @param loginTime value of loginTime
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * Gets issueReason.
     *
     * @return value of issueReason
     */
    public String getIssueReason() {
        return issueReason;
    }

    /**
     * Sets issueReason.
     *
     * @param issueReason value of issueReason
     */
    public void setIssueReason(String issueReason) {
        this.issueReason = issueReason;
    }

    /**
     * Gets creationDate.
     *
     * @return value of creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets creationDate.
     *
     * @param creationDate value of creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets lastModifiedDate.
     *
     * @return value of lastModifiedDate
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets lastModifiedDate.
     *
     * @param lastModifiedDate value of lastModifiedDate
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Gets createdBy.
     *
     * @return value of createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets createdBy.
     *
     * @param createdBy value of createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets modifiedBy.
     *
     * @return value of modifiedBy
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Sets modifiedBy.
     *
     * @param modifiedBy value of modifiedBy
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * Gets codeCoreBanking.
     *
     * @return value of codeCoreBanking
     */
    public String getCodeCoreBanking() {
        return codeCoreBanking;
    }

    /**
     * Sets codeCoreBanking.
     *
     * @param codeCoreBanking value of codeCoreBanking
     */
    public void setCodeCoreBanking(String codeCoreBanking) {
        this.codeCoreBanking = codeCoreBanking;
    }

    /**
     * Gets ageInBank.
     *
     * @return value of ageInBank
     */
    public Long getAgeInBank() {
        return ageInBank;
    }

    /**
     * Sets ageInBank.
     *
     * @param ageInBank value of ageInBank
     */
    public void setAgeInBank(Long ageInBank) {
        this.ageInBank = ageInBank;
    }

    /**
     * Gets ageInFunction.
     *
     * @return value of ageInFunction
     */
    public Long getAgeInFunction() {
        return ageInFunction;
    }

    /**
     * Sets ageInFunction.
     *
     * @param ageInFunction value of ageInFunction
     */
    public void setAgeInFunction(Long ageInFunction) {
        this.ageInFunction = ageInFunction;
    }

    /**
     * Gets agency.
     *
     * @return value of agency
     */
    public AgencyDto getAgency() {
        return agency;
    }

    /**
     * Sets agency.
     *
     * @param agency value of agency
     */
    public void setAgency(AgencyDto agency) {
        this.agency = agency;
    }

    /**
     * Gets startDate.
     *
     * @return value of startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets startDate.
     *
     * @param startDate value of startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets signature.
     *
     * @return value of signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets signature.
     *
     * @param signature value of signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Gets department.
     *
     * @return value of department
     */
    public DepartmentInfo getDepartment() {
        return department;
    }

    /**
     * Sets department.
     *
     * @param department value of department
     */
    public void setDepartment(DepartmentInfo department) {
        this.department = department;
    }

    /**
     * Gets supervisor.
     *
     * @return value of supervisor
     */
    public EmployeeInfo getSupervisor() {
        return supervisor;
    }

    /**
     * Sets supervisor.
     *
     * @param supervisor value of supervisor
     */
    public void setSupervisor(EmployeeInfo supervisor) {
        this.supervisor = supervisor;
    }




}

