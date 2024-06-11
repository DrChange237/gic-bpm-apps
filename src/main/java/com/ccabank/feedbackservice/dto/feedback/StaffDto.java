package com.ccabank.feedbackservice.dto.feedback;

public class StaffDto {

    private String username;

    private String position;

    private AgencyDto agency;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public AgencyDto getAgency() {
        return agency;
    }

    public void setAgency(AgencyDto agency) {
        this.agency = agency;
    }
}
