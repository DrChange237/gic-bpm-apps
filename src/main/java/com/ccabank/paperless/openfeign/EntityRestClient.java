package com.ccabank.paperless.openfeign;

import com.ccabank.paperless.dto.entity.AgencyInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ENTITY-SERVICE" , configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface EntityRestClient {

    @GetMapping(path = "api/entities/agencies/getAllAgencies")
    List<AgencyInfo> getAllAgencies();

}
