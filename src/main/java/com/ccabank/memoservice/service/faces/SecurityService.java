package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.user.EmployeeInfo;

import javax.servlet.http.HttpServletRequest;

public interface SecurityService {
    boolean checkUserSignature(String username);

    EmployeeInfo getCurrentUser(HttpServletRequest request);
}
