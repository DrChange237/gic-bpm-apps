package com.ccabank.paperless.service.impl;

import com.ccabank.paperless.dto.user.EmployeeInfo;
import com.ccabank.paperless.exception.BadRequestException;
import com.ccabank.paperless.openfeign.UserRestClient;
import com.ccabank.paperless.service.faces.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserRestClient userRestClient;

    @Override
    public boolean checkUserSignature(String username){
        String signature;
        try{
            signature = userRestClient.getEmployeeSignature(username);
        }catch (Exception e){
            throw new BadRequestException("l'utilisateur " + username + " n'a pas de signature !");
        }
        return StringUtils.isNotBlank(signature);
    }

    @Override
    public EmployeeInfo getCurrentUser(){
        String username = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map(Principal::getName).orElse("");
        log.warn("Current user: {}", username);
        return userRestClient.getStaffByUsername(username);
    }

}
