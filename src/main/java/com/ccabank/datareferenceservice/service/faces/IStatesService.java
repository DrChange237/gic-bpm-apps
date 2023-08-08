package com.ccabank.datareferenceservice.service.faces;

import com.ccabank.datareferenceservice.domain.AppServiceResult;
import com.ccabank.datareferenceservice.dto.country.StatesDto;

import java.util.List;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : datareferenceservice
 * @Package : com.ccabank.datareferenceservice.service.faces
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
