package com.ccabank.memoservice.dto.memo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class AcceptedApprovalDto {

    @Schema(example = "1")
    private Long idApproval;

    @Schema(example = "Liste des champs")
    private List<FieldDto> fields;

    @Schema(example = "Favorable")
    private String comments;

    @Schema(example = "true")
    private boolean decision;

    public Long getIdApproval() {
        return idApproval;
    }

    public void setIdApproval(Long idApproval) {
        this.idApproval = idApproval;
    }

    public List<FieldDto> getFields() {
        return fields;
    }

    public void setFields(List<FieldDto> fields) {
        this.fields = fields;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isDecision() {
        return decision;
    }

    public void setDecision(boolean decision) {
        this.decision = decision;
    }
}
