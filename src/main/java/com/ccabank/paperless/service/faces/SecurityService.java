package com.ccabank.paperless.service.faces;

import com.ccabank.paperless.dto.user.EmployeeInfo;

public interface SecurityService {
    boolean checkUserSignature(String username);

    EmployeeInfo getCurrentUser();
}
