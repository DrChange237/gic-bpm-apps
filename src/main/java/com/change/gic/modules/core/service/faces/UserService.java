package com.change.gic.modules.core.service.faces;

import com.change.gic.modules.core.dto.auth.RegisterUserDto;
import com.change.gic.modules.core.info.UserInfo;

public interface UserService {
    UserInfo createUser(RegisterUserDto request);
}
