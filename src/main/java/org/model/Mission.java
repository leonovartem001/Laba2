package org.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Mission {
    private String missionId;
    private LocalDate date;
    private String location;
    private MissionOutcome outcome = MissionOutcome.UNKNOWN;
    private long damageCost;
    private Curse curse = new Curse();
    private final List<Sorcerer> sorcerers = new ArrayList<>();
    private final List<Technique> techniques = new ArrayList<>();
    private EconomicAssessment economicAssessment;
    private CivilianImpact civilianImpact;
    private EnemyActivity enemyActivity;
    private EnvironmentConditions environmentConditions;
    private final List<OperationTimelineEvent> operationTimeline = new ArrayList<>();
    private final Map<String, Object> additionalData = new LinkedHashMap<>();

    public String getMissionId() { return missionId; }
    public LocalDate getDate() { return date; }
    public String getLocation() { return location; }
    public MissionOutcome getOutcome() { return outcome; }
    public long getDamageCost() { return damageCost; }
    public Curse getCurse() { return curse; }
    public List<Sorcerer> getSorcerers() { return sorcerers; }
    public List<Technique> getTechniques() { return techniques; }
    public EconomicAssessment getEconomicAssessment() { return economicAssessment; }
    public CivilianImpact getCivilianImpact() { return civilianImpact; }
    public EnemyActivity getEnemyActivity() { return enemyActivity; }
    public EnvironmentConditions getEnvironmentConditions() { return environmentConditions; }
    public List<OperationTimelineEvent> getOperationTimeline() { return operationTimeline; }
    public Map<String, Object> getAdditionalData() { return additionalData; }

    public void setMissionId(String missionId) { this.missionId = missionId; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setLocation(String location) { this.location = location; }
    public void setOutcome(MissionOutcome outcome) { this.outcome = outcome == null ? MissionOutcome.UNKNOWN : outcome; }
    public void setDamageCost(long damageCost) { this.damageCost = damageCost; }
    public void setCurse(Curse curse) { this.curse = curse == null ? new Curse() : curse; }
    public void setEconomicAssessment(EconomicAssessment economicAssessment) { this.economicAssessment = economicAssessment; }
    public void setCivilianImpact(CivilianImpact civilianImpact) { this.civilianImpact = civilianImpact; }
    public void setEnemyActivity(EnemyActivity enemyActivity) { this.enemyActivity = enemyActivity; }
    public void setEnvironmentConditions(EnvironmentConditions environmentConditions) { this.environmentConditions = environmentConditions; }

    public long getTotalTechniqueDamage() {
        return techniques.stream().mapToLong(Technique::getDamage).sum();
    }
}
