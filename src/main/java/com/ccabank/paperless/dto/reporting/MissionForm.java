package com.ccabank.paperless.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Schema(description = "Ordre de mission")
public class MissionForm {
    @NotBlank
    @Schema(example = "Douala")
    private String place = "Douala";
    @NotNull
    private LocalDate date = LocalDate.now();

    @NotBlank
    @Schema(example = "LONLA GATIEN JORDAN")
    private String name = "LONLA GATIEN JORDAN" ;
    @NotBlank
    @Schema(example = "Développeur Back-End")
    private String function = "Développeur Back-End";
    @NotBlank
    @Schema(example = "Départment Etudes et Développement des Solutions Digitales")
    private String unity = "DSD";
    @NotBlank
    @Schema(example = "Déploiement de la solution MOTOMAN", description = "Objet de la mission")
    private String object = "Déploiement de la solution MOTOMAN";
    @NotBlank
    @Schema(description = "Lieu de la mission", example = "YAOUNDE")
    private String location = "YAOUNDE";
    @NotNull
    private LocalDate startDate = LocalDate.now();
    @NotNull
    private LocalDate endDate = LocalDate.now();
    @NotNull
    @Schema(description = "Nombre de nuitées", example = "3")
    private Integer nights = 3;
    @NotNull
    private Transport transport = new Transport();
    @NotBlank
    @Schema(description = "Numéro de compte courant", example = "00671459701 - 67")
    private String accountNumber = "00671459701 - 67";
    @NotNull
    private Signatory supervisor = new Signatory();
    @NotNull
    private Signatory supervisorNext = new Signatory();
    @NotNull
    private Signatory uch = new Signatory();
    @Schema(description = "Décision de la Direction Générale", example = "Favorable")
    private String decision = "Favorable";

    @Schema(example = " ")
    private String signature = "";

    @NotNull
    @Schema(description = "Donneur d'ordre")
    private Signatory requester;

    private String chargeSupport = "Douala";
    private Double missionFees = 0.0;
    private Double transportFees = 0.0;
    @Schema(example = "123456789")
    private String authorisationNumber = "123456789";
    @Schema(example = "987654321")
    private String receiptNumber =  "987654321";

    @Data
    @Schema(name = "Signer")
    public static class Signatory {
        @NotBlank
        @Schema(example = "SIMO PATRICK")
        private String name = "";

        @Schema(example = "Chef de département")
        private String function = "";
        @NotNull
        private LocalDate date;
        @Schema(example = " ", description = "Base64-encoded image")
        private String signature =  "";
    }

    @Data
    public static class Transport {
        @Schema(example = "false")
        public Boolean common = false;
        @Schema(example = "CE277DG")
        public String immatriculation = "CE277DG";
        @Schema(example = "CLÉMENT")
        public String courier = "CLÉMENT";
    }
}
