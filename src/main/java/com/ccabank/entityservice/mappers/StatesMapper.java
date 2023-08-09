package com.ccabank.entityservice.mappers;

import com.ccabank.entityservice.dto.country.StatesDto;
import com.ccabank.entityservice.entity.States;
import org.mapstruct.Mapper;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.mappers
 * <p>
 * @date: 08/08/2023
 * @time: 11:48
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Mapper(componentModel = "spring")
public interface StatesMapper extends EntityMapper<StatesDto, States> {
}
