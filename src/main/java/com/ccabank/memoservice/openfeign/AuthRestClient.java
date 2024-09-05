package com.ccabank.memoservice.openfeign;


import com.ccabank.memoservice.dto.email.EmailDto;
import com.ccabank.memoservice.dto.user.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthRestClient {
    @PostMapping(path = "/api/oauth/profile/userInfo")
    UserInfo profile();

}
