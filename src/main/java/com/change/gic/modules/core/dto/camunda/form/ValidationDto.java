package com.change.gic.modules.core.dto.camunda.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les règles de validation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
class ValidationDto {

    @JsonProperty("required")
    private Boolean required = false;

    @JsonProperty("minLength")
    private Integer minLength;

    @JsonProperty("maxLength")
    private Integer maxLength;

    @JsonProperty("min")
    private Number min;

    @JsonProperty("max")
    private Number max;

    @JsonProperty("pattern")
    private String pattern;

    @JsonProperty("validationType")
    private String validationType; // email, phone, etc.
}
