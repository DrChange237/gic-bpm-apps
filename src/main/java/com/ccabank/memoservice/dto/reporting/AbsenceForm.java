package com.ccabank.memoservice.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Schema(description = "Demande d'autorisation d'absence")
public class AbsenceForm {
    @NotBlank
    @Schema(example = "Douala")
    private String place;
    @NotNull
    private LocalDate date;
    @Schema(example = " ")
    private String signature;

    @NotBlank
    @Schema(example = "01593")
    private String matricule;
    @NotBlank
    @Schema(example = "LONLA GATIEN JORDAN")
    private String name;
    @NotBlank
    @Schema(example = "Départment Etudes et Développement des Solutions Digitales")
    private String unity;
    @NotBlank
    @Schema(example = "Développeur Back-End")
    private String function;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    @Min(value = 1)
    @Schema(example = "3")
    private Integer days;
    @NotBlank
    @Schema(example = "Convalescence")
    private String reason;
    @NotNull
    private Deduction deduction;
    @NotBlank
    @Schema(example = "SIMO TRÉSOR WILFRIED")
    private String interim;
    @NotNull
    private Signatory signatory1;
    @NotNull
    private Signatory signatory2;
    @NotNull
    private Signatory headOffice;

    @NotNull
    private Settlement absence;
    @NotNull
    private Settlement stock;
    @NotNull
    private Settlement advice;
    @NotNull
    private Settlement rights;
    @NotNull
    private Settlement salary;
    @NotNull
    private Settlement vacation;
    private List<String> signatures;


    @Data
    public static class Settlement {
        private Double paid;
        private Double unpaid;
    }

    @Data
    @Schema(name = "Avis")
    public static class Signatory {
        @NotBlank
        @Schema(example = "SIMO PATRICK")
        private String name;
        @Schema(example = " ", description = "Base64-encoded image")
        private String signature;
        @NotNull
        private LocalDate date;
    }

    public enum Deduction {
        SALARY,
        VACATION
    }

}
