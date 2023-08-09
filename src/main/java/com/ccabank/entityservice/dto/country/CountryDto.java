package com.ccabank.entityservice.dto.country;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.dto.country
 * <p>
 * @date: 08/08/2023
 * @time: 11:33
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@ApiModel
public class CountryDto {

    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "CODE")
    private String code;
    @Size(min = 1, max = 10)
    private String codeIso3;
    @NotNull
    @Size(min = 1, max = 45)
    private String countryName;
    @Size(min = 1, max = 45)
    private String phoneCode;
    private Collection<StatesDto> statesCollection;
    private boolean isActive;

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
     * Gets statesCollection.
     *
     * @return value of statesCollection
     */
    @JsonIgnore
    public Collection<StatesDto> getStatesCollection() {
        return statesCollection;
    }

    /**
     * Sets statesCollection.
     *
     * @param statesCollection value of statesCollection
     */
    public void setStatesCollection(Collection<StatesDto> statesCollection) {
        this.statesCollection = statesCollection;
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
    public void setActive(boolean isActive) {
        isActive = isActive;
    }
}
