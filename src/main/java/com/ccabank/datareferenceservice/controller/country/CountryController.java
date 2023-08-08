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
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<HttpResponse> findAgencyByStatus() {
        AppServiceResult<List<CountryDto>> result = countryService.getAllCountry();
        return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<CountryDto>>(result.getData()))
                : ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
    }

}
