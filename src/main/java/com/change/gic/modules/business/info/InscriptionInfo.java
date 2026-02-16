package com.change.gic.modules.business.info;

import com.change.gic.modules.business.enumeration.InscriptionStatus;
import com.change.gic.modules.business.enumeration.Matrimonial;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InscriptionInfo extends AuditableInfo {

    private String id;

    private String reference;

    private AgencyInfo agency;

    private String firstName;

    private String lastName;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFullNameConjoint() {
        return firstNameConjoint + " " + lastNameConjoint;
    }

    private LocalDate birthday;

    private String birthplace;

    private String sexe;

    private String nationality;

    private String address;

    private String cniNumber;

    private LocalDate cniDelivery;

    private String cniPlace;

    private String diploma;

    private String formation;

    private Integer yearGraduation;

    private String School;

    private Integer experience;

    private Matrimonial matrimonial;

    private Integer children;

    private String childrenAge;

    private String mobile;

    private String email;

    private String firstNameConjoint;

    private String lastNameConjoint;

    private LocalDate birthdayConjoint;

    private String birthplaceConjoint;

    private String nationalityConjoint;

    private String mobileConjoint;

    private String emailConjoint;

    private String diplomaConjoint;

    private String cniNumberConjoint;

    private LocalDate cniDeliveryConjoint;

    private String cniPlaceConjoint;

    private Integer yearGraduationConjoint;

    private String schoolConjoint;

    private String formationConjoint;

    private Integer experienceConjoint;

    private InscriptionStatus status;
}
