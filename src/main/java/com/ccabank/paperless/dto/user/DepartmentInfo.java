package com.ccabank.paperless.dto.user;


import java.util.Date;

/**
 * @author : <a href="mailto:marcelin.hamidou@cca-bank.com">Marcelin HAMIDOU NDAM</a>
 * @project : email-service
 * @Package : com.ccabank.userservice.dto.user
 * <p>
 * @date: 14/12/2023
 * @time: 11:50
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class DepartmentInfo {

    private String id;
    private String name;
    private String description;
    private String headOfDepartment;
    private DirectionInfo direction;
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
     * Gets headOfDepartment.
     *
     * @return value of headOfDepartment
     */
    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    /**
     * Sets headOfDepartment.
     *
     * @param headOfDepartment value of headOfDepartment
     */
    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    /**
     * Gets direction.
     *
     * @return value of direction
     */
    public DirectionInfo getDirection() {
        return direction;
    }

    /**
     * Sets direction.
     *
     * @param direction value of direction
     */
    public void setDirection(DirectionInfo direction) {
        this.direction = direction;
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

    @Override
    public String toString() {
        return "FunctionInfo{" + "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

