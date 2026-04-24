package org.factory;

import org.model.*;

public class DefaultMissionFactory implements MissionFactory {
    @Override
    public Mission createMission() { return new Mission(); }
    @Override
    public Curse createCurse() { return new Curse(); }
    @Override
    public Curse createCurse(String name, ThreatLevel threatLevel) { return new Curse(name, threatLevel); }
    @Override
    public Sorcerer createSorcerer() { return new Sorcerer(); }
    @Override
    public Sorcerer createSorcerer(String name, SorcererRank rank) { return new Sorcerer(name, rank); }
    @Override
    public Technique createTechnique() { return new Technique(); }
    @Override
    public Technique createTechnique(String name, TechniqueType type, String owner, long damage) {
        return new Technique(name, type, owner, damage);
    }
    @Override
    public EconomicAssessment createEconomicAssessment() { return new EconomicAssessment(); }
    @Override
    public CivilianImpact createCivilianImpact() { return new CivilianImpact(); }
    @Override
    public EnemyActivity createEnemyActivity() { return new EnemyActivity(); }
    @Override
    public EnvironmentConditions createEnvironmentConditions() { return new EnvironmentConditions(); }
    @Override
    public OperationTimelineEvent createTimelineEvent() { return new OperationTimelineEvent(); }
    @Override
    public OperationTimelineEvent createTimelineEvent(java.time.LocalDateTime timestamp, String type, String description) {
        return new OperationTimelineEvent(timestamp, type, description);
    }
    @Override
    public MissionBuilder newBuilder() { return new MissionBuilder(this); }
}
