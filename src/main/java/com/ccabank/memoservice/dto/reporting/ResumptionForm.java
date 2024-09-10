package com.ccabank.memoservice.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Schema(description = "Fiche de reprise de service")
public class ResumptionForm {
    @NotBlank
    @Schema(example = "Douala")
    private String place = "Douala";
    @NotNull
    private LocalDate date = LocalDate.now();

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

    @Schema(example = " ", description = "Base64-encoded image")
    private String signature = "";

    @NotNull
    private LocalDate startDate = LocalDate.now();
    @NotNull
    private LocalDate endDate = LocalDate.now();
    @NotNull
    private LocalDate realEndDate = LocalDate.now();
    @NotNull
    private Reason reason = Reason.ABSENCE;
    @NotNull
    private Signatory supervisor = new Signatory();

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getRealEndDate() {
        return realEndDate;
    }

    public void setRealEndDate(LocalDate realEndDate) {
        this.realEndDate = realEndDate;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public Signatory getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Signatory supervisor) {
        this.supervisor = supervisor;
    }

    public enum Reason {
        ANNUAL,
        MATERNITY,
        MEDICAL,
        ABSENCE
    }

    @Data
    public static class Signatory {
        @NotBlank
        @Schema(example = "SIMO PATRICK")
        private String name = "SIMO PATRICK";
        @Schema(example = " ", description = "Base64-encoded image")
        private String signature = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
}
