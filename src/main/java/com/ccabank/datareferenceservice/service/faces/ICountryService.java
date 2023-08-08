package com.ccabank.datareferenceservice.service.faces;

import com.ccabank.datareferenceservice.domain.AppServiceResult;
import com.ccabank.datareferenceservice.dto.country.CountryDto;

import java.util.List;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : datareferenceservice
 * @Package : com.ccabank.datareferenceservice.service.faces
 * <p>
 * @date: 08/08/2023
 * @time: 11:29
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public interface ICountryService {
    AppServiceResult<List<CountryDto>> getAllCountry();

    AppServiceResult<CountryDto> getCountryById(String id);

    AppServiceResult<CountryDto> getCountryByName(String countryName);

    AppServiceResult<CountryDto> getCountryByCode(String code);

    AppServiceResult<CountryDto> getCountryByCodeIso3(String code);

    AppServiceResult<CountryDto> getCountryByPhoneCode(String code);
}
