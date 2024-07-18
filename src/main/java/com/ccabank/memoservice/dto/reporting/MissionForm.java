package com.ccabank.memoservice.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Schema(description = "Ordre de mission")
public class MissionForm {
    @NotBlank
    @Schema(example = "Douala")
    private String place;
    @NotNull
    private LocalDate date;

    @NotBlank
    @Schema(example = "LONLA GATIEN JORDAN")
    private String name;
    @NotBlank
    @Schema(example = "Développeur Back-End")
    private String function;
    @NotBlank
    @Schema(example = "Départment Etudes et Développement des Solutions Digitales")
    private String unity;
    @NotBlank
    @Schema(example = "Déploiement de la solution MOTOMAN", description = "Objet de la mission")
    private String object;
    @NotBlank
    @Schema(description = "Lieu de la mission", example = "YAOUNDE")
    private String location;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    @Schema(description = "Nombre de nuitées", example = "3")
    private Integer nights;
    @NotNull
    private Transport transport;
    @NotBlank
    @Schema(description = "Numéro de compte courant", example = "00671459701 - 67")
    private String accountNumber;
    @NotNull
    private Signatory supervisor;
    @NotNull
    private Signatory supervisorNext;
    @NotNull
    private Signatory uch;
    @Schema(description = "Décision de la Direction Générale", example = "Favorable")
    private String decision;
    @Schema(example = " ")
    private String signature;
    @Schema(description = "Signature du donneur d'ordre", example = " ")
    private String requesterSignature;

    private Double chargeSupport;
    private Double missionFees;
    private Double transportFees;
    @Schema(example = "123456789")
    private String authorisationNumber;
    @Schema(example = "987654321")
    private String receiptNumber;

    @Data
    @Schema(name = "Signer")
    public static class Signatory {
        @NotBlank
        @Schema(example = "SIMO PATRICK")
        private String name;
        @Schema(example = "Chef de département")
        private String function;
        @NotNull
        private LocalDate date;
        @Schema(example = " ", description = "Base64-encoded image")
        private String signature;
    }

    @Data
    public static class Transport {
        @Schema(example = "false")
        public Boolean common = false;
        @Schema(example = "CE277DG")
        public String immatriculation;
        @Schema(example = "CLÉMENT")
        public String courier;
    }
}
