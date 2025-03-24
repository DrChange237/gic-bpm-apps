package com.ccabank.memoservice.util;

import com.ccabank.memoservice.config.HolidayConfig;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

public class WorkDayCalculator {


    public static  String getDateRangeAsString(LocalDate startDate, LocalDate endDate) {
        // Formatter les mois en texte
        String startMonth = startDate.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
        String endMonth = endDate.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);

        // Obtenir les années
        int startYear = startDate.getYear();
        int endYear = endDate.getYear();

        return String.format("%s %d à %s %d", startMonth, startYear, endMonth, endYear);
    }


    public static  LocalDate addBusinessDays(LocalDate startDate, int daysToAdd) {
        List<LocalDate> holidaySet = HolidayConfig.getHolidays();
        LocalDate currentDate = startDate;
        int addedDays = 0;
        while (addedDays < daysToAdd) {
            currentDate = currentDate.plusDays(1);
            // Vérifiez si c'est un dimanche ou un jour férié
            if (currentDate.getDayOfWeek() != DayOfWeek.SUNDAY && !holidaySet.contains(currentDate)) {
                addedDays++;
            }
        }
        return currentDate;
    }


    public static int calculateNights(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isAfter(checkOut)) {
            throw new IllegalArgumentException("La date d'arrivée doit être avant la date de départ");
        }
        return Math.toIntExact(ChronoUnit.DAYS.between(checkIn, checkOut));
    }


    public static long calculateWorkdays(LocalDate startDate, LocalDate endDate) {
        long workdays = 0;
        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            if (isWeekday(startDate)) {
                workdays++;
            }
            startDate = startDate.plusDays(1);
        }
        return workdays - 1;
    }

    private static boolean isWeekday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return  dayOfWeek != DayOfWeek.SUNDAY;
    }
}
