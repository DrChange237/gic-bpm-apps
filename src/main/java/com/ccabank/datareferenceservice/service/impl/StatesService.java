package com.ccabank.datareferenceservice.service.impl;

import com.ccabank.datareferenceservice.constant.AppError;
import com.ccabank.datareferenceservice.domain.AppServiceResult;
import com.ccabank.datareferenceservice.dto.country.StatesDto;
import com.ccabank.datareferenceservice.entity.States;
import com.ccabank.datareferenceservice.mappers.StatesMapper;
import com.ccabank.datareferenceservice.repository.StatesRepository;
import com.ccabank.datareferenceservice.service.faces.IStatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ccabank.datareferenceservice.constant.BeanIdConstant.STATE_DETAIL_SERVICE;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : datareferenceservice
 * @Package : com.ccabank.datareferenceservice.service.impl
 * <p>
 * @date: 08/08/2023
 * @time: 16:57
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
@Qualifier(STATE_DETAIL_SERVICE)
public class StatesService implements IStatesService {
    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
    @Autowired
    private StatesRepository statesRepository;
    @Autowired
    private StatesMapper statesMapper;

    @Override
    public AppServiceResult<List<StatesDto>> getAllStates() {

        try {
            logger.info(STATE_DETAIL_SERVICE, "getAllStates : methode invocation");
            List<States> states = statesRepository.findAll();

            return getConvertedResult(states, "getAllStates ");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(STATE_DETAIL_SERVICE, "getAllStates : Exception ", e.getMessage());
            return new AppServiceResult<List<StatesDto>>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<StatesDto> getStatesById(String id) {
        try {
            logger.info(STATE_DETAIL_SERVICE, "getStatesById : methode invocation", id);
            States state = statesRepository.findById(Long.valueOf(id)).orElse(null);
            if (state == null) {
                logger.warn(STATE_DETAIL_SERVICE, "getStatesById",
                        "State not exist!, Cannot further process!");
                return new AppServiceResult<StatesDto>(false, AppError.Validattion.errorCode(),
                        "State not exist!", null);
            }
            return new AppServiceResult<StatesDto>(true, 0, "Succeed!", statesMapper.toDto(state));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(STATE_DETAIL_SERVICE, "getStatesById : Exception ", e.getMessage());
            return new AppServiceResult<StatesDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<StatesDto> getStatesByName(String stateName) {
        try {
            logger.info(STATE_DETAIL_SERVICE, "getStatesByName : methode invocation", stateName);
            States state = statesRepository.getStateByName(stateName);
            if (state == null) {
                logger.warn(STATE_DETAIL_SERVICE, "getStatesByName",
                        "State not exist!, Cannot further process!");
                return new AppServiceResult<StatesDto>(false, AppError.Validattion.errorCode(),
                        "State not exist!", null);
            }
            return new AppServiceResult<StatesDto>(true, 0, "Succeed!", statesMapper.toDto(state));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(STATE_DETAIL_SERVICE, "getStatesByName : Exception ", e.getMessage());
            return new AppServiceResult<StatesDto>(false, AppError.Unknown.errorCode(),
                    AppError.Unknown.errorMessage(), null);
        }
    }

    @Override
    public AppServiceResult<StatesDto> getStateByCity(String countryId) {
        return null;
    }

    private AppServiceResult<List<StatesDto>> getConvertedResult(List<States> states, String functionName) {
        if (states == null) {
            logger.warn(STATE_DETAIL_SERVICE, functionName,
                    "State not exist!, Cannot further process!");
            return new AppServiceResult<List<StatesDto>>(false, AppError.Validattion.errorCode(),
                    "State not exist!", null);
        }
        List<StatesDto> result = new ArrayList<StatesDto>();
        if (states.size() > 0) {
            for (States state : states) {
                result.add(statesMapper.toDto(state));
            }
        }
        return new AppServiceResult<List<StatesDto>>(true, 0, "Succeed!", result);
    }
}
