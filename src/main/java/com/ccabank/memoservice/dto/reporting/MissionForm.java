package com.ccabank.memoservice.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Schema(description = "Ordre de mission")
public class MissionForm {
    @NotBlank
    @Schema(example = "Douala")
    private String place;
    @NotNull
    private LocalDate date;

    @NotBlank
    @Schema(example = "LONLA GATIEN JORDAN")
    private String name;
    @NotBlank
    @Schema(example = "Développeur Back-End")
    private String function;
    @NotBlank
    @Schema(example = "Départment Etudes et Développement des Solutions Digitales")
    private String unity;
    @NotBlank
    @Schema(example = "Déploiement de la solution MOTOMAN", description = "Objet de la mission")
    private String object;
    @NotBlank
    @Schema(description = "Lieu de la mission", example = "YAOUNDE")
    private String location;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    @Schema(description = "Nombre de nuitées", example = "3")
    private Integer nights;
    @NotNull
    private Transport transport;
    @NotBlank
    @Schema(description = "Numéro de compte courant", example = "00671459701 - 67")
    private String accountNumber;
    @NotNull
    private Signatory supervisor;
    @NotNull
    private Signatory supervisorNext;
    @NotNull
    private Signatory uch;
    @Schema(description = "Décision de la Direction Générale", example = "Favorable")
    private String decision;
    @Schema(example = " ")
    private String signature;
    @Schema(description = "Signature du donneur d'ordre", example = " ")
    private String requesterSignature;

    private Double chargeSupport;
    private Double missionFees;
    private Double transportFees;
    @Schema(example = "123456789")
    private String authorisationNumber;
    @Schema(example = "987654321")
    private String receiptNumber;

    @Data
    @Schema(name = "Signer")
    public static class Signatory {
        @NotBlank
        @Schema(example = "SIMO PATRICK")
        private String name;
        @Schema(example = "Chef de département")
        private String function;
        @NotNull
        private LocalDate date;
        @Schema(example = " ", description = "Base64-encoded image")
        private String signature;

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

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

    @Data
    public static class Transport {
        @Schema(example = "false")
        public Boolean common = false;
        @Schema(example = "CE277DG")
        public String immatriculation;
        @Schema(example = "CLÉMENT")
        public String courier;

        public Boolean getCommon() {
            return common;
        }

        public void setCommon(Boolean common) {
            this.common = common;
        }

        public String getImmatriculation() {
            return immatriculation;
        }

        public void setImmatriculation(String immatriculation) {
            this.immatriculation = immatriculation;
        }

        public String getCourier() {
            return courier;
        }

        public void setCourier(String courier) {
            this.courier = courier;
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

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public Integer getNights() {
        return nights;
    }

    public void setNights(Integer nights) {
        this.nights = nights;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public Signatory getUch() {
        return uch;
    }

    public void setUch(Signatory uch) {
        this.uch = uch;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getRequesterSignature() {
        return requesterSignature;
    }

    public void setRequesterSignature(String requesterSignature) {
        this.requesterSignature = requesterSignature;
    }

    public Double getChargeSupport() {
        return chargeSupport;
    }

    public void setChargeSupport(Double chargeSupport) {
        this.chargeSupport = chargeSupport;
    }

    public Double getMissionFees() {
        return missionFees;
    }

    public void setMissionFees(Double missionFees) {
        this.missionFees = missionFees;
    }

    public Double getTransportFees() {
        return transportFees;
    }

    public void setTransportFees(Double transportFees) {
        this.transportFees = transportFees;
    }

    public String getAuthorisationNumber() {
        return authorisationNumber;
    }

    public void setAuthorisationNumber(String authorisationNumber) {
        this.authorisationNumber = authorisationNumber;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }
}
