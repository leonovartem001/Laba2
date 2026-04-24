package org.parsers;

import org.factory.MissionBuilder;
import org.model.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NormalizedMissionMapper {
    public void map(Map<String, Object> root, MissionBuilder builder) {
        if (root == null) {
            throw new IllegalArgumentException("Пустые данные миссии");
        }

        Map<String, Object> remaining = new LinkedHashMap<>(root);

        builder.missionId(stringValue(remaining.remove("missionId")));
        Object dateValue = remaining.remove("date");
        if (dateValue != null) builder.date(ParserUtils.date(String.valueOf(dateValue)));
        builder.location(stringValue(remaining.remove("location")));
        Object outcomeValue = remaining.remove("outcome");
        if (outcomeValue != null) builder.outcome(ParserUtils.outcome(String.valueOf(outcomeValue)));
        Object damageCostValue = remaining.remove("damageCost");
        if (damageCostValue != null && !String.valueOf(damageCostValue).isBlank()) {
            builder.damageCost(ParserUtils.number(String.valueOf(damageCostValue)));
        }

        mapCurse(remaining.remove("curse"), builder);
        mapSorcerers(remaining.remove("sorcerers"), builder);
        mapTechniques(remaining.remove("techniques"), builder);
        mapEconomicAssessment(remaining.remove("economicAssessment"), builder);
        mapCivilianImpact(remaining.remove("civilianImpact"), builder);
        Object enemyActivity = remaining.containsKey("enemyActivity") ? remaining.remove("enemyActivity") : remaining.remove("enemyActions");
        mapEnemyActivity(enemyActivity, builder);
        Object environment = remaining.containsKey("environmentConditions")
                ? remaining.remove("environmentConditions") : remaining.remove("environment");
        mapEnvironment(environment, builder);
        Object timeline = remaining.containsKey("operationTimeline")
                ? remaining.remove("operationTimeline") : remaining.remove("timeline");
        mapTimeline(timeline, builder);

        for (Map.Entry<String, Object> entry : remaining.entrySet()) {
            builder.putAdditionalData(entry.getKey(), entry.getValue());
        }
    }

    private void mapCurse(Object value, MissionBuilder builder) {
        if (!(value instanceof Map<?, ?> raw)) return;
        builder.curse(
                stringValue(raw.get("name")),
                ParserUtils.threat(stringValue(raw.get("threatLevel")))
        );
    }

    private void mapSorcerers(Object value, MissionBuilder builder) {
        if (!(value instanceof List<?> list)) return;
        for (Object item : list) {
            if (item instanceof Map<?, ?> sorcerer) {
                builder.addSorcerer(
                        stringValue(sorcerer.get("name")),
                        ParserUtils.rank(stringValue(sorcerer.get("rank")))
                );
            }
        }
    }

    private void mapTechniques(Object value, MissionBuilder builder) {
        if (!(value instanceof List<?> list)) return;
        for (Object item : list) {
            if (item instanceof Map<?, ?> technique) {
                builder.addTechnique(
                        stringValue(technique.get("name")),
                        ParserUtils.techniqueType(stringValue(technique.get("type"))),
                        stringValue(technique.get("owner")),
                        numberOrZero(technique.get("damage"))
                );
            }
        }
    }

    private void mapEconomicAssessment(Object value, MissionBuilder builder) {
        if (!(value instanceof Map<?, ?> assessment)) return;
        EconomicAssessment block = builder.economicAssessment();
        block.setTotalDamageCost(longOrNull(assessment.get("totalDamageCost")));
        block.setInfrastructureDamage(longOrNull(assessment.get("infrastructureDamage")));
        block.setTransportDamage(longOrNull(assessment.get("transportDamage")));
        block.setCommercialDamage(longOrNull(assessment.get("commercialDamage")));
        block.setRecoveryEstimateDays(longOrNull(assessment.get("recoveryEstimateDays")));
        block.setInsuranceCovered(boolOrNull(assessment.get("insuranceCovered")));
    }

    private void mapCivilianImpact(Object value, MissionBuilder builder) {
        if (!(value instanceof Map<?, ?> impact)) return;
        CivilianImpact block = builder.civilianImpact();
        block.setEvacuated(longOrNull(impact.get("evacuated")));
        block.setInjured(longOrNull(impact.get("injured")));
        block.setMissing(longOrNull(impact.get("missing")));
        if (impact.get("publicExposureRisk") != null) {
            block.setPublicExposureRisk(stringValue(impact.get("publicExposureRisk")));
        }
    }

    private void mapEnemyActivity(Object value, MissionBuilder builder) {
        if (!(value instanceof Map<?, ?> activity)) return;
        EnemyActivity block = builder.enemyActivity();
        if (activity.get("behaviorType") != null) block.setBehaviorType(stringValue(activity.get("behaviorType")));
        if (activity.get("mobility") != null) block.setMobility(stringValue(activity.get("mobility")));
        if (activity.get("escalationRisk") != null) block.setEscalationRisk(stringValue(activity.get("escalationRisk")));
        block.getTargetPriorities().addAll(ParserUtils.asStringList(activity.get("targetPriority")));
        block.getAttackPatterns().addAll(ParserUtils.asStringList(activity.get("attackPatterns")));
        block.getCountermeasuresUsed().addAll(ParserUtils.asStringList(activity.get("countermeasuresUsed")));
    }

    private void mapEnvironment(Object value, MissionBuilder builder) {
        if (!(value instanceof Map<?, ?> environment)) return;
        EnvironmentConditions block = builder.environmentConditions();
        if (environment.get("weather") != null) block.setWeather(stringValue(environment.get("weather")));
        if (environment.get("timeOfDay") != null) block.setTimeOfDay(stringValue(environment.get("timeOfDay")));
        if (environment.get("visibility") != null) block.setVisibility(stringValue(environment.get("visibility")));
        if (environment.get("cursedEnergyDensity") != null) {
            block.setCursedEnergyDensity(intOrNull(environment.get("cursedEnergyDensity")));
        }
    }

    private void mapTimeline(Object value, MissionBuilder builder) {
        if (!(value instanceof List<?> list)) return;
        for (Object item : list) {
            if (item instanceof Map<?, ?> event) {
                Object ts = event.get("timestamp");
                if (ts == null) continue;
                builder.addTimelineEvent(
                        ParserUtils.dateTime(String.valueOf(ts)),
                        stringValue(event.get("type")),
                        stringValue(event.get("description"))
                );
            }
        }
    }

    private String stringValue(Object value) {
        return value == null ? "" : ParserUtils.cleanScalar(String.valueOf(value));
    }

    private long numberOrZero(Object value) {
        return value == null ? 0L : ParserUtils.number(String.valueOf(value));
    }

    private Long longOrNull(Object value) {
        return value == null ? null : ParserUtils.longOrNull(String.valueOf(value));
    }

    private Integer intOrNull(Object value) {
        return value == null ? null : ParserUtils.integerOrNull(String.valueOf(value));
    }

    private Boolean boolOrNull(Object value) {
        return value == null ? null : ParserUtils.boolOrNull(String.valueOf(value));
    }
}
