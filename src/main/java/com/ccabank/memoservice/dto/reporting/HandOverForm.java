package com.ccabank.memoservice.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class HandOverForm {

    @NotNull
    @Schema(description = "Titulaire du poste")
    private Employee employee = new Employee();

    @NotNull
    @Schema(description = "Intérimaire")
    private Employee interim = new Employee();

    @NotNull
    @Schema(description = "N+1 du titulaire du poste")
    private Employee supervisor = new Employee();

    @NotNull
    @Schema(description = "Date de début de l'intérim")
    private LocalDate startDate = LocalDate.now();

    @Schema(description = "Date de fin de l'intérim")
    private LocalDate endDate = LocalDate.now();

    @NotBlank
    @Schema(description = "Tableau (format HTML) des dossiers critiques")
    private String activities = "test";

    @NotBlank
    @Schema(description = "Tableau (format HTML) des responsabilités à prendre en main")
    private String responsibilities = "test";


    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getInterim() {
        return interim;
    }

    public void setInterim(Employee interim) {
        this.interim = interim;
    }

    public Employee getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
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

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    @Data
    public static class Employee {
        @NotBlank
        @Schema(example = "SIMO PATRICK")
        private String name = "KEVIN SIMO";

        @NotBlank
        @Schema(example = "Chef de département")
        private String function = "Développeur Backend";

        @Schema(example = " ", description = "Base64-encoded image")
        private String signature = "";

        @NotNull
        @Schema(description = "Date de signature")
        private LocalDate date = LocalDate.now();


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

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }
    }
}
