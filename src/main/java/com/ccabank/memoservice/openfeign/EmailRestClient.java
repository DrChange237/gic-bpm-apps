package com.ccabank.memoservice.openfeign;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "EMAIL-SERVICE")
public interface EmailRestClient {

}
