package com.ccabank.memoservice.process.absence.domain;


import lombok.Getter;

@Getter
public enum ReasonAbsence {
     MATERNITY("Maternité", true, 3),
     BAPTEME("Baptème", true, 3),
     OTHER("Autres", false, 0);

     private final String name;
     private final boolean conventional;
     private final int nbDays;


    ReasonAbsence(String name, boolean conventional, int nbDays) {
        this.name = name;
        this.conventional = conventional;
        this.nbDays = nbDays;
    }
}
