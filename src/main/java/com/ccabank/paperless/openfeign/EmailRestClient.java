package com.ccabank.paperless.openfeign;


import com.ccabank.paperless.dto.email.EmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "EMAIL-SERVICE")
public interface EmailRestClient {

    @PostMapping(path = "/api/emails/sendGenericEmail")
    boolean send(@RequestBody EmailDto email);

}
