package com.ccabank.paperless.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date getFirstDayOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);       // premier jour
        cal.set(Calendar.HOUR_OF_DAY, 0);        // minuit
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getLastDayOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // dernier jour
        cal.set(Calendar.HOUR_OF_DAY, 23);       // fin de journée
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static Date convertToDate(Object object) throws ParseException {
        if (object == null) {
            return null;
        }

        if (object instanceof Date) {
            return (Date) object;
        }

        if (object instanceof Long) {
            return new Date((Long) object); // milliseconds timestamp
        }

        if (object instanceof String) {
            // Try multiple date formats
            String str = (String) object;
            String[] formats = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy", "EEE MMM dd HH:mm:ss zzz yyyy"};

            for (String format : formats) {
                try {
                    return new SimpleDateFormat(format).parse(str);
                } catch (ParseException ignored) {}
            }
            throw new ParseException("Unparseable date: " + str, 0);
        }

        if (object instanceof LocalDate) {
            return Date.from(((LocalDate) object).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        if (object instanceof LocalDateTime) {
            return Date.from(((LocalDateTime) object).atZone(ZoneId.systemDefault()).toInstant());
        }

        throw new IllegalArgumentException("Cannot convert type to Date: " + object.getClass());
    }

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
            log.info("Invalid date format: " + e.getMessage());
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
