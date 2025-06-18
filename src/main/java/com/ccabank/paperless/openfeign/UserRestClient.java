package com.ccabank.paperless.openfeign;

import com.ccabank.paperless.config.FeignClientConfiguration;
import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.dto.user.UserRestDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "USER-SERVICE", configuration = FeignClientConfiguration.class)
//@FeignClient(value = "userRestClient", url = "https://developer.ccabank-app.com", configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface UserRestClient {


    @GetMapping(path = "/api/users/employees/findByUsername/{username}")
    EmployeeInfo getStaffByUsername(@PathVariable("username") String username);


    @GetMapping(path = "/api/users/exposed/findByUsername/{username}")
    @Headers({
            "x-api-key: key",
            "secret : secret"
    })
    UserRestDto getAgencyByStaffUsername(@PathVariable("username") String username,
                                         @RequestHeader("x-api-key") String token,
                                         @RequestHeader("secret") String customValue);

    @GetMapping(path = "/api/users/employees/getEmployeeSignatureB64/{username}")
    String getEmployeeSignature(@PathVariable("username") String username,
                                @RequestHeader("x-api-key") String key,
                                @RequestHeader("secret") String secret);


}
