package com.ccabank.memoservice.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Schema(description = "Demande d'autorisation d'absence")
public class AbsenceForm {
    @NotBlank
    @Schema(example = "Douala")
    private String place = "Douala";

    @NotNull
    private LocalDate date = LocalDate.now();

    @Schema(example = " ")
    private String signature = " ";

    @NotBlank
    @Schema(example = "01593")
    private String matricule = "01593";

    @NotBlank
    @Schema(example = "LONLA GATIEN JORDAN")
    private String name = "LONLA GATIEN JORDAN";

    @NotBlank
    @Schema(example = "Départment Etudes et Développement des Solutions Digitales")
    private String unity = "Départment Etudes et Développement des Solutions Digitales";

    @NotBlank
    @Schema(example = "Développeur Back-End")
    private String function = "Développeur Back-End";

    @NotNull
    private LocalDate startDate = LocalDate.now();

    @NotNull
    private LocalDate endDate = LocalDate.now();

    @NotNull
    @Min(value = 1)
    @Schema(example = "3")
    private Integer days = 1;

    @NotBlank
    @Schema(example = "Convalescence")
    private String reason = "Convalescence";

    @NotNull
    private Deduction deduction = Deduction.VACATION;

    @NotBlank
    @Schema(example = "SIMO TRÉSOR WILFRIED")
    private String interim = "";

    @NotNull
    private Signatory signatory1  = new Signatory();
    @NotNull
    private Signatory signatory2  = new Signatory();
    @NotNull
    private Signatory headOffice = new Signatory();

    @NotNull
    private Double absence= 0.0;
    @NotNull
    private Double stock = 0.0;
    @NotNull
    private Double advice = 0.0;
    @NotNull
    private Double rights = 0.0;

    private List<Signatory> signatories;

    private List<String> signatures = new ArrayList<>();

    @Data
    @Schema(name = "Avis")
    public static class Signatory {
        @NotBlank
        @Schema(example = "SIMO PATRICK")
        private String name = "SIMO PATRICK";
        @Schema(example = " ", description = "Base64-encoded image")
        private String signature = "";
        @NotNull
        private LocalDate date = LocalDate.now();
    }

    public enum Deduction {
        SALARY,
        VACATION
    }



}
