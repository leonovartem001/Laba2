package org.service;

import org.model.Mission;

public record MissionLoadResult(Mission mission, String parserFormat, String reportType, String reportText) {}
