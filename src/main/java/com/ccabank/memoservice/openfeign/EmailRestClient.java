package com.ccabank.memoservice.openfeign;


import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.reporting.VacationForm;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "EMAIL-SERVICE")
public interface EmailRestClient {

    @PostMapping(path = "/api/emails/sendGenericEmail")
    boolean send(@RequestBody EmailDto email);

}
