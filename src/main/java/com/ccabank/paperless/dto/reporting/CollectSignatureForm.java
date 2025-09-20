package com.ccabank.paperless.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class CollectSignatureForm {

    private String reference;

    private List<Signatory> signatories;


    @Data
    @Schema(name = "Signature")
    public static class Signatory {
        @Schema(example = "SIMO PATRICK")
        private String name;

        @Schema(example = "Responsable de Département")
        private String function;

        @Schema(example = " ", description = "Base64-encoded image")
        private String signature;

        @Schema(example = "Commentaires")
        private String comments;

        @NotNull
        private LocalDate date;

    }
}
