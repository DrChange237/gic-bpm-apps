package com.change.gic.modules.core.dto.camunda.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FormLayoutDto {

    @JsonProperty("row")
    private String row;

    @JsonProperty("columns")
    private Integer columns;

}
