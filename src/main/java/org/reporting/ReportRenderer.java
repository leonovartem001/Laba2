package org.reporting;

import org.model.Mission;

public interface ReportRenderer {
    String getName();
    String render(Mission mission);
}
