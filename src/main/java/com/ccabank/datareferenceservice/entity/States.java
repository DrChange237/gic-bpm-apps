package com.ccabank.datareferenceservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : datareferenceservice
 * @Package : com.ccabank.datareferenceservice.entity
 * <p>
 * @date: 08/08/2023
 * @time: 10:57
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_STATES")
@XmlRootElement
public class States implements Serializable {

    // `private Long id;` déclare une variable d'instance privée `id` de type `Long` dans la classe
    // `States`. Cette variable représente l'identifiant unique d'un objet `States` dans la table de base
    // de données `T_STATES`. Elle est annotée `@Id` pour indiquer qu'il s'agit de la clé primaire de
    // l'entité et `@GeneratedValue` pour préciser que sa valeur sera générée automatiquement par la base
    // de données.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;


    // `private String name;` déclare une variable d'instance privée `name` de type `String` dans la
    // classe `States`. Cette variable représente le nom d'un état et est mappée à la colonne "NAME" de
    // la table "T_STATES" de la base de données. Il est annoté avec `@Basic`, `@NotNull`, `@Size` pour
    // spécifier ses contraintes et ses règles de validation.
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "NAME")
    private String name;

    // `private Country country;` déclare une variable d'instance privée `country` de type `Country` dans
    // la classe `States`. Cette variable représente le pays auquel appartient l'état et est mappée à la
    // colonne "COUNTRY" dans la table "T_STATES" de la base de données. Il est annoté avec `@JoinColumn`
    // pour spécifier la relation de clé étrangère entre les entités `States` et `Country`, et
    // `@ManyToOne` pour indiquer que plusieurs états peuvent appartenir à un même pays.
    @JoinColumn(name = "COUNTRY", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Country country;

    // `private Collection<City> cityCollection;` déclare une variable d'instance privée `cityCollection`
    // de type `Collection<City>` dans la classe `States`. Cette variable représente une collection
    // d'objets `City` qui sont associés à l'objet `States` courant. Il est mappé à la table `T_CITY`
    // dans la base de données via l'annotation `@OneToMany`, qui spécifie qu'il existe une relation
    // un-à-plusieurs entre les entités `States` et `City`. L'attribut `cascade` spécifie que toute
    // modification apportée à l'entité `States` doit être propagée aux entités `City` associées, et
    // l'attribut `fetch` spécifie que les entités `City` doivent être chargées paresseusement.
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "states", fetch = FetchType.LAZY)
    private Collection<City> cityCollection;

    public States() {
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
     * Gets name.
     *
     * @return value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets country.
     *
     * @return value of country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Sets country.
     *
     * @param country value of country
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Gets cityCollection.
     *
     * @return value of cityCollection
     */
    @XmlTransient
    @JsonIgnore
    public Collection<City> getCityCollection() {
        return cityCollection;
    }

    /**
     * Sets cityCollection.
     *
     * @param cityCollection value of cityCollection
     */
    public void setCityCollection(Collection<City> cityCollection) {
        this.cityCollection = cityCollection;
    }

    @Override
    public String toString() {
        return "States{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country=" + country +
                '}';
    }
}
