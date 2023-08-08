package com.ccabank.datareferenceservice.service.faces;

import com.ccabank.datareferenceservice.domain.AppServiceResult;
import com.ccabank.datareferenceservice.dto.country.CityDto;

import java.util.List;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : datareferenceservice
 * @Package : com.ccabank.datareferenceservice.service.faces
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
