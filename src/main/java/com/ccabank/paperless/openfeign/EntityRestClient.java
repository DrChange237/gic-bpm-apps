package com.ccabank.paperless.openfeign;

import com.ccabank.paperless.config.FeignClientConfiguration;
import com.ccabank.paperless.constant.FeignHeader;
import com.ccabank.paperless.dto.entity.AgencyInfo;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ENTITY-SERVICE" , configuration = FeignClientConfiguration.class)
@Headers(FeignHeader.TARGET_SERVICE_NAME + ": ENTITY-SERVICE")
public interface EntityRestClient {

    @GetMapping(path = "api/entities/agencies/getAllAgencies")
    List<AgencyInfo> getAllAgencies();

}
