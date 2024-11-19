/**
 *
 */
package com.ccabank.memoservice.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    /*
     * (non-Javadoc)
     * @see org.springframework.security.oauth2.config.annotation.web.configuration.
     * ResourceServerConfigurerAdapter#configure(org.springframework.security.config.annotation.web.
     * builders.HttpSecurity)
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/oauth/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/**", "/v2/**", "/v3/**","/swagger-ui/**")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated();
    }
}

@Order(1)
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ActuatorWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
     * #configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/actuator/**", "/swagger*", "/swagger-resources/**", "/webjars/**", "/v2/**", "/v3/**","/swagger-ui/**", "/users/exposed/**", "/camunda/**").and().authorizeRequests().requestMatchers(EndpointRequest.to("info", "health")).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN").antMatchers("/swagger*", "/swagger-resources/**").permitAll().and().httpBasic();
    }

}
