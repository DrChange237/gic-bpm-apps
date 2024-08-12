package com.ccabank.memoservice.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class MemoForm {

    @NotBlank
    @Schema(example = "28122023-01")
    private String number = "28122023-01" ;

    @NotNull
    private LocalDate date = LocalDate.now();

    @NotBlank
    @Schema(example = "28122023-01DG/DS/DEDSD/2023")
    private String reference = "28122023-01/DG/DS/DEDSD/2023";

    @NotBlank
    @Schema(example = "Département étude et développement de solutions digitales")
    private String sender = "Département étude et développement de solutions digitales" ;

    @NotBlank
    @Schema(example = "Acquisition d'un <b>certificat SSL/TLS</b> pour le domaine <b>ccabank-app.com</b> et sous domaines")
    private String subject = "Acquisition d'un <b>certificat SSL/TLS</b> pour le domaine <b>ccabank-app.com</b> et sous domaines";

    @NotBlank
    @Schema(example = "Serveur de production")
    private String material =  "Serveur de production";

    @NotBlank
    @Schema(example = "Directeur Support")
    private String receiver = "Directeur Support";

    @NotBlank
    @Schema(example = "<p style='text-indent:50px'>Depuis quelques années, notre banque : <b>CCA BANK</b> s'est lancé dans un processus de <b>digitalisation</b>, celui-ci s'appuie sur l'utilisation des technologie numériques pour améliorer les opérations commerciales, financières et sociales au sein de la banque. Il vise également à transformer la matière dont nous interagissons avec nos clients et à maximiser le retour sur investissement (ROI).</p>")
    private String body = "";

    private List<Signatory> signatories = new ArrayList<>();

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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Signatory> getSignatories() {
        return signatories;
    }

    public void setSignatories(List<Signatory> signatories) {
        this.signatories = signatories;
    }

    @Data
    @Schema(name = "Approbation")
    public static class Signatory {
        @NotBlank
        @Schema(example = "SIMO PATRICK")
        private String name =  "SIMO PATRICK";
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

