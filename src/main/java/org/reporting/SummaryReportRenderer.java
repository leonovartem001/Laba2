package org.reporting;

import org.model.Mission;

public class SummaryReportRenderer extends AbstractReportRenderer {
    @Override
    public String getName() { return "summary"; }

    @Override
    public String render(Mission mission) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== КРАТКАЯ СВОДКА ПО МИССИИ ===\n");
        appendBaseMissionData(sb, mission);
        appendCurse(sb, mission);
        appendSorcerers(sb, mission);
        appendTechniques(sb, mission);
        appendEnvironment(sb, mission);
        appendEnemyActivity(sb, mission);
        appendEconomicAssessment(sb, mission);
        appendCivilianImpact(sb, mission);
        appendTimeline(sb, mission);
        appendAdditionalData(sb, mission);
        return sb.toString();
    }
}
