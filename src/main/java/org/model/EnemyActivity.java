package org.model;

import java.util.ArrayList;
import java.util.List;

public class EnemyActivity {
    private String behaviorType;
    private final List<String> targetPriorities = new ArrayList<>();
    private final List<String> attackPatterns = new ArrayList<>();
    private String mobility;
    private String escalationRisk;
    private final List<String> countermeasuresUsed = new ArrayList<>();

    public String getBehaviorType() { return behaviorType; }
    public List<String> getTargetPriorities() { return targetPriorities; }
    public List<String> getAttackPatterns() { return attackPatterns; }
    public String getMobility() { return mobility; }
    public String getEscalationRisk() { return escalationRisk; }
    public List<String> getCountermeasuresUsed() { return countermeasuresUsed; }

    public void setBehaviorType(String behaviorType) { this.behaviorType = behaviorType; }
    public void setMobility(String mobility) { this.mobility = mobility; }
    public void setEscalationRisk(String escalationRisk) { this.escalationRisk = escalationRisk; }
}
