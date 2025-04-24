package com.ccabank.memoservice.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {

    public static boolean isDatePassed(Date date) {
        Instant instant = date.toInstant();
        return instant.isBefore(Instant.now());
    }

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

    public static String timeAgo(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        long hours = ChronoUnit.HOURS.between(dateTime, now);
        long days = ChronoUnit.DAYS.between(dateTime, now);
        long weeks = ChronoUnit.WEEKS.between(dateTime, now);
        long months = ChronoUnit.MONTHS.between(dateTime, now);
        long years = ChronoUnit.YEARS.between(dateTime, now);

        if (years > 0) {
            return "il y a " + years + " an" + (years > 1 ? "s" : "");
        } else if (months > 0) {
            return "il y a " + months + " mois";
        } else if (weeks > 0) {
            return "il y a " + weeks + " semaine" + (weeks > 1 ? "s" : "");
        } else if (days > 0) {
            return "il y a " + days + " jour" + (days > 1 ? "s" : "");
        } else if (hours > 0) {
            return "il y a " + hours + " heure" + (hours > 1 ? "s" : "");
        } else if (minutes > 0) {
            return "il y a " + minutes + " minute" + (minutes > 1 ? "s" : "");
        } else {
            return "à l'instant";
        }
    }
}
