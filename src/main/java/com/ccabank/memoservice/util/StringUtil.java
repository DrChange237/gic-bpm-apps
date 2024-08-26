package com.ccabank.memoservice.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : entity-service
 * @Package : com.ccabank.userservice.util
 * <p>
 * @date: 20/06/2023
 * @time: 09:54
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
public final class StringUtil {

    public static final String ENCODING_UTF8 = "UTF-8";

    private static final Pattern SPLIT_PATTERN = Pattern.compile("\"([^\"]*)\"|(\\S+)");

    private static final Set<String> CONJUNCTIONS = new HashSet<>(Arrays.asList("et", "ou", "ni", "car", "donc", "mais", "or", "si", "que", "à", "de", "des", "au", "aux"));

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

    private static final String[][] MIME_TYPES = {
            {"mp3", "audio/mpeg"},
            {"ogg", "audio/ogg"},
            {"oga", "audio/ogg"},
            {"opus", "audio/ogg"},
            {"ogx", "application/ogg"},
            {"aac", "audio/mp4"},
            {"m4a", "audio/mp4"},
            {"m4b", "audio/mp4"},
            {"flac", "audio/flac"},
            {"wav", "audio/x-wav"},
            {"wma", "audio/x-ms-wma"},
            {"ape", "audio/x-monkeys-audio"},
            {"mpc", "audio/x-musepack"},
            {"shn", "audio/x-shn"},

            {"flv", "video/x-flv"},
            {"avi", "video/avi"},
            {"mpg", "video/mpeg"},
            {"mpeg", "video/mpeg"},
            {"mp4", "video/mp4"},
            {"m4v", "video/x-m4v"},
            {"mkv", "video/x-matroska"},
            {"mov", "video/quicktime"},
            {"wmv", "video/x-ms-wmv"},
            {"ogv", "video/ogg"},
            {"divx", "video/divx"},
            {"m2ts", "video/MP2T"},
            {"ts", "video/MP2T"},
            {"webm", "video/webm"},

            {"gif", "image/gif"},
            {"jpg", "image/jpeg"},
            {"jpeg", "image/jpeg"},
            {"png", "image/png"},
            {"bmp", "image/bmp"},
    };

    private static final String[] FILE_SYSTEM_UNSAFE = {"/", "\\", "..", ":", "\"", "?", "*", "|"};

    /**
     * Disallow external instantiation.
     */
    private StringUtil() {
    }

    /**
     * This Java function returns the MIME type of a file based on its suffix.
     *
     * @param suffix The suffix parameter is a string representing the file extension or suffix of a
     *               file, for example, ".txt" or ".pdf". The method searches for a matching MIME type
     *               based on this suffix.
     * @return If the suffix matches any of the MIME types in the `MIME_TYPES` array, then the
     * corresponding MIME type is returned. Otherwise, the default MIME type
     * "application/octet-stream" is returned.
     */
    public static String getMimeType(String suffix) {
        for (String[] map : MIME_TYPES) {
            if (map[0].equalsIgnoreCase(suffix) || ('.' + map[0]).equalsIgnoreCase(suffix)) {
                return map[1];
            }
        }
        return "application/octet-stream";
    }

    /**
     * This function returns the MIME type of a given file suffix, and if the "sonos" parameter is
     * true, it returns "audio/aac" instead of "audio/mp4" for ALAC and AAC files in MP4 containers.
     *
     * @param suffix The file extension of the file for which we want to determine the MIME type. For
     *               example, ".mp3", ".wav", ".flac", etc.
     * @param sonos  A boolean variable that indicates whether the MIME type is being checked for
     *               compatibility with Sonos devices. If it is true, the method will return
     *               "audio/aac" instead of "audio/mp4" for ALAC and AAC files in MP4 containers. If
     *               it is false, the method will return
     * @return The method `getMimeType` returns a string representing the MIME type of a file based on
     * its suffix. The method `getMimeType` with a boolean parameter `sonos` is being called, and it
     * returns a string representing the MIME type of a file based on its suffix. If `sonos` is true
     * and the MIME type is "audio/mp4", then the method returns "audio/a
     */
    public static String getMimeType(String suffix, boolean sonos) {
        String result = getMimeType(suffix);

        // Sonos doesn't work with "audio/mp4" but needs "audio/aac" for ALAC and AAC (in MP4 container)
        return sonos && "audio/mp4".equals(result) ? "audio/aac" : result;
    }

    /**
     * The function returns the file extension suffix for a given MIME type.
     *
     * @param mimeType The parameter "mimeType" is a String variable that represents the MIME type of
     *                 a file. MIME type is a standard way to identify the type of a file based on its
     *                 contents and is used by web browsers and servers to handle different types of
     *                 files appropriately. Examples of MIME types include "text/plain
     * @return The method is returning a String value which represents the file extension or suffix
     * associated with the given MIME type. If the MIME type is not found in the MIME_TYPES array, the
     * method returns null.
     */
    public static String getSuffix(String mimeType) {
        for (String[] map : MIME_TYPES) {
            if (map[1].equalsIgnoreCase(mimeType)) {
                return map[0];
            }
        }
        return null;
    }

