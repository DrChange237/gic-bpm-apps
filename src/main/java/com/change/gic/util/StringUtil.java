package com.change.gic.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class StringUtil {

    private static final Set<String> CONJUNCTIONS = new HashSet<>(Arrays.asList("et", "ou", "ni", "car", "donc", "mais", "or", "si", "que", "à", "de", "des", "au", "aux", "&"));

    public static  String getFirstLetters(String input) {
        return Arrays.stream(input.split(" "))
                .filter(word -> !CONJUNCTIONS.contains(word.toLowerCase()))
                .map(String::trim)
                .map(word -> word.substring(0, 1).toUpperCase(Locale.US))
                .collect(Collectors.joining());
    }

    public static String transformUsernameToName(String name) {
        // Séparer le nom en prénom et nom de famille
        String[] nameParts = name.split("\\.");

        // Mettre le prénom en majuscules
        String firstName = nameParts[0].toUpperCase();

        // Mettre le nom de famille en majuscules
        String lastName = nameParts[1].toUpperCase();

        // Recombiner le prénom et le nom de famille
        return firstName + " " + lastName;
    }

}
