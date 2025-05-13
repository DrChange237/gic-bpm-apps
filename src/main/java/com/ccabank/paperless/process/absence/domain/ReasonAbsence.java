package com.ccabank.paperless.process.absence.domain;


import lombok.Getter;

@Getter
public enum ReasonAbsence {
     MARIAGE("Mariage", true, 5),
     ACCOUCHEMENT("Accouchement de l'épouse", true, 3),
     BAPTEME("Baptème d'un enfant", true, 2),
     COMMUNION("Première communion d'un enfant ", true, 1),
     MARIAGE_ENFANT("Mariage d'un enfant", true, 2),
     DECES_CONJOINT("Déces Conjoint", true, 5),
    DECES_ENFANT("Déces d'un enfant", true, 3),
    DECES_PARENT("Déces d'un parent", true, 3),
    DECES_FRERE("Déces d'un frère ou d'une soeur", true, 2),
    DEMENAGEMENT("Déménagement du travailleur", true, 2),
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
