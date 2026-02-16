package com.change.gic.modules.core.dto.camunda.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les métadonnées du formulaire
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormMetadataDto {

    private String processDefinitionId;
    private String processDefinitionKey;
    private Integer processDefinitionVersion;
    private String formKey;
    private String formResourceName;
    private String deploymentId;
}
