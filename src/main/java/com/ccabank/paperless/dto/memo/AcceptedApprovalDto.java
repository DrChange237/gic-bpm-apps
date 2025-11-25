package com.ccabank.paperless.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AcceptedApprovalDto {
    @Schema(example = "1")
    private String idApproval;

    @Schema(example = "2")
    private int positionRejected = 1;

    @Schema(example = "Liste des champs")
    private List<FieldDto> fields;

    @Schema(example = "Favorable")
    private String comments;

    @Schema(example = "true")
    private boolean decision;
}
