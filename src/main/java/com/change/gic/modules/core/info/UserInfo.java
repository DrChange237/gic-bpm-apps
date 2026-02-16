package com.change.gic.modules.core.info;

import com.change.gic.modules.business.info.AgencyInfo;
import lombok.Data;


@Data
public class UserInfo {

    private String id;

    private String username;

    private String email;

    private AgencyInfo agency;

    private RoleInfo role;
}
