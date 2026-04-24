package org.reporting;

import org.model.Mission;

public class DetailedReportRenderer extends AbstractReportRenderer {
    @Override
    public String getName() { return "detailed"; }

    @Override
    public String render(Mission mission) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== ДЕТАЛИЗИРОВАННЫЙ ОТЧЕТ ПО МИССИИ ===\n");
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
        sb.append("\nИтоговый вывод:\n");
        sb.append("  Миссия ").append(mission.getMissionId()).append(" завершилась со статусом ")
                .append(mission.getOutcome()).append(". ")
                .append("Зафиксировано участников: ").append(mission.getSorcerers().size())
                .append(", техник: ").append(mission.getTechniques().size()).append(".\n");
        return sb.toString();
    }
}
