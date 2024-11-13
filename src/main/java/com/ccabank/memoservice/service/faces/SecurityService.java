package com.ccabank.memoservice.service.faces;

import com.ccabank.memoservice.dto.user.EmployeeInfo;

import javax.servlet.http.HttpServletRequest;

public interface SecurityService {
    EmployeeInfo getCurrentUser(HttpServletRequest request);
}
