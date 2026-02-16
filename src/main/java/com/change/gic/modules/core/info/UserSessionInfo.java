package com.change.gic.modules.core.info;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSessionInfo {

    private String id;

    private UserInfo user;

    private LocalDateTime date;

    private String token;
}
