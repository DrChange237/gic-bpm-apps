package com.ccabank.paperless.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TakeLeaveDto {

    @Schema(example = "1")
    private String idApproval;

    @Schema(example = "true")
    private boolean decision;
}
