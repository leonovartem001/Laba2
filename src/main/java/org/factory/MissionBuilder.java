package org.factory;

import org.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class MissionBuilder {
    private final MissionFactory factory;
    private final Mission mission;

    public MissionBuilder(MissionFactory factory) {
        this.factory = factory;
        this.mission = factory.createMission();
        this.mission.setCurse(factory.createCurse());
    }

    public MissionBuilder missionId(String missionId) {
        mission.setMissionId(missionId);
        return this;
    }

    public MissionBuilder date(LocalDate date) {
        mission.setDate(date);
        return this;
    }

    public MissionBuilder location(String location) {
        mission.setLocation(location);
        return this;
    }

    public MissionBuilder outcome(MissionOutcome outcome) {
        mission.setOutcome(outcome);
        return this;
    }

    public MissionBuilder damageCost(long damageCost) {
        mission.setDamageCost(damageCost);
        return this;
    }

    public MissionBuilder curse(String name, ThreatLevel threatLevel) {
        mission.setCurse(factory.createCurse(name, threatLevel));
        return this;
    }

    public MissionBuilder curseName(String name) {
        ensureCurse().setName(name);
        return this;
    }

    public MissionBuilder curseThreatLevel(ThreatLevel threatLevel) {
        ensureCurse().setThreatLevel(threatLevel);
        return this;
    }

    public MissionBuilder addSorcerer(String name, SorcererRank rank) {
        mission.getSorcerers().add(factory.createSorcerer(name, rank));
        return this;
    }

    public MissionBuilder addTechnique(String name, TechniqueType type, String owner, long damage) {
        mission.getTechniques().add(factory.createTechnique(name, type, owner, damage));
        return this;
    }

    public EconomicAssessment economicAssessment() {
        if (mission.getEconomicAssessment() == null) {
            mission.setEconomicAssessment(factory.createEconomicAssessment());
        }
        return mission.getEconomicAssessment();
    }

    public CivilianImpact civilianImpact() {
        if (mission.getCivilianImpact() == null) {
            mission.setCivilianImpact(factory.createCivilianImpact());
        }
        return mission.getCivilianImpact();
    }

    public EnemyActivity enemyActivity() {
        if (mission.getEnemyActivity() == null) {
            mission.setEnemyActivity(factory.createEnemyActivity());
        }
        return mission.getEnemyActivity();
    }

    public EnvironmentConditions environmentConditions() {
        if (mission.getEnvironmentConditions() == null) {
            mission.setEnvironmentConditions(factory.createEnvironmentConditions());
        }
        return mission.getEnvironmentConditions();
    }

    public MissionBuilder addTimelineEvent(LocalDateTime timestamp, String type, String description) {
        mission.getOperationTimeline().add(factory.createTimelineEvent(timestamp, type, description));
        return this;
    }

    public MissionBuilder putAdditionalData(String key, Object value) {
        if (key != null && !key.isBlank() && value != null) {
            mission.getAdditionalData().put(key, value);
        }
        return this;
    }

    public Mission build() {
        validateRequiredFields();
        if (mission.getDamageCost() == 0 && mission.getEconomicAssessment() != null
                && mission.getEconomicAssessment().getTotalDamageCost() != null) {
            mission.setDamageCost(mission.getEconomicAssessment().getTotalDamageCost());
        }
        return mission;
    }

    private Curse ensureCurse() {
        if (mission.getCurse() == null) {
            mission.setCurse(factory.createCurse());
        }
        return mission.getCurse();
    }

    private void validateRequiredFields() {
        if (isBlank(mission.getMissionId())) throw new IllegalArgumentException("Не заполнен missionId");
        if (mission.getDate() == null) throw new IllegalArgumentException("Не заполнена дата миссии");
        if (isBlank(mission.getLocation())) throw new IllegalArgumentException("Не заполнена локация миссии");
        if (mission.getOutcome() == null) throw new IllegalArgumentException("Не заполнен outcome");
        if (mission.getCurse() == null || isBlank(mission.getCurse().getName())) {
            throw new IllegalArgumentException("Не заполнен блок curse.name");
        }
        if (mission.getCurse().getThreatLevel() == null) {
            throw new IllegalArgumentException("Не заполнен блок curse.threatLevel");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
