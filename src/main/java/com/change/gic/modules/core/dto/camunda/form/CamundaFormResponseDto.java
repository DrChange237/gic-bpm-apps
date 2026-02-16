package com.change.gic.modules.core.dto.camunda.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la réponse complète avec métadonnées
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CamundaFormResponseDto {

    @JsonProperty("form")
    private CamundaFormDto form;

    @JsonProperty("metadata")
    private FormMetadataDto metadata;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("message")
    private String message;
}
