package com.ccabank.memoservice.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateUtil {

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate convertDateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

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
