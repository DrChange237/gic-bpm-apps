package com.ccabank.memoservice.dto.user;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author : <a href="mailto:marcelin.hamidou@cca-bank.com">Marcelin HAMIDOU NDAM</a>
 * @project : user-service
 * @Package : com.ccabank.userservice.dto.user
 * <p>
 * @date: 10/08/2023
 * @time: 15:54
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@ApiModel
public class AgencyDto implements Serializable {

    private String id;
    @NotNull(message = "code cannot be null")
    @Size(min = 1, max = 25)
    private String code;
    @Size(max = 45)
    private String name;
    @Size(max = 255)
    private String mobile;
    @Size(max = 255)
    private String email;
    @Size(max = 255)
    private String address;
    @Size(max = 65535)
    private String description;

    public AgencyDto() {
    }

    public AgencyDto(String code, String name, String mobile,
                     String email, String address, String description) {
        this.code = code;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.description = description;
    }

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
     * Gets mobile.
     *
     * @return value of mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets mobile.
     *
     * @param mobile value of mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets email.
     *
     * @return value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email value of email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets address.
     *
     * @return value of address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address value of address
     */
    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgencyDto entity = (AgencyDto) o;
        return Objects.equals(this.code, entity.code) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.mobile, entity.mobile) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.address, entity.address) &&
                Objects.equals(this.description, entity.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, mobile, email, address, description);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "code = " + code + ", " +
                "name = " + name + ", " +
                "mobile = " + mobile + ", " +
                "email = " + email + ", " +
                "address = " + address + ", " +
                "description = " + description + ")";
    }
}
