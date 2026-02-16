package com.change.gic.modules.core.dto.camunda.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO pour un composant du formulaire
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormComponentDto {

    @JsonProperty("label")
    private String label;

    @JsonProperty("text")
    private String text;

    @JsonProperty("type")
    private String type; // textfield, number, checkbox, select, etc.

    @JsonProperty("id")
    private String id;

    @JsonProperty("key")
    private String key;

    @JsonProperty("description")
    private String description;

    @JsonProperty("defaultValue")
    private Object defaultValue;

    @JsonProperty("validate")
    private ValidationDto validate;

    @JsonProperty("properties")
    private Map<String, Object> properties;

    @JsonProperty("values")
    private List<SelectOptionDto> values; // Pour les select/radio

    @JsonProperty("conditional")
    private ConditionalDto conditional;

    @JsonProperty("disabled")
    private Boolean disabled;

    @JsonProperty("readonly")
    private Boolean readonly;

    @JsonProperty("layout")
    private FormLayoutDto layout;
}
