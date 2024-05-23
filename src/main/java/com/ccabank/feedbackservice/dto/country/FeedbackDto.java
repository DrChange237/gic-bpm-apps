package com.ccabank.feedbackservice.dto.country;


import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel()
public class FeedbackDto {

    private String id;

    @NotNull(message = "staffUsername cannot be null")
    @Size(min = 1, max = 150)
    private String staffUsername;

    @NotNull(message = "fullname cannot be null")
    @Size(min = 1, max = 150)
    private String fullname;

    @NotNull(message = "mobile cannot be null")
    @Size(min = 1, max = 15)
    private String mobile;

    @Size(min = 1, max = 150)
    private String email;

    private boolean haveAccount;

    @Size(min = 1, max = 150)
    private String branch;

    @NotNull(message = "Cause of visit cannot be null")
    @Size(min = 1, max = 150)
    private String visitCause;

    private int clarteInstructions;
    private int qualityOfWelcome;
    private int professionalism;
    private int speed;
    private int formAvaibility;
    private int climatisation;
    private int waitingTime;
    private int comfort;
    private int toiletCleanliness;
    private int cleanliness;
    private int otherStaffMember;
    private String comments;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getStaffUsername() {
        return staffUsername;
    }

    public void setStaffUsername(String staffUsername) {
        this.staffUsername = staffUsername;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isHaveAccount() {
        return haveAccount;
    }

    public void setHaveAccount(boolean haveAccount) {
        this.haveAccount = haveAccount;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getVisitCause() {
        return visitCause;
    }

    public void setVisitCause(String visitCause) {
        this.visitCause = visitCause;
    }

    public int getClarteInstructions() {
        return clarteInstructions;
    }

    public void setClarteInstructions(int clarteInstructions) {
        this.clarteInstructions = clarteInstructions;
    }

    public int getQualityOfWelcome() {
        return qualityOfWelcome;
    }

    public void setQualityOfWelcome(int qualityOfWelcome) {
        this.qualityOfWelcome = qualityOfWelcome;
    }

    public int getProfessionalism() {
        return professionalism;
    }

    public void setProfessionalism(int professionalism) {
        this.professionalism = professionalism;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getFormAvaibility() {
        return formAvaibility;
    }

    public void setFormAvaibility(int formAvaibility) {
        this.formAvaibility = formAvaibility;
    }

    public int getClimatisation() {
        return climatisation;
    }

    public void setClimatisation(int climatisation) {
        this.climatisation = climatisation;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getComfort() {
        return comfort;
    }

    public void setComfort(int comfort) {
        this.comfort = comfort;
    }

    public int getToiletCleanliness() {
        return toiletCleanliness;
    }

    public void setToiletCleanliness(int toiletCleanliness) {
        this.toiletCleanliness = toiletCleanliness;
    }

    public int getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(int cleanliness) {
        this.cleanliness = cleanliness;
    }

    public int getOtherStaffMember() {
        return otherStaffMember;
    }

    public void setOtherStaffMember(int otherStaffMember) {
        this.otherStaffMember = otherStaffMember;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