    /**
     * Formats a duration with minutes and seconds, e.g., "4:34" or "93:45"
     */
    public static String formatDurationMSS(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("seconds must be >= 0");
        }
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    /**
     * Formats a duration with H:MM:SS, e.g., "1:33:45"
     */
    public static String formatDurationHMMSS(int seconds) {
        int hours = seconds / 3600;
        seconds -= hours * 3600;

        return String.format("%d:%s%s", hours, seconds < 600 ? "0" : "", formatDurationMSS(seconds));
    }

    /**
     * Formats a duration to M:SS or H:MM:SS
     */
    public static String formatDuration(int seconds) {
        if (seconds >= 3600) {
            return formatDurationHMMSS(seconds);
        }
        return formatDurationMSS(seconds);
    }

    /**
     * Splits the input string. White space is interpreted as separator token. Double quotes are
     * interpreted as grouping operator. <br/> For instance, the input <code>"u2 rem "greatest
     * hits""</code> will return an array with three elements: <code>{"u2", "rem", "greatest
     * hits"}</code>
     *
     * @param input The input string.
     * @return Array of elements.
     */
    public static String[] split(String input) {
        if (input == null) {
            return new String[0];
        }

        List<String> result = new ArrayList<>();
        Matcher m = SPLIT_PATTERN.matcher(input);
        while (m.find()) {
            if (m.group(1) != null) {
                result.add(m.group(1)); // quoted string
            } else {
                result.add(m.group(2)); // unquoted string
            }
        }

        return result.toArray(new String[result.size()]);
    }

    /**
     * Cette fonction encode une chaîne donnée à l'aide de l'encodage UTF-8 et renvoie la chaîne
     * encodée.
     *
     * @param s La chaîne d'entrée qui doit être encodée en URL.
     * @return La méthode `urlEncode` renvoie une chaîne codée en URL de la chaîne d'entrée `s`. Il
     * utilise la méthode `URLEncoder.encode` pour coder la chaîne à l'aide du codage de caractères
     * UTF-8. Si l'encodage UTF-8 n'est pas pris en charge, la méthode lève une `RuntimeException`.
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, StringUtil.ENCODING_UTF8);
        } catch (UnsupportedEncodingException x) {
            throw new RuntimeException(x);
        }
    }

    /**
     * URL-decodes the input value using UTF-8.
     */
    public static String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, StringUtil.ENCODING_UTF8);
        } catch (UnsupportedEncodingException x) {
            throw new RuntimeException(x);
        }
    }


    /**
     * La fonction remplace tous les caractères non sécurisés d'un nom de fichier par un trait d'union
     * pour le rendre sûr pour une utilisation dans un système de fichiers.
     *
     * @param filename Le paramètre "filename" est une variable String qui représente le nom d'un
     *                 fichier. La méthode "fileSystemSafe" prend ce nom de fichier en entrée et
     *                 renvoie une version modifiée du nom de fichier qui peut être utilisée en toute
     *                 sécurité dans un système de fichiers.
     * @return La méthode renvoie une chaîne qui a été modifiée pour remplacer tous les caractères
     * considérés comme dangereux pour une utilisation dans un système de fichiers par un trait
     * d'union ("-").
     */
    public static String fileSystemSafe(String filename) {
        for (String s : FILE_SYSTEM_UNSAFE) {
            filename = filename.replace(s, "-");
        }
        return filename;
    }

    /**
     * La fonction supprime toutes les balises de balisage HTML d'une chaîne donnée.
     *
     * @param s La chaîne d'entrée qui peut contenir un balisage HTML.
     * @return La méthode `removeMarkup` renvoie une `String` avec tout le balisage HTML supprimé de
     * l'entrée `String` `s`. Si `s` vaut `null`, la méthode renvoie `null`.
     */
    public static String removeMarkup(String s) {
        if (s == null) {
            return null;
        }
        return s.replaceAll("<.*?>", "");
    }

    /**
     * La fonction supprime les signes diacritiques d'une chaîne donnée.
     *
     * @param s Le paramètre "s" est une variable String qui représente la chaîne d'entrée dont nous
     *          voulons supprimer les signes diacritiques ou les accents.
     * @return La méthode renvoie une chaîne avec tous les signes diacritiques (accents) supprimés de
     * la chaîne d'entrée.
     */
    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    public static String normalizeUri(String input) {
        input = input.replaceAll("đ", "d").replace("Đ", "D");
        input = stripAccents(input);
        return input.trim().replaceAll("[^a-zA-Z0-9]+", "-").toLowerCase();
    }

    /**
     * La méthode `trimOrNull` prend une `value` d'entrée `String` et renvoie une version tronquée de
     * la chaîne d'entrée si elle n'est pas nulle ou vide. Si la chaîne d'entrée est nulle ou vide,
     * elle renvoie null. La méthode supprime tous les espaces blancs de début ou de fin de la chaîne
     * d'entrée à l'aide de la méthode `trim()`.
     *
     * @param value
     * @return
     */
    public static String trimOrNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty() ? true : false;
    }


    public static Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static String RandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1).limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    public static String getFileNameWithoutExtension(String fullname) {
        if (fullname == null) {
            return null;
        }
        return fullname.replaceFirst("[.][^.]+$", "");
    }
}
