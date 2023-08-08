package com.ccabank.datareferenceservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : datareferenceservice
 * @Package : com.ccabank.datareferenceservice.entity
 * <p>
 * @date: 08/08/2023
 * @time: 11:05
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_COUNTRY")
public class Country implements Serializable {

    // `private Long id;` déclare une variable d'instance privée `id` de type `Long` dans la classe
    // `Country`. Cette variable représente l'identifiant unique d'un objet `Country` et est annotée par
    // `@Id` pour indiquer qu'il s'agit de la clé primaire de la table de base de données correspondante.
    // Il est également annoté avec `@GeneratedValue` pour spécifier que la valeur de ce champ sera
    // automatiquement générée par la base de données lorsqu'un nouvel objet `Country` est persisté.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;


    // `code chaîne privée;` déclare une variable d'instance privée `code` de type `String` dans la
    // classe `Country`. Cette variable représente le code d'un pays et est annotée par `@Column(name =
    // "CODE")` pour indiquer qu'elle correspond à une colonne nommée "CODE" dans la table de base de
    // données correspondante. Il est également annoté avec `@NotNull` et `@Size(min = 1, max = 10)` pour
    // spécifier que la valeur de ce champ ne peut pas être nulle et doit être comprise entre 1 et 10
    // caractères.
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "CODE")
    private String code;

    // `private String codeIso3;` déclare une variable d'instance privée `codeIso3` de type `String` dans
    // la classe `Country`. Cette variable représente le code ISO 3 d'un pays et est annotée par
    // `@Column(name="CODE_ISO3")` pour indiquer qu'elle correspond à une colonne nommée "CODE_ISO3" dans
    // la table de base de données correspondante. Il est également annoté avec `@Size(min = 1, max =
    // 10)` pour spécifier que la valeur de ce champ doit être comprise entre 1 et 10 caractères.
    @Size(min = 1, max = 10)
    @Column(name = "CODE_ISO3")
    private String codeIso3;

    // `private String countryName;` déclare une variable d'instance privée `countryName` de type
    // `String` dans la classe `Country`. Cette variable représente le nom d'un pays et est annotée par
    // `@Column(name = "COUNTRY_NAME")` pour indiquer qu'elle correspond à une colonne nommée
    // "COUNTRY_NAME" dans la table de base de données correspondante. Il est également annoté avec
    // `@NotNull` et `@Size(min = 1, max = 45)` pour spécifier que la valeur de ce champ ne peut pas être
    // nulle et doit être comprise entre 1 et 45 caractères.
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COUNTRY_NAME")
    private String countryName;

    // `private String phoneCode;` déclare une variable d'instance privée `phoneCode` de type `String`
    // dans la classe `Country`. Cette variable représente l'indicatif téléphonique d'un pays et est
    // annotée par `@Column(name = "PHONE_CODE")` pour indiquer qu'elle correspond à une colonne nommée
    // "PHONE_CODE" dans la table de base de données correspondante. Il est également annoté avec
    // `@Size(min = 1, max = 45)` pour préciser que la valeur de ce champ doit être comprise entre 1 et
    // 45 caractères.
    @Size(min = 1, max = 45)
    @Column(name = "PHONE_CODE")
    private String phoneCode;

    //private boolean isActive; déclare une variable d'instance privée 'isactive' de type 'boolean'
    // dans la classe 'Country'. Cette variable représenate l'état de actif du pays et est annotée par
    // @Column(name = "IS_ACTIVE") pour indiquer qu'elle correspond à une colonne nommée "IS_ACTIVE"dans
    // la table de la base de données correspondante.
    @Column(name = "IS_ACTIVE")
    private boolean isActive;

    // `private Collection<States> statesCollection;` déclare une variable d'instance privée
    // `statesCollection` de type `Collection<States>` dans la classe `Country`. Cette variable
    // représente une collection d'objets "États" associés à un objet "Pays" particulier. Il est annoté
    // avec `@OneToMany` pour indiquer une relation un-à-plusieurs entre les entités `Country` et
    // `States`. L'attribut `cascade` spécifie que toute modification apportée à un objet `Country` doit
    // être propagée à ses objets `States` associés. L'attribut `mappedBy` spécifie le nom du champ dans
    // la classe `States` qui correspond à l'objet `Country`. L'attribut `fetch` spécifie que les objets
    // `States` doivent être chargés paresseusement lorsqu'ils sont accédés via la variable
    // `statesCollection`.
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "country", fetch = FetchType.LAZY)
    private Collection<States> statesCollection;

//  @OneToMany(cascade = CascadeType.ALL, mappedBy = "country", fetch = FetchType.LAZY)
//  private Collection<Organization> organizationCollection;

    public Country() {
    }

    /**
     * Gets id.
     *
     * @return value of id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id value of id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets code.
     *
     * @return value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code value of code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets codeIso3.
     *
     * @return value of codeIso3
     */
    public String getCodeIso3() {
        return codeIso3;
    }

    /**
     * Sets codeIso3.
     *
     * @param codeIso3 value of codeIso3
     */
    public void setCodeIso3(String codeIso3) {
        this.codeIso3 = codeIso3;
    }

    /**
     * Gets countryName.
     *
     * @return value of countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets countryName.
     *
     * @param countryName value of countryName
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * Gets phoneCode.
     *
     * @return value of phoneCode
     */
    public String getPhoneCode() {
        return phoneCode;
    }

    /**
     * Sets phoneCode.
     *
     * @param phoneCode value of phoneCode
     */
    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    /**
     * Gets isActive.
     *
     * @return value of isActive
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets isActive.
     *
     * @param isActive value of isActive
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Gets statesCollection.
     *
     * @return value of statesCollection
     */
    @XmlTransient
    @JsonIgnore
    public Collection<States> getStatesCollection() {
        return statesCollection;
    }

    /**
     * Sets statesCollection.
     *
     * @param statesCollection value of statesCollection
     */
    public void setStatesCollection(
            Collection<States> statesCollection) {
        this.statesCollection = statesCollection;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", codeIso3='" + codeIso3 + '\'' +
                ", countryName='" + countryName + '\'' +
                ", phoneCode='" + phoneCode + '\'' +
                '}';
    }
}
