package org.model;

public class Curse {
    private String name = "";
    private ThreatLevel threatLevel = ThreatLevel.UNKNOWN;

    public Curse() {}

    public Curse(String name, ThreatLevel threatLevel) {
        this.name = name;
        this.threatLevel = threatLevel == null ? ThreatLevel.UNKNOWN : threatLevel;
    }

    public String getName() { return name; }
    public ThreatLevel getThreatLevel() { return threatLevel; }
    public void setName(String name) { this.name = name == null ? "" : name; }
    public void setThreatLevel(ThreatLevel threatLevel) { this.threatLevel = threatLevel == null ? ThreatLevel.UNKNOWN : threatLevel; }
}
