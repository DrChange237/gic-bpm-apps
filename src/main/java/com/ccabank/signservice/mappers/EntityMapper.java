package com.ccabank.signservice.mappers;

import java.util.List;
import java.util.Set;

public interface EntityMapper<D, E> {

    /**
     * La fonction convertit un objet DTO en objet entité.
     *
     * @param dto Le paramètre "dto" est un type générique représentant un objet de transfert de données.
     *            C'est un objet qui transporte des données entre des processus ou entre différentes parties d'un
     *            programme. Dans ce contexte, il est utilisé comme paramètre d'entrée pour une méthode appelée
     *            "toEntity", qui est responsable de la conversion de l'objet DTO
     * @return La méthode renvoie un objet de type "E", qui est la classe d'entité correspondant à la
     * classe DTO "D". La méthode est probablement utilisée pour convertir un objet DTO en son objet
     * entité correspondant.
     */
    E toEntity(D dto);

    /**
     * La fonction convertit un objet DTO en objet entité.
     *
     * @param entity Le paramètre "dto" est un type générique représentant un objet de transfert de données.
     *               C'est un objet qui transporte des données entre des processus ou entre différentes parties d'un
     *               programme. Dans ce contexte, il est utilisé comme paramètre d'entrée pour une méthode appelée
     *               "toEntity", qui est responsable de la conversion de l'objet DTO
     * @return La méthode renvoie un objet de type "E", qui est la classe d'entité, après avoir converti
     * l'objet d'entrée de type "D" (qui est la classe DTO) en objet d'entité.
     */
    D toDto(E entity);

    /**
     * Cette fonction convertit une liste d'objets DTO en une liste d'objets d'entité.
     *
     * @param dtoList dtoList est une liste d'objets de type D, qui représente une collection d'objets de
     *                transfert de données (DTO). Ces DTO sont généralement utilisés pour transférer des données entre
     *                différentes couches d'une application ou entre différentes applications. Dans ce cas, la méthode
     *                toEntity() prend cette liste de DTO et convertit
     * @return La méthode `toEntity` renvoie une `Liste` d'objets de type `E`, qui est la classe
     * d'entité, après avoir converti une `Liste` d'objets de type `D`, qui est la classe DTO (Data
     * Transfer Object).
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * Cette fonction convertit une liste d'entités de type E en une liste de DTO de type D.
     *
     * @param entityList Une liste d'objets de type E, qui est la classe d'entité.
     * @return La méthode renvoie une liste d'objets de type D, qui est la représentation DTO (Data
     * Transfer Object) d'une liste d'objets de type E, qui est la représentation de l'entité.
     */
    List<D> toDto(List<E> entityList);

    /**
     * Cette fonction convertit une liste d'objets DTO en une liste d'objets d'entité.
     *
     * @param entityList dtoList est une liste d'objets de type D, qui représente une collection d'objets de
     *                   transfert de données (DTO). Ces DTO sont généralement utilisés pour transférer des données entre
     *                   différentes couches d'une application ou entre différentes applications. La méthode toEntity()
     *                   prend cette liste de DTO en entrée et les convertit en
     * @return La méthode `toEntity` renvoie une `Liste` d'objets de type `E`, qui est la classe
     * d'entité. La `Liste` est créée en convertissant une `Liste` d'objets de type `D`, qui est la
     * classe DTO (Data Transfer Object).
     */
    Set<D> toDto(Set<E> entityList);
}
