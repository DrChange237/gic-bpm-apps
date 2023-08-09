package com.ccabank.entityservice.service.faces;

import com.ccabank.entityservice.domain.AppServiceResult;
import com.ccabank.entityservice.dto.country.StatesDto;

import java.util.List;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.service.faces
 * <p>
 * @date: 08/08/2023
 * @time: 16:54
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */

public interface IStatesService {

    AppServiceResult<List<StatesDto>> getAllStates();

    AppServiceResult<StatesDto> getStatesById(String id);

    AppServiceResult<StatesDto> getStatesByName(String name);

    AppServiceResult<StatesDto> getStateByCity(String countryId);
}
