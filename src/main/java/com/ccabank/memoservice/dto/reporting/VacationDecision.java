package com.ccabank.memoservice.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Schema(description = "Décision de congé annuel")
public class VacationDecision {
    @NotNull
    @Schema(description = "Date de la précise de décision", example = "2024-11-18")
    private LocalDate date = LocalDate.now();
    @Schema(description = "Référence de la note", example = "08/11/2024/DG/DGA/DAF/RCH/DAAS/CORH")
    private String reference = "08/11/2024/DG/DGA/DAF/RCH/DAAS/CORH";
    @Schema(description = "Période de service liée à la mise en congé", example = "novembre 2023 à novembre 2024")
    private String period = "novembre 2023 à novembre 2024";
    @Schema(example = "01410")
    private String matricule = "01410";
    @Schema(description = "Nom complet de l'employé avec préfixé par son titre (M. ou Mme.)", example = "Mme ACHALLE Epse MINJO MAGDALINE TABE")
    private String employee = "Mme ACHALLE Epse MINJO MAGDALINE TABE";
    @Schema(description = "Unité de service préfixée par l'article et/ou la préposition appropriée. Ex : à l'Agence, à la Direction, au Département", example = "à l'Agence de Kumba")
    private String unity = "Agence de Mbouda";
    @Schema(example = "Chef de guichet")
    private String function = "Chef de guichet";
    @NotNull
    @Schema(description = "Date de départ en congés", example = "2024-11-15")
    private LocalDate startDate = LocalDate.now();
    @NotNull
    @Schema(description = "Date de reprise", example = "2024-12-20")
    private LocalDate endDate = LocalDate.now();

    @Schema(description = "Responsable du capital humain")
    private Signatory signatory;

    @Schema(description = "Congé principal", example = "18", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer principalVacation = 18;
    @Schema(description = "Allocation de congé due", example = "18", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer allocation = 18;
    @Schema(description = "Majoration pour ancienneté", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer seniority = 0;
    @Schema(description = "Majoration pour charge familiale", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer familyCharges = 0;
    @Schema(description = "Congé antérieur", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer previousVacation = 0;
    @Schema(description = "Permissions à déduire du congé", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer permissions = 0;
    @Schema(description = "Nombre de jours total accordés", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer providedVacation = 18;

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


}
