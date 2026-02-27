package com.change.gic.modules.core.service.faces;

import com.change.gic.modules.core.dto.auth.RegisterUserDto;
import com.change.gic.modules.core.info.UserInfo;

import java.util.List;

public interface UserService {
    List<UserInfo> getUsers();

    void activateUser(String username);

    UserInfo createUser(RegisterUserDto request);
}
