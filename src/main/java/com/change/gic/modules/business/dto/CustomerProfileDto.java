package com.change.gic.modules.business.dto;

import com.change.gic.modules.business.enumeration.Matrimonial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDto {

    // Informations personnelles / Personal Information
    private String reference;
    private String fullName;
    private LocalDate dateOfBirth;
    private Integer age;
    private String placeOfBirth;
    private String sex;
    private String nationality;
    private String phone;
    private String email;
    private String cniNumber;
    private LocalDate cniIssueDate;
    private String cniIssuePlace;
    private Integer numberOfChildren;
    private String childrenAges;
    private String highestDegree;
    private Integer graduationYear;
    private String school;
    private String specialty;
    private String address;


    // Situation matrimoniale / Marital Status
    private Matrimonial maritalStatus;

    // Informations conjoint / Spouse Information
    private String spouseName;
    private LocalDate spouseDateOfBirth;
    private String spousePlaceOfBirth;
    private Integer spouseAge;
    private String spouseSex;
    private String spouseNationality;
    private String spousePhone;
    private String spouseAddress;
    private String spouseEmail;
    private String spouseCniNumber;
    private LocalDate spouseCniIssueDate;
    private String spouseCniIssuePlace;
    private String spouseHighestDegree;
    private Integer spouseGraduationYear;
    private String spouseSchool;
    private String spouseSpecialty;

    // Recommandation / Recommendation
    private String referredBy;
    private String programTrack;
    private LocalDate consultationDate;

    // Photo (Base64 ou chemin)
    private String photoBase64;

    /*public enum MaritalStatus {
        MARRIED("Marié / Married"),
        SINGLE_WITHOUT_CHILD("Célibataire sans enfant / Single without a child"),
        SINGLE_WITH_CHILD("Célibataire avec enfant / Single with a child"),
        COMMON_LAW("Conjoint(e) de fait / Common-law partner");

        private final String displayName;

        MaritalStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }*/
}
