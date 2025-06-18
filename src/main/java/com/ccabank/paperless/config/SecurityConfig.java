package com.ccabank.paperless.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;


@Configuration
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter {

    @Value("${endpoints.base.url}")
    private String baseUrl;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Set session management to stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Set permissions on endpoints
        http
            .authorizeRequests()
            .antMatchers("rest-docs/**", "/swagger-ui/**", "/swagger-resources/**", String.format("%s/validationForm/**", baseUrl)).permitAll()
            .anyRequest().authenticated();
    }
}
