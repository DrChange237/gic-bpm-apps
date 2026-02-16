package com.change.gic.modules.core.dto.camunda.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
class ExporterDto {

    @JsonProperty("name")
    private String name; // "Camunda Modeler"

    @JsonProperty("version")
    private String version; // "5.x.x"
}
