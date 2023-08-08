package com.ccabank.datareferenceservice.service.impl;

import com.ccabank.datareferenceservice.constant.AppError;
import com.ccabank.datareferenceservice.domain.AppServiceResult;
import com.ccabank.datareferenceservice.dto.country.CountryDto;
import com.ccabank.datareferenceservice.entity.Country;
import com.ccabank.datareferenceservice.mappers.CountryMapper;
import com.ccabank.datareferenceservice.repository.CountryRepository;
import com.ccabank.datareferenceservice.service.faces.ICountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ccabank.datareferenceservice.constant.BeanIdConstant.COUNTRY_DETAIL_SERVICE;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : datareferenceservice
 * @Package : com.ccabank.datareferenceservice.service.impl
 * <p>
 * @date: 08/08/2023
 * @time: 11:28
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
@Qualifier(COUNTRY_DETAIL_SERVICE)
public class CountryService implements ICountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CountryMapper countryMapper;

    @Override
    public AppServiceResult<List<CountryDto>> getAllCountry() {
        try {
            logger.info(COUNTRY_DETAIL_SERVICE, "getAllCountry : methode invocation");
            List<Country> countries = countryRepository.findAll();

            return getConvertedResult(countries, "getAllCountry ");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(COUNTRY_DETAIL_SERVICE, "getAllCountry : Exception ", e.getMessage());
            return new AppServiceResult<List<CountryDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }

    }

    @Override
    public AppServiceResult<CountryDto> getCountryById(String id) {
        try {
            logger.info(COUNTRY_DETAIL_SERVICE, "getCountryById : methode invocation", id);
            Country country = countryRepository.findById(Long.parseLong(id)).orElse(null);
            if (country == null) {
                logger.warn(COUNTRY_DETAIL_SERVICE, "getCountryById",
                        "Country not exist!, Cannot further process!");
                return new AppServiceResult<CountryDto>(false, AppError.Validattion.errorCode(),
                        "Country not exist!", null);
            }
            return new AppServiceResult<CountryDto>(true, 0, "Succeed!", countryMapper.toDto(country));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(COUNTRY_DETAIL_SERVICE, "getCountryById : Exception ", e.getMessage());
            return new AppServiceResult<CountryDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<CountryDto> getCountryByName(String countryName) {
        try {
            logger.info(COUNTRY_DETAIL_SERVICE, "getCountryByName : methode invocation", countryName);
            Country country = countryRepository.getCountryByName(countryName);
            if (country == null) {
                logger.warn(COUNTRY_DETAIL_SERVICE, "getCountryByName",
                        "Country not exist!, Cannot further process!");
                return new AppServiceResult<CountryDto>(false, AppError.Validattion.errorCode(),
                        "Country not exist!", null);
            }
            return new AppServiceResult<CountryDto>(true, 0, "Succeed!", countryMapper.toDto(country));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(COUNTRY_DETAIL_SERVICE, "getCountryByName : Exception ", e.getMessage());
            return new AppServiceResult<CountryDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<CountryDto> getCountryByCode(String code) {
        try {
            logger.info(COUNTRY_DETAIL_SERVICE, "getCountryByCode : methode invocation");
            Country country = countryRepository.getCountryByCode(code);
            if (country == null) {
                logger.warn(COUNTRY_DETAIL_SERVICE, "getCountryByCode",
                        "Country not exist!, Cannot further process!");
                return new AppServiceResult<CountryDto>(false, AppError.Validattion.errorCode(),
                        "Country not exist!", null);
            }
            return new AppServiceResult<CountryDto>(true, 0, "Succeed!", countryMapper.toDto(country));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(COUNTRY_DETAIL_SERVICE, "getCountryByCode : Exception ", e.getMessage());
            return new AppServiceResult<CountryDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<CountryDto> getCountryByCodeIso3(String code) {
        try {
            logger.info(COUNTRY_DETAIL_SERVICE, "getCountryByCodeIso3 : methode invocation");
            Country country = countryRepository.getCountryByCodeIso3(code);
            if (country == null) {
                logger.warn(COUNTRY_DETAIL_SERVICE, "getCountryByCodeIso3",
                        "Country not exist!, Cannot further process!");
                return new AppServiceResult<CountryDto>(false, AppError.Validattion.errorCode(),
                        "Country not exist!", null);
            }
            return new AppServiceResult<CountryDto>(true, 0, "Succeed!", countryMapper.toDto(country));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(COUNTRY_DETAIL_SERVICE, "getCountryByCodeIso3 : Exception ", e.getMessage());
            return new AppServiceResult<CountryDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<CountryDto> getCountryByPhoneCode(String code) {
        try {
            logger.info(COUNTRY_DETAIL_SERVICE, "getCountryByPhoneCode : methode invocation");
            Country country = countryRepository.getCountryByPhoneCode(code);
            if (country == null) {
                logger.warn(COUNTRY_DETAIL_SERVICE, "getCountryByPhoneCode",
                        "Country not exist!, Cannot further process!");
                return new AppServiceResult<CountryDto>(false, AppError.Validattion.errorCode(),
                        "Country not exist!", null);
            }
            return new AppServiceResult<CountryDto>(true, 0, "Succeed!", countryMapper.toDto(country));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(COUNTRY_DETAIL_SERVICE, "getCountryByPhoneCode : Exception ", e.getMessage());
            return new AppServiceResult<CountryDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    private AppServiceResult<List<CountryDto>> getConvertedResult(List<Country> countries, String functionName) {
        if (countries == null) {
            logger.warn(COUNTRY_DETAIL_SERVICE, functionName,
                    "Country not exist!, Cannot further process!");
            return new AppServiceResult<List<CountryDto>>(false, AppError.Validattion.errorCode(),
                    "Country not exist!", null);
        }
        List<CountryDto> result =  new ArrayList<CountryDto>();
        if (countries.size() > 0) {
            for (Country country : countries) {
                result.add(countryMapper.toDto(country));
            }
        }
        return new AppServiceResult<List<CountryDto>>(true, 0, "Succeed!", result);
    }
}
