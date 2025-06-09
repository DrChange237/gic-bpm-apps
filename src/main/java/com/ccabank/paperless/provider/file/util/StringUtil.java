package com.ccabank.paperless.provider.file.util;

import java.text.Normalizer;

public class StringUtil {

    public static String normalizeUri(String input) {
        input = input.replaceAll("đ", "d").replace("Đ", "D");
        input = stripAccents(input);
        return input.trim().replaceAll("[^a-zA-Z0-9]+", "-").toLowerCase();
    }

    private static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
}


