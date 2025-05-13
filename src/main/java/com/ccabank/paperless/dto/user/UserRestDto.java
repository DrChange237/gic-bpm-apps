package com.ccabank.paperless.dto.user;

import com.ccabank.paperless.util.StringUtil;
import lombok.Data;

@Data
public class UserRestDto {

    private String username;

    private String function;

    private String agencyCode;

    private String agencyName;

    private String matricule;

    private String department;

    public String getEmail(){
        return  this.getUsername() + "@cca-bank.com";
    }

    public String getName(){
        return  StringUtil.transformUsernameToName(username);
    }
}
