package com.ccabank.memoservice.dto.reporting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Schema(description = "Demande d'autorisation d'absence")
public class AbsenceForm {
    @NotBlank
    @Schema(example = "Douala")
    private String place;
    @NotNull
    private LocalDate date;
    @Schema(example = " ")
    private String signature;

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
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    @Min(value = 1)
    @Schema(example = "3")
    private Integer days;
    @NotBlank
    @Schema(example = "Convalescence")
    private String reason;
    @NotNull
    private Deduction deduction;
    @NotBlank
    @Schema(example = "SIMO TRÉSOR WILFRIED")
    private String interim;
    @NotNull
    private Signatory signatory1;
    @NotNull
    private Signatory signatory2;
    @NotNull
    private Signatory headOffice;

    @NotNull
    private Settlement absence;
    @NotNull
    private Settlement stock;
    @NotNull
    private Settlement advice;
    @NotNull
    private Settlement rights;
    @NotNull
    private Settlement salary;
    @NotNull
    private Settlement vacation;


    private List<String> signatures;

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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Deduction getDeduction() {
        return deduction;
    }

    public void setDeduction(Deduction deduction) {
        this.deduction = deduction;
    }

    public String getInterim() {
        return interim;
    }

    public void setInterim(String interim) {
        this.interim = interim;
    }

    public Signatory getSignatory1() {
        return signatory1;
    }

    public void setSignatory1(Signatory signatory1) {
        this.signatory1 = signatory1;
    }

    public Signatory getSignatory2() {
        return signatory2;
    }

    public void setSignatory2(Signatory signatory2) {
        this.signatory2 = signatory2;
    }

    public Signatory getHeadOffice() {
        return headOffice;
    }

    public void setHeadOffice(Signatory headOffice) {
        this.headOffice = headOffice;
    }

    public Settlement getAbsence() {
        return absence;
    }

    public void setAbsence(Settlement absence) {
        this.absence = absence;
    }

    public Settlement getStock() {
        return stock;
    }

    public void setStock(Settlement stock) {
        this.stock = stock;
    }

    public Settlement getAdvice() {
        return advice;
    }

    public void setAdvice(Settlement advice) {
        this.advice = advice;
    }

    public Settlement getRights() {
        return rights;
    }

    public void setRights(Settlement rights) {
        this.rights = rights;
    }

    public Settlement getSalary() {
        return salary;
    }

    public void setSalary(Settlement salary) {
        this.salary = salary;
    }

    public Settlement getVacation() {
        return vacation;
    }

    public void setVacation(Settlement vacation) {
        this.vacation = vacation;
    }

    public List<String> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    @Data
    public static class Settlement {
        private Double paid;
        private Double unpaid;
    }

    @Data
    @Schema(name = "Avis")
    public static class Signatory {
        @NotBlank
        @Schema(example = "SIMO PATRICK")
        private String name;
        @Schema(example = " ", description = "Base64-encoded image")
        private String signature;
        @NotNull
        private LocalDate date;

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

    public enum Deduction {
        SALARY,
        VACATION
    }

}
