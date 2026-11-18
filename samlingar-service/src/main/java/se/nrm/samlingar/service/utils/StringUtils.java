package se.nrm.samlingar.service.utils;

public class StringUtils {

    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        int firstCodePoint = str.codePointAt(0);
        int titleCaseCodePoint = Character.toTitleCase(firstCodePoint);
        if (firstCodePoint == titleCaseCodePoint) {
            return str; // Optimization: avoid allocating a new String if already capitalized
        }
        return new StringBuilder(str.length())
                .appendCodePoint(titleCaseCodePoint)
                .append(str, Character.charCount(firstCodePoint), str.length())
                .toString();
    }
}
