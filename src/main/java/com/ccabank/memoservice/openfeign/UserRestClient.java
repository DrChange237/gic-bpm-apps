package com.ccabank.memoservice.openfeign;

import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.dto.user.UserRestDto;
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
@FeignClient(name = "USER-SERVICE", configuration = FeignClientProperties.FeignClientConfiguration.class)
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
    @Headers({
            "x-api-key: key",
            "secret : secret"
    })
    String getEmployeeSignature(@PathVariable("username") String username);


}
