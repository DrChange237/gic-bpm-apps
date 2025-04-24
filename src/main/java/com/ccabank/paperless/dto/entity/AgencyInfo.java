package com.ccabank.paperless.dto.entity;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


@Data
public class AgencyInfo {

    private String id;

    @NotNull(message = "code cannot be null")
    @Size(min = 1, max = 25)
    private String code;

    @NotEmpty(message = "name cannot be empty")
    @NotNull(message = "name cannot be null")
    @Size(max = 45)
    private String name;

    @Size(max = 255)
    private String mobile;

    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 255)
    private String address;

    @Size(max = 65535)
    private String description;

    @Size(max = 255)
    private String supervisor;

    private Boolean enabled;

    @NotNull(message = "Organization cannot be null")
    @NotNull(message = "City cannot be null")
    private Date creationDate;

    private Date lastModifiedDate;

}
