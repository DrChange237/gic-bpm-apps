package com.change.gic.modules.core.info;

import com.change.gic.modules.core.enumeration.Habilitation;
import lombok.Data;

@Data
public class UserAuthorityInfo {

    private String id;

    private String name;

    private String description;

    private Habilitation code;
}
