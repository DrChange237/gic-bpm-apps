package com.change.gic.modules.core.util;

import java.time.LocalDate;
import java.time.Period;

public class DateUtils {

    public static Integer calculerAge(LocalDate dateNaissance) {
        if (dateNaissance == null) {
            return null;
            //throw new IllegalArgumentException("La date de naissance ne peut pas être nulle");
        }
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }

}
