package com.change.gic.config;

import com.change.gic.constant.FeignHeader;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer";

    private final Environment environment;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        boolean hasToken = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
            if(details != null && StringUtils.isNotBlank(details.getTokenValue())) {
                requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, details.getTokenValue()));
                hasToken = true;
            }
        }

        requestTemplate.header(FeignHeader.SOURCE_SERVICE_NAME, environment.getProperty("spring.application.name"));
        if(!hasToken) {
            String target = requestTemplate.headers().getOrDefault(FeignHeader.TARGET_SERVICE_NAME, Collections.emptyList())
                    .stream()
                    .findFirst()
                    .orElse("")
                    .toLowerCase().replaceAll("-", ".");
            requestTemplate.header(FeignHeader.API_KEY, environment.getProperty(target + ".key"));
            requestTemplate.header(FeignHeader.API_SECRET, environment.getProperty(target + ".secret"));
        }
    }
}
