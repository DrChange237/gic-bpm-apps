package com.change.gic.modules.core.dto.camunda.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO principal pour le formulaire Camunda
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CamundaFormDto {

    @JsonProperty("schemaVersion")
    private Integer schemaVersion;

    @JsonProperty("exporter")
    private ExporterDto exporter;

    @JsonProperty("type")
    private String type; // "default"

    @JsonProperty("id")
    private String id;

    @JsonProperty("executionPlatform")
    private String executionPlatform; // "Camunda Platform"

    @JsonProperty("executionPlatformVersion")
    private String executionPlatformVersion; // "7.19.0"

    @JsonProperty("components")
    private List<FormComponentDto> components;

    // Métadonnées supplémentaires
    private FormMetadataDto metadata;
}
