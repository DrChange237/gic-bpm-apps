package com.change.gic.modules.business.entity;


import com.change.gic.modules.core.entity.Auditable;
import com.change.gic.modules.business.enumeration.InscriptionStatus;
import com.change.gic.modules.business.enumeration.Matrimonial;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "INSCRIPTION")
public class Inscription extends Auditable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(name = "REFERENCE", nullable = false, unique = true)
    private String reference;

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "AGENCY"), nullable = false)
    private Agency agency;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFullNameConjoint() {
        return firstNameConjoint + " " + lastNameConjoint;
    }


    @Column(name = "BIRTHDAY", nullable = false)
    private LocalDate birthday;

    @Column(name = "BIRTHPLACE", nullable = false)
    private String birthplace;

    @Column(name = "SEXE", nullable = false)
    private String sexe;

    @Column(name = "NATIONALITY", nullable = true)
    private String nationality;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "CNI_NUMBER", nullable = false)
    private String cniNumber;

    @Column(name = "CNI_DELIVERY", nullable = true)
    private LocalDate cniDelivery;

    @Column(name = "CNI_PLACE", nullable = true)
    private String cniPlace;


    @Column(name = "DIPLOMA", nullable = false)
    private String diploma;

    @Column(name = "FORMATION", nullable = true)
    private String formation;

    @Column(name = "YEAR_GRADUATION", nullable = false)
    private Integer yearGraduation;

    @Column(name = "SCHOOL", nullable = false)
    private String School;

    @Column(name = "EXPERIENCE", nullable = false)
    private Integer experience;

    @Column(name = "MATRIMONIAL", nullable = false)
    @Enumerated(EnumType.STRING)
    private Matrimonial matrimonial;

    @Column(name = "CHILDREN", nullable = true)
    private Integer children;

    @Column(name = "CHILDREN_AGE", nullable = true)
    private String childrenAge;

    @Column(name = "MOBILE", nullable = false)
    private String mobile;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "CONJOINT_FIRST_NAME", nullable = true)
    private String firstNameConjoint;

    @Column(name = "CONJOINT_LAST_NAME", nullable = true)
    private String lastNameConjoint;

    @Column(name = "CONJOINT_BIRTHDAY", nullable = true)
    private LocalDate birthdayConjoint;

    @Column(name = "CONJOINT_BIRTHPLACE", nullable = true)
    private String birthplaceConjoint;

    @Column(name = "CONJOINT_NATIONALITY", nullable = true)
    private String nationalityConjoint;

    @Column(name = "CONJOINT_MOBILE", nullable = true)
    private String mobileConjoint;

    @Column(name = "CONJOINT_EMAIL", nullable = true)
    private String emailConjoint;

    @Column(name = "CONJOINT_DIPLOMA", nullable = true)
    private String diplomaConjoint;

    @Column(name = "CONJOINT_CNI_NUMBER", nullable = true)
    private String cniNumberConjoint;

    @Column(name = "CONJOINT_CNI_DELIVERY", nullable = true)
    private LocalDate cniDeliveryConjoint;

    @Column(name = "CONJOINT_CNI_PLACE", nullable = true)
    private String cniPlaceConjoint;

    @Column(name = "CONJOINT_YEAR_GRADUATION", nullable = true)
    private Integer yearGraduationConjoint;

    @Column(name = "CONJOINT_SCHOOL", nullable = true)
    private String schoolConjoint;

    @Column(name = "CONJOINT_FORMATION", nullable = true)
    private String formationConjoint;

    @Column(name = "CONJOINT_EXPERIENCE", nullable = true)
    private Integer experienceConjoint;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private InscriptionStatus status;

}
