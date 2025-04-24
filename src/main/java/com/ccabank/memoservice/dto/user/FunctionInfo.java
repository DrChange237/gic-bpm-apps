package com.ccabank.memoservice.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class FunctionInfo {

    private String id;

    @Size(min = 2, max = 125, message = "name size minimum is 2 maximum is 125")
    @NotNull(message = "name cannot be null")
    private String name;

    private String description;
}
