package com.ccabank.memoservice.dto.reporting;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Schema(description = "Demande de congé annuel")
public class VacationForm {
    @NotBlank
    @Schema(example = "Douala")
    private String place;
    @NotNull
    private LocalDate date;

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
    private LocalDate lastVacationDate;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;


    @Schema(example = " ", description = "Base64-encoded image")
    private String signature;

    @NotNull
    private Interim interim;

    @NotNull
    private Signatory supervisor;
    @NotNull
    private Signatory supervisorNext;

    @Data
    public static class Interim {
        @NotBlank
        @Schema(example = "SIMO TRÉSOR WILFRIED")
        private String name;
        @NotBlank
        @Schema(example = "Développeur Back-End")
        private String function;
        @NotBlank
        @Schema(example = "Départment Etudes et Développement des Solutions Digitales")
        private String unity;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFunction() {
            return function;
        }

        public void setFunction(String function) {
            this.function = function;
        }

        public String getUnity() {
            return unity;
        }

        public void setUnity(String unity) {
            this.unity = unity;
        }
    }

    @Data
    public static class Signatory {
        @NotBlank
        @Schema(example = "SIMO PATRICK")
        private String name;
        @Schema(example = " ", description = "Base64-encoded image")
        private String signature;

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

    public LocalDate getLastVacationDate() {
        return lastVacationDate;
    }

    public void setLastVacationDate(LocalDate lastVacationDate) {
        this.lastVacationDate = lastVacationDate;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Interim getInterim() {
        return interim;
    }

    public void setInterim(Interim interim) {
        this.interim = interim;
    }

    public Signatory getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Signatory supervisor) {
        this.supervisor = supervisor;
    }

    public Signatory getSupervisorNext() {
        return supervisorNext;
    }

    public void setSupervisorNext(Signatory supervisorNext) {
        this.supervisorNext = supervisorNext;
    }
}
