package com.ccabank.paperless.dto.reporting;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
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

        @Schema(example = "kevin.simo")
        private String identifier;

        @Schema(example = "SIMO PATRICK")
        private String name;

        @Schema(example = "Responsable de Département")
        private String function;

        @Schema(example = " ", description = "Base64-encoded image")
        private String signature;

        @Schema(example = "Commentaires")
        private String comments;

        @NotNull
        private String date;

    }
}
