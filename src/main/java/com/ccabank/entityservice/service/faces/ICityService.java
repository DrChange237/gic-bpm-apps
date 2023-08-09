package com.ccabank.entityservice.service.faces;

import com.ccabank.entityservice.domain.AppServiceResult;
import com.ccabank.entityservice.dto.country.CityDto;

import java.util.List;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.service.faces
 * <p>
 * @date: 08/08/2023
 * @time: 17:44
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public interface ICityService {

    AppServiceResult<List<CityDto>> getAllCity();

    AppServiceResult<CityDto> getCityById(String id);

    AppServiceResult<CityDto> getCityByName(String name);

    AppServiceResult<List<CityDto>> getCityByState(String stateId);
}
