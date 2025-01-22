package com.ccabank.memoservice.process.absence.domain;


import lombok.Getter;

@Getter
public enum ReasonAbsence {
     MARIAGE("Mariage", true, 5),
     CHILDBIRTH("Accouchement de l'épouse", true, 3),
     BAPTEME("Baptème d'un enfant", true, 2),
     COMMUNION("Première communion d'un enfant ", true, 1),
     MARIAGE_CHILD("Mariage d'un enfant", true, 2),
     DEATH_SPOUSE("Déces Conjoint", true, 5),
    DEATH_CHILD("Déces d'un enfant", true, 3),
    DEATH_PARENT("Déces d'un parent", true, 3),
    DEATH_BROTHER("Déces d'un frère ou d'une soeur", true, 2),
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
