package com.ccabank.entityservice.provider.file.util;

import java.text.Normalizer;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : entity-service
 * @Package : com.ccabank.userservice.provider.file.util
 * <p>
 * @date: 19/06/2023
 * @time: 09:53
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
public class StringUtil {


    /**
     * The function normalizes a given URI by replacing special characters, stripping accents,
     * removing non-alphanumeric characters, and converting to lowercase.
     *
     * @param input The input parameter is a String that represents a URI (Uniform Resource
     *              Identifier).
     * @return The method `normalizeUri` returns a `String` that is the normalized version of the
     * input `String`. The normalization process involves replacing the characters "đ" and "Đ" with
     * "d" and "D" respectively, removing any accents from the input using the `stripAccents` method,
     * trimming the resulting `String`, and replacing any non-alphanumeric characters with a hyphen
     * ("-")
     */
    public static String normalizeUri(String input) {
        input = input.replaceAll("đ", "d").replace("Đ", "D");
        input = stripAccents(input);
        return input.trim().replaceAll("[^a-zA-Z0-9]+", "-").toLowerCase();
    }


    /**
     * The function removes diacritical marks from a given string.
     *
     * @param s The input string that needs to be stripped of diacritical marks (accented
     *          characters).
     * @return The method is returning a string with all diacritical marks (accents) removed from the
     * input string.
     */
    private static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
}


