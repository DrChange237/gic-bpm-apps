package com.ccabank.entityservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.entity
 * <p>
 * @date: 08/08/2023
 * @time: 11:05
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "T_CITY")
@XmlRootElement
public class City implements Serializable {

    // `private Long id;` déclare une variable d'instance privée `id` de type `Long` dans la classe
    // `City`. Cette variable représente l'identifiant unique d'une ville et est annotée par `@Id` pour
    // indiquer qu'il s'agit de la clé primaire de la table de base de données correspondante. Il est
    // également annoté avec `@GeneratedValue` pour spécifier que la valeur de ce champ sera générée
    // automatiquement par la base de données lors de l'insertion d'un nouvel enregistrement.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", columnDefinition = "serial")
    private Long id;

    // `private String name;` déclare une variable d'instance privée `name` de type `String` dans la
    // classe `City`. Cette variable représente le nom d'une ville et est annotée par `@Column` pour
    // spécifier le nom de la colonne correspondante dans la table de la base de données. Il est
    // également annoté avec `@NotNull` et `@Size` pour appliquer des contraintes de validation sur la
    // longueur et la nullité du champ de nom.
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "NAME")
    private String name;

    // `Private States states;` déclare une variable d'instance privée `states` de type `States` dans la
    // classe `City`. Cette variable représente l'état auquel appartient la ville et est annotée par
    // `@ManyToOne` pour indiquer que plusieurs villes peuvent appartenir à un seul état. Il est
    // également annoté avec `@JoinColumn` pour spécifier la colonne de clé étrangère dans la table
    // `T_CITY` qui fait référence à la colonne de clé primaire dans la table `T_STATES`.
    @JoinColumn(name = "STATES", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private States states;

    public City() {
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
     * Gets states.
     *
     * @return value of states
     */
    public States getStates() {
        return states;
    }

    /**
     * Sets states.
     *
     * @param states value of states
     */
    public void setStates(States states) {
        this.states = states;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
