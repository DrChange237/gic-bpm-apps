package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.dto.user.EmployeeInfo;
import com.ccabank.memoservice.openfeign.UserRestClient;
import com.ccabank.memoservice.service.faces.SecurityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;


@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    UserRestClient userRestClient;

    @Override
    public boolean checkUserSignature(String username){
        String signature = null;
        try{
            signature = userRestClient.getEmployeeSignature(username);
        }catch (Exception e){
            signature = null;
        }
        if(signature == null){
            return false;
        }
        if(signature.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public EmployeeInfo getCurrentUser(HttpServletRequest request){
        // get current user details
        if (
                !request.getServletPath().contains("import")   // if not importing users file
                        && request.getHeader("Authorization") != null
        ) {
            String token = request.getHeader("Authorization");
            token = token.substring(7, token.length());

            EmployeeInfo employeeInfo = null;
            try {
                employeeInfo = getCurrentUserInfo(token);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return employeeInfo;
        }

        return null;
       /* EmployeeInfo employeeInfo = getCurrentUserInfo();

        return employeeInfo;*/

    }

    private EmployeeInfo
    getCurrentUserInfo(String token) throws JsonProcessingException {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(payload);
        String username = actualObj.get("user_name").textValue();
        //String username = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map(Principal::getName).orElse("");
        return userRestClient.getStaffByUsername(username);
    }



}
