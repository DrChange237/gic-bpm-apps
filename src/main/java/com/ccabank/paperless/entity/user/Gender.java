package com.ccabank.paperless.entity.user;

public enum Gender {

    MALE("MALE"),
    FEMALE("FEMALE"),
    OTHER("OTHER");
    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }


    public String getGender() {
        return gender;
    }

}
