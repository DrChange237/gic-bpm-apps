package com.change.gic.config;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class HolidayConfig {
        // Liste statique des jours fériés
        private static final List<LocalDate> HOLIDAY_LIST = Arrays.asList(
                LocalDate.of(LocalDate.now().getYear(), 1, 1),   // Jour de l'An
                LocalDate.of(LocalDate.now().getYear(), 4, 21),  // Pâques
                LocalDate.of(LocalDate.now().getYear(), 5, 1),   // Fête du Travail
                LocalDate.of(LocalDate.now().getYear(), 5, 20),   // 20 Mai
                LocalDate.of(LocalDate.now().getYear(), 7, 14),  // Fête Nationale
                LocalDate.of(LocalDate.now().getYear(), 8, 15),  // Assomption
                LocalDate.of(LocalDate.now().getYear(), 12, 25)  // Noël
        );

        // Méthode pour obtenir la liste des jours fériés
        public static  List<LocalDate> getHolidays() {
            return HOLIDAY_LIST;
        }

        // Méthode pour vérifier si une date est un jour férié
        public static boolean isHoliday(LocalDate date) {
            return HOLIDAY_LIST.contains(date);
        }

}
