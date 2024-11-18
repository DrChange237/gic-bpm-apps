package com.ccabank.memoservice.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    public static LocalDate convertStringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // Ajustez le format selon vos besoins

        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            // Gérer l'erreur de parsing
            System.out.println("Invalid date format: " + e.getMessage());
            return null; // ou lancer une exception personnalisée
        }
    }
}
