package com.ccabank.datareferenceservice.service.impl;

import com.ccabank.datareferenceservice.constant.AppError;
import com.ccabank.datareferenceservice.domain.AppServiceResult;
import com.ccabank.datareferenceservice.dto.country.CityDto;
import com.ccabank.datareferenceservice.entity.City;
import com.ccabank.datareferenceservice.mappers.CityMapper;
import com.ccabank.datareferenceservice.repository.CityRepository;
import com.ccabank.datareferenceservice.service.faces.ICityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ccabank.datareferenceservice.constant.BeanIdConstant.CITY_DETAIL_SERVICE;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : datareferenceservice
 * @Package : com.ccabank.datareferenceservice.service.impl
 * <p>
 * @date: 08/08/2023
 * @time: 17:48
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
@Qualifier(CITY_DETAIL_SERVICE)
public class CityService implements ICityService {
    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CityMapper cityMapper;

    @Override
    public AppServiceResult<List<CityDto>> getAllCity() {
        try {
            logger.info(CITY_DETAIL_SERVICE, "getAllCity : methode invocation");
            List<City> cities = cityRepository.findAll();

            return getConvertedResult(cities, "getAllCity ");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(CITY_DETAIL_SERVICE, "getAllCity : Exception ", e.getMessage());
            return new AppServiceResult<List<CityDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<CityDto> getCityById(String id) {
        try {
            logger.info(CITY_DETAIL_SERVICE, "getCityById : methode invocation", id);
            City city = cityRepository.findById(Long.parseLong(id)).orElse(null);
            if (city == null) {
                logger.warn(CITY_DETAIL_SERVICE, "getCityById",
                        "City not exist!, Cannot further process!");
                return new AppServiceResult<CityDto>(false, AppError.Validattion.errorCode(),
                        "City not exist!", null);
            }
            return new AppServiceResult<CityDto>(true, 0, "Succeed!", cityMapper.toDto(city));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(CITY_DETAIL_SERVICE, "getCityById : Exception ", e.getMessage());
            return new AppServiceResult<CityDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<CityDto> getCityByName(String name) {
        try {
            logger.info(CITY_DETAIL_SERVICE, "getCityByName : methode invocation", name);
            City city = cityRepository.getCityByName(name);
            if (city == null) {
                logger.warn(CITY_DETAIL_SERVICE, "getCityByName",
                        "City not exist!, Cannot further process!");
                return new AppServiceResult<CityDto>(false, AppError.Validattion.errorCode(),
                        "City not exist!", null);
            }
            return new AppServiceResult<CityDto>(true, 0, "Succeed!", cityMapper.toDto(city));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(CITY_DETAIL_SERVICE, "getCityByName : Exception ", e.getMessage());
            return new AppServiceResult<CityDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<List<CityDto>> getCityByState(String stateId) {
        try {
            logger.info(CITY_DETAIL_SERVICE, "getAllCity : methode invocation");
            List<City> cities = cityRepository.getCityByState(Long.parseLong(stateId));

            return getConvertedResult(cities, "getAllCity ");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(CITY_DETAIL_SERVICE, "getAllCity : Exception ", e.getMessage());
            return new AppServiceResult<List<CityDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    private AppServiceResult<List<CityDto>> getConvertedResult(List<City> cities, String functionName) {
        if (cities == null) {
            logger.warn(CITY_DETAIL_SERVICE, functionName,
                    "City not exist!, Cannot further process!");
            return new AppServiceResult<List<CityDto>>(false, AppError.Validattion.errorCode(),
                    "City not exist!", null);
        }
        List<CityDto> result = new ArrayList<CityDto>();
        if (cities.size() > 0) {
            for (City city : cities) {
                result.add(cityMapper.toDto(city));
            }
        }
        return new AppServiceResult<List<CityDto>>(true, 0, "Succeed!", result);
    }
}
