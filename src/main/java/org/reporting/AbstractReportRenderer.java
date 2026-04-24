package org.reporting;

import org.model.CivilianImpact;
import org.model.EconomicAssessment;
import org.model.EnemyActivity;
import org.model.EnvironmentConditions;
import org.model.Mission;
import org.model.OperationTimelineEvent;
import org.model.Sorcerer;
import org.model.Technique;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractReportRenderer implements ReportRenderer {
    protected final NumberFormat numberFormat = NumberFormat.getIntegerInstance(new Locale("ru", "RU"));
    protected final DateTimeFormatter timelineFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected void appendBaseMissionData(StringBuilder sb, Mission mission) {
        line(sb, "ID миссии", mission.getMissionId());
        line(sb, "Дата", String.valueOf(mission.getDate()));
        line(sb, "Локация", mission.getLocation());
        line(sb, "Итог", String.valueOf(mission.getOutcome()));
        line(sb, "Стоимость ущерба", money(mission.getDamageCost()));
    }

    protected void appendCurse(StringBuilder sb, Mission mission) {
        if (mission.getCurse() == null) return;
        section(sb, "Проклятие");
        line(sb, "Название", mission.getCurse().getName());
        line(sb, "Уровень угрозы", String.valueOf(mission.getCurse().getThreatLevel()));
    }

    protected void appendSorcerers(StringBuilder sb, Mission mission) {
        if (mission.getSorcerers().isEmpty()) return;
        section(sb, "Участники миссии");
        for (int i = 0; i < mission.getSorcerers().size(); i++) {
            Sorcerer sorcerer = mission.getSorcerers().get(i);
            sb.append("  ").append(i + 1).append(") ").append(sorcerer.getName())
                    .append(" — ").append(sorcerer.getRank()).append('\n');
        }
    }

    protected void appendTechniques(StringBuilder sb, Mission mission) {
        if (mission.getTechniques().isEmpty()) return;
        section(sb, "Примененные техники");
        for (int i = 0; i < mission.getTechniques().size(); i++) {
            Technique technique = mission.getTechniques().get(i);
            sb.append("  ").append(i + 1).append(") ").append(technique.getName()).append('\n');
            sb.append("     тип: ").append(technique.getType())
                    .append(", владелец: ").append(technique.getOwner())
                    .append(", урон: ").append(numberFormat.format(technique.getDamage())).append('\n');
        }
        line(sb, "Суммарный урон техник", numberFormat.format(mission.getTotalTechniqueDamage()));
    }

    protected void appendEnvironment(StringBuilder sb, Mission mission) {
        EnvironmentConditions block = mission.getEnvironmentConditions();
        if (block == null) return;
        section(sb, "Условия среды");
        line(sb, "Погода", block.getWeather());
        line(sb, "Время суток", block.getTimeOfDay());
        line(sb, "Видимость", block.getVisibility());
        if (block.getCursedEnergyDensity() != null) {
            line(sb, "Плотность энергии", String.valueOf(block.getCursedEnergyDensity()));
        }
    }

    protected void appendEnemyActivity(StringBuilder sb, Mission mission) {
        EnemyActivity block = mission.getEnemyActivity();
        if (block == null) return;
        section(sb, "Активность противника");
        line(sb, "Тип поведения", block.getBehaviorType());
        if (!block.getTargetPriorities().isEmpty()) {
            list(sb, "Приоритет целей", block.getTargetPriorities());
        }
        if (!block.getAttackPatterns().isEmpty()) {
            list(sb, "Паттерны атак", block.getAttackPatterns());
        }
        line(sb, "Мобильность", block.getMobility());
        line(sb, "Риск эскалации", block.getEscalationRisk());
        if (!block.getCountermeasuresUsed().isEmpty()) {
            list(sb, "Примененные контрмеры", block.getCountermeasuresUsed());
        }
    }

    protected void appendEconomicAssessment(StringBuilder sb, Mission mission) {
        EconomicAssessment block = mission.getEconomicAssessment();
        if (block == null) return;
        section(sb, "Экономическая оценка");
        if (block.getTotalDamageCost() != null) line(sb, "Общий ущерб", money(block.getTotalDamageCost()));
        if (block.getInfrastructureDamage() != null) line(sb, "Ущерб инфраструктуре", money(block.getInfrastructureDamage()));
        if (block.getTransportDamage() != null) line(sb, "Ущерб транспорту", money(block.getTransportDamage()));
        if (block.getCommercialDamage() != null) line(sb, "Коммерческий ущерб", money(block.getCommercialDamage()));
        if (block.getRecoveryEstimateDays() != null) line(sb, "Восстановление, дней", String.valueOf(block.getRecoveryEstimateDays()));
        if (block.getInsuranceCovered() != null) line(sb, "Покрыто страхованием", String.valueOf(block.getInsuranceCovered()));
    }

    protected void appendCivilianImpact(StringBuilder sb, Mission mission) {
        CivilianImpact block = mission.getCivilianImpact();
        if (block == null) return;
        section(sb, "Влияние на гражданских");
        if (block.getEvacuated() != null) line(sb, "Эвакуировано", String.valueOf(block.getEvacuated()));
        if (block.getInjured() != null) line(sb, "Пострадавшие", String.valueOf(block.getInjured()));
        if (block.getMissing() != null) line(sb, "Пропавшие", String.valueOf(block.getMissing()));
        line(sb, "Риск раскрытия", block.getPublicExposureRisk());
    }

    protected void appendTimeline(StringBuilder sb, Mission mission) {
        if (mission.getOperationTimeline().isEmpty()) return;
        section(sb, "Хронология операции");
        for (OperationTimelineEvent event : mission.getOperationTimeline()) {
            sb.append("  - ").append(event.getTimestamp() == null ? "" : event.getTimestamp().format(timelineFormatter))
                    .append(" | ").append(event.getType())
                    .append(" | ").append(event.getDescription()).append('\n');
        }
    }

    protected void appendAdditionalData(StringBuilder sb, Mission mission) {
        if (mission.getAdditionalData().isEmpty()) return;
        section(sb, "Дополнительные данные");
        for (Map.Entry<String, Object> entry : mission.getAdditionalData().entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ")
                    .append(renderGeneric(entry.getValue())).append('\n');
        }
    }

    protected void section(StringBuilder sb, String title) {
        sb.append('\n').append(title).append(':').append('\n');
    }

    protected void line(StringBuilder sb, String label, String value) {
        if (value == null || value.isBlank()) return;
        sb.append("  ").append(label).append(": ").append(value).append('\n');
    }

    protected void list(StringBuilder sb, String label, List<String> values) {
        if (values == null || values.isEmpty()) return;
        sb.append("  ").append(label).append(':').append('\n');
        for (String value : values) {
            sb.append("    - ").append(value).append('\n');
        }
    }

    protected String money(long value) {
        return numberFormat.format(value) + " ¥";
    }

    protected String renderGeneric(Object value) {
        if (value == null) return "null";
        if (value instanceof Map<?, ?> map) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) sb.append(", ");
                sb.append(entry.getKey()).append('=').append(renderGeneric(entry.getValue()));
                first = false;
            }
            return sb.append('}').toString();
        }
        if (value instanceof List<?> list) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(renderGeneric(list.get(i)));
            }
            return sb.append(']').toString();
        }
        return String.valueOf(value);
    }
}
