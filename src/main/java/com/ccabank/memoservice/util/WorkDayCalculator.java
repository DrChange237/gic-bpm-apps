package com.ccabank.memoservice.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WorkDayCalculator {

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

    public static void main(String[] args) {
        LocalDate startDate = LocalDate.of(2023, 5, 1);
        LocalDate endDate = LocalDate.of(2023, 5, 15);
        long workdays = calculateWorkdays(startDate, endDate);
        System.out.println("Nombre de jours ouvrables entre " + startDate + " et " + endDate + ": " + workdays);
    }
}
