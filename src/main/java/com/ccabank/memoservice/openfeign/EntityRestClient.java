package com.ccabank.memoservice.openfeign;

import com.ccabank.memoservice.dto.entity.AgencyInfo;
import com.ccabank.memoservice.dto.user.EmployeeInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ENTITY-SERVICE" , configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface EntityRestClient {

    @GetMapping(path = "api/entities/agencies/getAllAgencies")
    List<AgencyInfo> getAllAgencies();

}
