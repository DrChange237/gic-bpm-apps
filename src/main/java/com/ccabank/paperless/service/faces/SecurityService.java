package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.dto.user.EmployeeInfo;

import javax.servlet.http.HttpServletRequest;

public interface SecurityService {
    boolean checkUserSignature(String username);

    EmployeeInfo getCurrentUser(HttpServletRequest request);
}
