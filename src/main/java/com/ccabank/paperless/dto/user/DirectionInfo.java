package com.ccabank.paperless.dto.user;

import java.util.Date;

public class DirectionInfo {

    private String id;
    private String name;
    private String description;
    private String director;
    private Date creationDate;
    private Date lastModifiedDate;

    /**
     * Gets id.
     *
     * @return value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id value of id
     */
    public void setId(String id) {
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
     * Gets description.
     *
     * @return value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description value of description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets director.
     *
     * @return value of director
     */
    public String getDirector() {
        return director;
    }

    /**
     * Sets director.
     *
     * @param director value of director
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Gets creationDate.
     *
     * @return value of creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets creationDate.
     *
     * @param creationDate value of creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets lastModifiedDate.
     *
     * @return value of lastModifiedDate
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets lastModifiedDate.
     *
     * @param lastModifiedDate value of lastModifiedDate
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}

