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
}
