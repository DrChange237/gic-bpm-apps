package com.ccabank.memoservice.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class InterimForm {
    @NotNull
    @Schema(description = "Date d'émission de la lettre", example = "2024-10-21")
    private LocalDate date = LocalDate.now();
    @NotNull
    @Schema(description = "Employé allant en congés")
    private Employee employee = new Employee();
    @NotNull
    @Schema(description = "Employé assurant l'intérim")
    private Employee interim = new Employee();
    @NotNull
    @Schema(description = "Date de début de l'intérim", example = "2024-10-21")
    private LocalDate startDate = LocalDate.now();
    @NotNull
    @Schema(description = "Date de fin de l'intérim", example = "2024-11-15")
    private LocalDate endDate = LocalDate.now();
    @NotBlank
    @Schema(description = "Numéro de la note", example = "00448023", requiredMode = Schema.RequiredMode.AUTO)
    private String number = "00448023";
    @NotBlank
    @Schema(description = "Référence de la note", example = "NOTE 2024 N° 2970/DGA/DAF/RCH/DAAS/CORH", requiredMode = Schema.RequiredMode.AUTO)
    private String noteId = "NOTE 2024 N° 2970/DGA/DAF/RCH/DAAS/CORH";
    @NotNull
    private Subject subject = Subject.INTERIM;

    @Schema(example = "true", description = "Permet de savoir si l'intérimaire occupe le poste cumulativement à ses fonctions")
    private Boolean cumulate = true;

    @Schema(description = "Directeur Général Adjoint")
    private Signatory signatory;


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


    @Getter
    public enum Subject {
        NONE,
        CONTINUITY,
        INTERIM;
    }

    @Data
    public static class Employee {
        @Schema(description = "Nom complet", example = "LONLA Gatien Jordan")
        private String name = "Gatien Jordan";
        @Schema(description = "Matricule de l'employé", example = "01593")
        private String matricule =  "01593";
        @Schema(description = "Fonction de l'employé", example = "Développeur Back End")
        private String function = " ";
        private Sex sex = Sex.MALE;

        @Getter
        public enum Sex {
            MALE,
            FEMALE;
        }
    }
}
