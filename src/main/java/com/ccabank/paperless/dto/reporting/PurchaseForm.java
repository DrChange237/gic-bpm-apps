package com.ccabank.paperless.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseForm {

    // Étape 1
    @Schema(example = "202408080001", description = "Numéro de la demande")
    private String number = "202408080001";

    @Schema(description = "Date d'initiation de la demande")
    private LocalDate date = LocalDate.now();

    @Schema(description = "Date de déclenchement")
    private LocalDate triggerDate = LocalDate.now();

    @Schema(example = "Achat d'un ordinateur portable HP pour bureau", description = "Objet de la demande")
    private String object = "Achat d'un ordinateur portable HP pour bureau";

    @Schema(example = "AKWA", description = "Agence / Service demandeur")
    private String branch = "AKWA";

    @Schema(example = "EG-STORE, MATRIX", description = "Fournisseurs soumissionnaires")
    private String providers = "EG-STORE, MATRIX";

    @Schema(example = "EG-STORE", description = "Fournisseur sélectionné")
    private String provider = "EG-STORE";

    @Schema(description = "Délai de livraison")
    private LocalDate deliveryDelay = LocalDate.now();

    @Schema(example = "VIREMENT BANCAIRE", description = "Type de règlement")
    private String settlementMode = "VIREMENT BANCAIRE";

    @Schema(description = "Numéro du compte à impacter", example = "1000 - 00671459701 - 67")
    private String accountNumber = "1000 - 00671459701 - 67";

    @Schema(description = "Liste des articles à acheter")
    private List<Article> articles = new ArrayList<>();

    @Schema(example = "Payer à la livraison")
    private String observation = "Payer à la livraison";

    // Étape 2
    @Schema(description = "Date de réception")
    private LocalDate receptionDate2 = LocalDate.now();

    @Schema(description = "Date de transmission")
    private LocalDate transmissionDate2 = LocalDate.now();

    @Schema(example = "true", description = "Opportune (OUI = true , NON = false)")
    private boolean necessity = true;

    @Schema(example = "1", description = "Disponibilité du budget (1 = mensuel, 2 = annuel, 3 = disponible)")
    private int budgetAvailability = 1;

    @Schema(description = "Avis et signature du CB (Contrôle budgétaire)")
    private Advice budgetControl = new Advice();

    // Étape 3
    @Schema(description = "Date de réception")
    private LocalDate receptionDate3 = LocalDate.now();

    @Schema(description = "Date de transmission")
    private LocalDate transmissionDate3 = LocalDate.now();

    @Schema(description = "Avis et signature de la fiscalité")
    private Advice fiscal = new Advice();

    // Étape 4
    private Signatory initiator = new Signatory();
    private Signatory supervisor = new Signatory();
    private Signatory armg = new Signatory();
    private Signatory rmg = new Signatory();
    private Signatory daf = new Signatory();
    private Signatory dga = new Signatory();
    private Signatory dg = new Signatory();

    // Étape 5
    @Schema(description = "Date de réception")
    private LocalDate receptionDate5 = LocalDate.now();

    @Schema(description = "Date de saisie")
    private LocalDate treatmentDate5 = LocalDate.now();

    @Schema(example = "LONLA GATIEN JORDAN", description = "Opérateur de la saisie de la DOP")
    private String operatorDop = "LONLA GATIEN JORDAN";

    @Schema(example = "SIMO WILFRIED", description = "Superviseur de la saisie")
    private String supervisorDop = "SIMO WILFRIED";

    @Schema(description = "Observations générales à renseigner au besoin avec signature et date")
    private Advice observationDop = new Advice();

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getTriggerDate() {
        return triggerDate;
    }

    public void setTriggerDate(LocalDate triggerDate) {
        this.triggerDate = triggerDate;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getProviders() {
        return providers;
    }

    public void setProviders(String providers) {
        this.providers = providers;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public LocalDate getDeliveryDelay() {
        return deliveryDelay;
    }

    public void setDeliveryDelay(LocalDate deliveryDelay) {
        this.deliveryDelay = deliveryDelay;
    }

    public String getSettlementMode() {
        return settlementMode;
    }

    public void setSettlementMode(String settlementMode) {
        this.settlementMode = settlementMode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDate getReceptionDate2() {
        return receptionDate2;
    }

    public void setReceptionDate2(LocalDate receptionDate2) {
        this.receptionDate2 = receptionDate2;
    }

    public LocalDate getTransmissionDate2() {
        return transmissionDate2;
    }

    public void setTransmissionDate2(LocalDate transmissionDate2) {
        this.transmissionDate2 = transmissionDate2;
    }

    public boolean isNecessity() {
        return necessity;
    }

    public void setNecessity(boolean necessity) {
        this.necessity = necessity;
    }

    public int getBudgetAvailability() {
        return budgetAvailability;
    }

    public void setBudgetAvailability(int budgetAvailability) {
        this.budgetAvailability = budgetAvailability;
    }

    public Advice getBudgetControl() {
        return budgetControl;
    }

    public void setBudgetControl(Advice budgetControl) {
        this.budgetControl = budgetControl;
    }

    public LocalDate getReceptionDate3() {
        return receptionDate3;
    }

    public void setReceptionDate3(LocalDate receptionDate3) {
        this.receptionDate3 = receptionDate3;
    }

    public LocalDate getTransmissionDate3() {
        return transmissionDate3;
    }

    public void setTransmissionDate3(LocalDate transmissionDate3) {
        this.transmissionDate3 = transmissionDate3;
    }

    public Advice getFiscal() {
        return fiscal;
    }

    public void setFiscal(Advice fiscal) {
        this.fiscal = fiscal;
    }

    public Signatory getInitiator() {
        return initiator;
    }

    public void setInitiator(Signatory initiator) {
        this.initiator = initiator;
    }

    public Signatory getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Signatory supervisor) {
        this.supervisor = supervisor;
    }

    public Signatory getArmg() {
        return armg;
    }

    public void setArmg(Signatory armg) {
        this.armg = armg;
    }

    public Signatory getRmg() {
        return rmg;
    }

    public void setRmg(Signatory rmg) {
        this.rmg = rmg;
    }

    public Signatory getDaf() {
        return daf;
    }

    public void setDaf(Signatory daf) {
        this.daf = daf;
    }

    public Signatory getDga() {
        return dga;
    }

    public void setDga(Signatory dga) {
        this.dga = dga;
    }

    public Signatory getDg() {
        return dg;
    }

    public void setDg(Signatory dg) {
        this.dg = dg;
    }

    public LocalDate getReceptionDate5() {
        return receptionDate5;
    }

    public void setReceptionDate5(LocalDate receptionDate5) {
        this.receptionDate5 = receptionDate5;
    }

    public LocalDate getTreatmentDate5() {
        return treatmentDate5;
    }

    public void setTreatmentDate5(LocalDate treatmentDate5) {
        this.treatmentDate5 = treatmentDate5;
    }

    public String getOperatorDop() {
        return operatorDop;
    }

    public void setOperatorDop(String operatorDop) {
        this.operatorDop = operatorDop;
    }

    public String getSupervisorDop() {
        return supervisorDop;
    }

    public void setSupervisorDop(String supervisorDop) {
        this.supervisorDop = supervisorDop;
    }

    public Advice getObservationDop() {
        return observationDop;
    }

    public void setObservationDop(Advice observationDop) {
        this.observationDop = observationDop;
    }

    @Data
    public static class Article {
        @Schema(example = "HP ProBook", description = "Désignation")
        private String name = "HP ProBook";

        @Schema(example = "1", description = "Quantité")
        private int quantity = 1;

        @Schema(example = "500000", description = "Prix Unitaire")
        private long price = 500;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public long getPrice() {
            return price;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Prix Total")
        public long getTotal(){
            return quantity * price;
        }
    }

    @Data
    @Schema(name = "Operator")
    public static class Signatory {
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

    @Data
    public static class Advice {
        @Schema(example = "FAVORABLE")
        private String opinion = "FAVORABLE";

        @Schema(example = "SIMO PATRICK")
        private String name = "SIMO PATRICK";

        @Schema(example = " ", description = "Base64-encoded image")
        private String signature = "";

        @NotNull
        private LocalDate date = LocalDate.now();

        public String getOpinion() {
            return opinion;
        }

        public void setOpinion(String opinion) {
            this.opinion = opinion;
        }

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

