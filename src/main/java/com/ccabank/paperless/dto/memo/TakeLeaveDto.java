package com.ccabank.paperless.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;

public class TakeLeaveDto {

    @Schema(example = "1")
    private String idApproval;

    @Schema(example = "true")
    private boolean decision;

    public String getIdApproval() {
        return idApproval;
    }

    public void setIdApproval(String idApproval) {
        this.idApproval = idApproval;
    }

    public boolean isDecision() {
        return decision;
    }

    public void setDecision(boolean decision) {
        this.decision = decision;
    }
}
