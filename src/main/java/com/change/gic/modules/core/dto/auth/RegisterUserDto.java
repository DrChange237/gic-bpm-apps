package com.change.gic.modules.core.dto.auth;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String agencyId;
    private String username;
    private String password;
    private String email;
    private String role;
}
