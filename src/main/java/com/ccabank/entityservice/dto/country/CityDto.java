package com.ccabank.entityservice.dto.country;

import com.ccabank.entityservice.entity.States;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.dto.country
 * <p>
 * @date: 08/08/2023
 * @time: 11:43
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@ApiModel()
public class CityDto {

    private String id;
    @NotNull(message = "name cannot be null")
    @Size(min = 1, max = 150)
    private String name;
    @NotNull(message = "state cannot be null")
    private States states;

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
}
