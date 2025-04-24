package com.ccabank.memoservice.entity.user;

public enum Gender {

    MALE("MALE"),
    FEMALE("FEMALE"),
    OTHER("OTHER");
    private final String gender;

    /**
     * @param gender
     */
    private Gender(String gender) {
        this.gender = gender;
    }

    /**
     * This function returns the gender of an object.
     *
     * @return The method is returning the value of the `gender` variable.
     */
    public String getGender() {
        return gender;
    }

}
