package org.factory;

import org.model.*;

public interface MissionFactory {
    Mission createMission();
    Curse createCurse();
    Curse createCurse(String name, ThreatLevel threatLevel);
    Sorcerer createSorcerer();
    Sorcerer createSorcerer(String name, SorcererRank rank);
    Technique createTechnique();
    Technique createTechnique(String name, TechniqueType type, String owner, long damage);
    EconomicAssessment createEconomicAssessment();
    CivilianImpact createCivilianImpact();
    EnemyActivity createEnemyActivity();
    EnvironmentConditions createEnvironmentConditions();
    OperationTimelineEvent createTimelineEvent();
    OperationTimelineEvent createTimelineEvent(java.time.LocalDateTime timestamp, String type, String description);
    MissionBuilder newBuilder();
}
