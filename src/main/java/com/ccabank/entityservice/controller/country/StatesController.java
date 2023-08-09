package com.ccabank.entityservice.controller.country;

import com.ccabank.entityservice.domain.AppServiceResult;
import com.ccabank.entityservice.dto.HttpResponse;
import com.ccabank.entityservice.dto.HttpResponseError;
import com.ccabank.entityservice.dto.HttpResponseSuccess;
import com.ccabank.entityservice.dto.country.StatesDto;
import com.ccabank.entityservice.service.impl.StatesService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : entityservice
 * @Package : com.ccabank.entityservice.controller.country
 * <p>
 * @date: 08/08/2023
 * @time: 16:53
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Api(tags = "States")
@RestController
@RequestMapping("/states")
public class StatesController {

    @Autowired
    private StatesService stateService;

    @GetMapping("/allStates")
    public ResponseEntity<HttpResponse> allStates() {
        AppServiceResult<List<StatesDto>> result = stateService.getAllStates();
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<StatesDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }
    @GetMapping("/statesDetails")
    public ResponseEntity<HttpResponse> statesDetails(@Valid @RequestParam(value = "id") String id) {
        AppServiceResult<StatesDto> result = stateService.getStatesById(id);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<StatesDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/getStatesByName")
    public ResponseEntity<HttpResponse> getStatesByName(@Valid @RequestParam(value = "name") String stateName) {
        AppServiceResult<StatesDto> result = stateService.getStatesByName(stateName);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<StatesDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }


}
