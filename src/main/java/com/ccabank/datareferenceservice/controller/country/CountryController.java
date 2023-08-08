package com.ccabank.datareferenceservice.controller.country;

import com.ccabank.datareferenceservice.domain.AppServiceResult;
import com.ccabank.datareferenceservice.dto.HttpResponse;
import com.ccabank.datareferenceservice.dto.HttpResponseError;
import com.ccabank.datareferenceservice.dto.HttpResponseSuccess;
import com.ccabank.datareferenceservice.dto.country.CountryDto;
import com.ccabank.datareferenceservice.service.impl.CountryService;
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
 * @time: 11:24
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@Api(tags = "Country")
@RestController
@RequestMapping("/country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping("/allCountry")
    public ResponseEntity<HttpResponse> getAllCountry() {
        AppServiceResult<List<CountryDto>> result = countryService.getAllCountry();
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<CountryDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/countryDetails")
    public ResponseEntity<HttpResponse> countryDetails(@Valid @RequestParam(value = "id") String id) {
        AppServiceResult<CountryDto> result = countryService.getCountryById(id);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<CountryDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/getCountryByName")
    public ResponseEntity<HttpResponse> getCountryByName(@Valid @RequestParam(value = "name") String countryName) {
        AppServiceResult<CountryDto> result = countryService.getCountryByName(countryName);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<CountryDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/getCountryByCode")
    public ResponseEntity<HttpResponse> getCountryByCode(@Valid @RequestParam(value = "code") String code) {
        AppServiceResult<CountryDto> result = countryService.getCountryByCode(code);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<CountryDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/getCountryByCodeIso3")
    public ResponseEntity<HttpResponse> getCountryByCodeIso3(@Valid @RequestParam(value = "code") String codeIso3) {
        AppServiceResult<CountryDto> result = countryService.getCountryByCodeIso3(codeIso3);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<CountryDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

    @GetMapping("/getCountryByPhoneCode")
    public ResponseEntity<HttpResponse> getCountryByPhoneCode(@Valid @RequestParam(value = "code") String phoneCode) {
        AppServiceResult<CountryDto> result = countryService.getCountryByPhoneCode(phoneCode);
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<CountryDto>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }


}
