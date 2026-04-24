package org.parsers;

import org.model.MissionOutcome;
import org.model.SorcererRank;
import org.model.TechniqueType;
import org.model.ThreatLevel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ParserUtils {
    private ParserUtils() {}

    public static String ext(String name) {
        int dot = name.lastIndexOf('.');
        return dot < 0 ? "" : name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    public static String firstNonBlankLine(String content) {
        for (String line : content.split("\\R")) {
            if (!line.isBlank()) return line.trim();
        }
        return "";
    }

    public static LocalDate date(String value) {
        return LocalDate.parse(cleanScalar(value));
    }

    public static LocalDateTime dateTime(String value) {
        return LocalDateTime.parse(cleanScalar(value));
    }

    public static long number(String value) {
        return Long.parseLong(cleanScalar(value).replace("_", ""));
    }

    public static Integer integerOrNull(String value) {
        String clean = cleanScalar(value);
        return clean.isEmpty() ? null : Integer.parseInt(clean);
    }

    public static Long longOrNull(String value) {
        String clean = cleanScalar(value);
        return clean.isEmpty() ? null : Long.parseLong(clean.replace("_", ""));
    }

    public static Boolean boolOrNull(String value) {
        String clean = cleanScalar(value);
        if (clean.isEmpty()) return null;
        return Boolean.parseBoolean(clean);
    }

    public static MissionOutcome outcome(String v) { return enumValue(MissionOutcome.class, v, MissionOutcome.UNKNOWN); }
    public static ThreatLevel threat(String v) { return enumValue(ThreatLevel.class, v, ThreatLevel.UNKNOWN); }
    public static SorcererRank rank(String v) { return enumValue(SorcererRank.class, v, SorcererRank.UNKNOWN); }
    public static TechniqueType techniqueType(String v) { return enumValue(TechniqueType.class, v, TechniqueType.UNKNOWN); }

    public static String cleanScalar(String value) {
        if (value == null) return "";
        String result = value.trim();
        if ((result.startsWith("\"") && result.endsWith("\"")) || (result.startsWith("'") && result.endsWith("'"))) {
            result = result.substring(1, result.length() - 1);
        }
        return result.trim();
    }

    public static List<String> asStringList(Object value) {
        List<String> result = new ArrayList<>();
        if (value == null) return result;
        if (value instanceof List<?> list) {
            for (Object item : list) {
                if (item != null) result.add(String.valueOf(item));
            }
            return result;
        }
        result.add(String.valueOf(value));
        return result;
    }

    public static Map<String, Object> mapOf() {
        return new LinkedHashMap<>();
    }

    public static List<Object> listOf() {
        return new ArrayList<>();
    }

    private static <T extends Enum<T>> T enumValue(Class<T> cls, String value, T fallback) {
        try {
            return Enum.valueOf(cls, cleanScalar(value).toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return fallback;
        }
    }
}
