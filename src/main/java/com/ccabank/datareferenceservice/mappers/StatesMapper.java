package com.ccabank.datareferenceservice.mappers;

import com.ccabank.datareferenceservice.dto.country.StatesDto;
import com.ccabank.datareferenceservice.entity.States;
import org.mapstruct.Mapper;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : datareferenceservice
 * @Package : com.ccabank.datareferenceservice.mappers
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
