package com.ccabank.entityservice.dto.country;

import com.ccabank.entityservice.entity.City;
import com.ccabank.entityservice.entity.Country;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.dto.country
 * <p>
 * @date: 08/08/2023
 * @time: 11:39
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@ApiModel
public class StatesDto {

    private String id;
    @NotNull(message = "name cannot be null")
    @Size(min = 1, max = 150)
    private String name;
    @NotNull(message = "country cannot be null")
    private Country country;
    private Collection<City> cityCollection;

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
}
