package com.ccabank.memoservice.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class WorkDayCalculator {


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
        return workdays;
    }

    private static boolean isWeekday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
}
