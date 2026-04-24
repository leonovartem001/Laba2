package org.parsers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtMissionParser extends AbstractMissionParser {
    private static final Pattern OLD_INDEXED = Pattern.compile("(sorcerer|technique)\\[(\\d+)]\\.(\\w+)");

    @Override
    public boolean canParse(Path path, String content) {
        String ext = ParserUtils.ext(path.getFileName().toString());
        String first = ParserUtils.firstNonBlankLine(content);
        return ext.equals("txt") || first.startsWith("[MISSION]") || first.startsWith("missionId:");
    }

    @Override
    protected Map<String, Object> toNormalizedMap(Path path, String content) {
        String first = ParserUtils.firstNonBlankLine(content);
        if (first.startsWith("[MISSION]")) {
            return parseSectioned(content);
        }
        return parseLegacy(content);
    }

    @Override
    public String getFormatName() { return "TXT"; }

    @Override
    public int priority() { return 50; }

    private Map<String, Object> parseLegacy(String content) {
        Map<String, Object> root = new LinkedHashMap<>();
        Map<String, Object> curse = new LinkedHashMap<>();
        Map<Integer, Map<String, Object>> sorcerers = new TreeMap<>();
        Map<Integer, Map<String, Object>> techniques = new TreeMap<>();

        for (String rawLine : content.split("\\R")) {
            if (rawLine.isBlank() || !rawLine.contains(":")) continue;
            String[] parts = rawLine.split(":", 2);
            String key = parts[0].trim();
            String value = parts[1].trim();
            switch (key) {
                case "missionId" -> root.put("missionId", value);
                case "date" -> root.put("date", value);
                case "location" -> root.put("location", value);
                case "outcome" -> root.put("outcome", value);
                case "damageCost" -> root.put("damageCost", value);
                case "curse.name" -> curse.put("name", value);
                case "curse.threatLevel" -> curse.put("threatLevel", value);
                default -> mapLegacyIndexed(key, value, sorcerers, techniques);
            }
        }
        root.put("curse", curse);
        root.put("sorcerers", new ArrayList<>(sorcerers.values()));
        root.put("techniques", new ArrayList<>(techniques.values()));
        return root;
    }

    private void mapLegacyIndexed(String key, String value,
                                  Map<Integer, Map<String, Object>> sorcerers,
                                  Map<Integer, Map<String, Object>> techniques) {
        Matcher matcher = OLD_INDEXED.matcher(key);
        if (!matcher.matches()) return;
        int index = Integer.parseInt(matcher.group(2));
        String field = matcher.group(3);
        if (matcher.group(1).equals("sorcerer")) {
            sorcerers.computeIfAbsent(index, i -> new LinkedHashMap<>()).put(field, value);
        } else {
            techniques.computeIfAbsent(index, i -> new LinkedHashMap<>()).put(field, value);
        }
    }

    private Map<String, Object> parseSectioned(String content) {
        Map<String, Object> root = new LinkedHashMap<>();
        Map<String, Object> curse = new LinkedHashMap<>();
        List<Object> sorcerers = new ArrayList<>();
        List<Object> techniques = new ArrayList<>();
        Map<String, Object> environment = new LinkedHashMap<>();

        String currentSection = "";
        Map<String, Object> currentListItem = null;

        for (String rawLine : content.split("\\R")) {
            String line = rawLine.trim();
            if (line.isBlank()) continue;
            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1).trim();
                currentListItem = switch (currentSection) {
                    case "SORCERER", "TECHNIQUE" -> new LinkedHashMap<>();
                    default -> null;
                };
                if ("SORCERER".equals(currentSection)) sorcerers.add(currentListItem);
                if ("TECHNIQUE".equals(currentSection)) techniques.add(currentListItem);
                continue;
            }
            if (!line.contains("=")) continue;
            String[] parts = line.split("=", 2);
            String key = parts[0].trim();
            String value = parts[1].trim();
            switch (currentSection) {
                case "MISSION" -> root.put(key, value);
                case "CURSE" -> curse.put(key, value);
                case "SORCERER", "TECHNIQUE" -> {
                    if (currentListItem != null) currentListItem.put(key, value);
                }
                case "ENVIRONMENT" -> environment.put(key, value);
                default -> root.put(key, value);
            }
        }
        root.put("curse", curse);
        root.put("sorcerers", sorcerers);
        root.put("techniques", techniques);
        if (!environment.isEmpty()) root.put("environment", environment);
        return root;
    }
}
