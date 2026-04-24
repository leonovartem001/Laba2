package org.parsers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EventMissionParser extends AbstractMissionParser {
    @Override
    public boolean canParse(Path path, String content) {
        return ParserUtils.firstNonBlankLine(content).startsWith("MISSION_CREATED|");
    }

    @Override
    protected Map<String, Object> toNormalizedMap(Path path, String content) {
        Map<String, Object> root = new LinkedHashMap<>();
        Map<String, Object> curse = new LinkedHashMap<>();
        List<Object> sorcerers = new ArrayList<>();
        List<Object> techniques = new ArrayList<>();
        Map<String, Object> civilianImpact = new LinkedHashMap<>();
        Map<String, Object> enemyActivity = new LinkedHashMap<>();
        List<String> attackPatterns = new ArrayList<>();
        List<Object> timeline = new ArrayList<>();

        for (String rawLine : content.split("\\R")) {
            String line = rawLine.trim();
            if (line.isBlank()) continue;
            String[] parts = line.split("\\|");
            String type = parts[0].trim();
            switch (type) {
                case "MISSION_CREATED" -> {
                    root.put("missionId", parts[1]);
                    root.put("date", parts[2]);
                    root.put("location", parts[3]);
                }
                case "CURSE_DETECTED" -> {
                    curse.put("name", parts[1]);
                    curse.put("threatLevel", parts[2]);
                }
                case "SORCERER_ASSIGNED" -> {
                    Map<String, Object> sorcerer = new LinkedHashMap<>();
                    sorcerer.put("name", parts[1]);
                    sorcerer.put("rank", parts[2]);
                    sorcerers.add(sorcerer);
                }
                case "TECHNIQUE_USED" -> {
                    Map<String, Object> technique = new LinkedHashMap<>();
                    technique.put("name", parts[1]);
                    technique.put("type", parts[2]);
                    technique.put("owner", parts[3]);
                    technique.put("damage", parts[4]);
                    techniques.add(technique);
                }
                case "TIMELINE_EVENT" -> {
                    Map<String, Object> event = new LinkedHashMap<>();
                    event.put("timestamp", parts[1]);
                    event.put("type", parts[2]);
                    event.put("description", parts[3]);
                    timeline.add(event);
                }
                case "ENEMY_ACTION" -> attackPatterns.add(parts[1] + ": " + parts[2]);
                case "CIVILIAN_IMPACT" -> {
                    for (int i = 1; i < parts.length; i++) {
                        String[] item = parts[i].split("=", 2);
                        if (item.length == 2) civilianImpact.put(item[0], item[1]);
                    }
                }
                case "MISSION_RESULT" -> {
                    root.put("outcome", parts[1]);
                    for (int i = 2; i < parts.length; i++) {
                        String[] item = parts[i].split("=", 2);
                        if (item.length == 2 && item[0].equals("damageCost")) root.put("damageCost", item[1]);
                    }
                }
                default -> root.put(type, List.of(parts));
            }
        }

        if (!curse.isEmpty()) root.put("curse", curse);
        if (!sorcerers.isEmpty()) root.put("sorcerers", sorcerers);
        if (!techniques.isEmpty()) root.put("techniques", techniques);
        if (!civilianImpact.isEmpty()) root.put("civilianImpact", civilianImpact);
        if (!attackPatterns.isEmpty()) {
            enemyActivity.put("attackPatterns", attackPatterns);
            root.put("enemyActivity", enemyActivity);
        }
        if (!timeline.isEmpty()) root.put("timeline", timeline);
        return root;
    }

    @Override
    public String getFormatName() { return "EVENT_PROTOCOL"; }

    @Override
    public int priority() { return 10; }
}
