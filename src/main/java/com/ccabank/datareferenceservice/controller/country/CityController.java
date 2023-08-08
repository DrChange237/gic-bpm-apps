package com.ccabank.datareferenceservice.controller.country;

import com.ccabank.datareferenceservice.domain.AppServiceResult;
import com.ccabank.datareferenceservice.dto.HttpResponse;
import com.ccabank.datareferenceservice.dto.HttpResponseError;
import com.ccabank.datareferenceservice.dto.HttpResponseSuccess;
import com.ccabank.datareferenceservice.dto.country.CityDto;
import com.ccabank.datareferenceservice.service.impl.CityService;
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
 * @project : datareferenceservice
 * @Package : com.ccabank.datareferenceservice.controller.country
 * <p>
 * @date: 08/08/2023
 * @time: 18:04
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Api(tags = "City")
@RestController
@RequestMapping("/city")
public class CityController {
    @Autowired
    private CityService cityService;

    @GetMapping("/allCity")
    public ResponseEntity<HttpResponse> allCity() {
        AppServiceResult<List<CityDto>> result = cityService.getAllCity();
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<CityDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/cityDetails")
    public ResponseEntity<HttpResponse> countryDetails(@Valid @RequestParam(value = "id") String id) {
        AppServiceResult<CityDto> result = cityService.getCityById(id);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<CityDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/getCityByName")
    public ResponseEntity<HttpResponse> getCityByName(@Valid @RequestParam(value = "name") String name) {
        AppServiceResult<CityDto> result = cityService.getCityByName(name);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<CityDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/getCityByState")
    public ResponseEntity<HttpResponse> getCityByState(@Valid @RequestParam(value = "id") String cityId) {
        AppServiceResult<List<CityDto>> result = cityService.getCityByState(cityId);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<CityDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }
}
