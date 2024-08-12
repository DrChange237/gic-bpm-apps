package com.ccabank.memoservice.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class WorkForm {
    @Schema(description = "Unité", example = "Départment Etudes et Développement des Solutions Digitales")
    private String unity = "Départment Etudes et Développement des Solutions Digitales";

    @Schema(description = "Utilisateur", example = "LONLA GATIEN JORDAN")
    private String user = "LONLA GATIEN JORDAN";

    @Schema(description = "Date de la demande")
    private LocalDate date = LocalDate.now();

    @Schema(description = "Désignation du prestataire", example = "BONNY INFORMATIQUE SARL")
    private String provider = "BONNY INFORMATIQUE SARL";

    @Schema(example = "Ordinateur Portable")
    private String designation = "Ordinateur Portable";

    @Schema(description = "Marque du matériel", example = "HP")
    private String brand = "HP";

    @Schema(description = "Code étiquette", example = "089454")
    private String labelCode = "089454";

    @Schema(description = "Travaux demandés", type = "array", example = "[\"Dératisation de l'espace de travail\", \"Réparation du climatiseur\"]")
    private List<@NotBlank String> tasks = new ArrayList<>();

    @Schema(description = "Signature de l'utilisateur")
    private Signatory initiator = new Signatory();

    @Schema(description = "Signature du comptable")
    private Signatory accountant = new Signatory();

    @Schema(description = "Signature du supérieur hiérarchique")
    private Signatory supervisor = new Signatory();

    @Schema(description = "Signature du responsable du département infrastructure")
    private Signatory department = new Signatory();

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(String labelCode) {
        this.labelCode = labelCode;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    public Signatory getInitiator() {
        return initiator;
    }

    public void setInitiator(Signatory initiator) {
        this.initiator = initiator;
    }

    public Signatory getAccountant() {
        return accountant;
    }

    public void setAccountant(Signatory accountant) {
        this.accountant = accountant;
    }

    public Signatory getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Signatory supervisor) {
        this.supervisor = supervisor;
    }

    public Signatory getDepartment() {
        return department;
    }

    public void setDepartment(Signatory department) {
        this.department = department;
    }

    @Data
    @Schema(name = "Validation")
    public static class Signatory {
        @NotBlank
        @Schema(example = "SIMO PATRICK")
        private String name = "SIMO PATRICK";

        @Schema(example = " ", description = "Base64-encoded image")
        private String signature = "";

        @NotNull
        private LocalDate date = LocalDate.now();

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

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }
    }
}
