package com.ccabank.feedbackservice.openfeign;


import com.ccabank.feedbackservice.dto.feedback.AgencyRestDto;
import com.ccabank.feedbackservice.dto.feedback.UserRestDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : user-service
 * @Package : com.ccabank.userservice.openfeign
 * <p>
 * @date: 12/08/2023
 * @time: 13:51
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
//@FeignClient(value = "entityRestClient", url = "https://developer.ccabank-app.com/sandbox", configuration = FeignClientProperties.FeignClientConfiguration.class)
@FeignClient(name = "ENTITY-SERVICE", configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface EntityRestClient {

    @GetMapping(path = "/api/entities/exposed/getAllAgencies")
    @Headers({
            "x-api-key: key",
            "secret : secret"
    })
    List<AgencyRestDto> getAgencies();

}
