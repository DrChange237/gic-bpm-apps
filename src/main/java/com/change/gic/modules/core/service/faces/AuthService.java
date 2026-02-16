package com.change.gic.modules.core.service.faces;

import com.change.gic.modules.core.dto.auth.AuthDto;
import com.change.gic.modules.core.dto.auth.ChangePasswordDto;
import com.change.gic.modules.core.info.UserSessionInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    void changePassword(String username, ChangePasswordDto req);

    UserSessionInfo login(AuthDto authDto);

    String getCurrentUsername();

    void performFullLogout(HttpServletRequest request,
                           HttpServletResponse response);
}
