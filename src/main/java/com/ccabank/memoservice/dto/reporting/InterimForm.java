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
    private LocalDate date;
    @NotNull
    @Schema(description = "Employé allant en congés")
    private Employee employee;
    @NotNull
    @Schema(description = "Employé assurant l'intérim")
    private Employee interim;
    @NotNull
    @Schema(description = "Date de début de l'intérim", example = "2024-10-21")
    private LocalDate startDate;
    @NotNull
    @Schema(description = "Date de fin de l'intérim", example = "2024-11-15")
    private LocalDate endDate;
    @NotBlank
    @Schema(description = "Numéro de la note", example = "00448023", requiredMode = Schema.RequiredMode.AUTO)
    private String number;
    @NotBlank
    @Schema(description = "Référence de la note", example = "NOTE 2024 N° 2970/DGA/DAF/RCH/DAAS/CORH", requiredMode = Schema.RequiredMode.AUTO)
    private String noteId;
    @NotNull
    private Subject subject;
    @Schema(example = " ", description = "Base64-encoded image contenant le cachet rond, le cachet nominatif et la signature du DGA")
    private String cachet;


    @Getter
    public enum Subject {
        CONTINUITY,
        INTERIM;
    }

    @Data
    public static class Employee {
        @Schema(description = "Nom complet", example = "LONLA Gatien Jordan")
        private String name;
        @Schema(description = "Matricule de l'employé", example = "01593")
        private String matricule;
        @Schema(description = "Fonction de l'employé", example = "Développeur Back End")
        private String function;
        private Sex sex;

        @Getter
        public enum Sex {
            MALE,
            FEMALE;
        }
    }
}
