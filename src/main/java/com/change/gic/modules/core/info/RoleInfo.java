package com.change.gic.modules.core.info;

import lombok.Data;

import java.util.Set;

@Data
public class RoleInfo {

    private String id;

    private String name;

    private String description;

    private Set<UserAuthorityInfo> authorities;
}
