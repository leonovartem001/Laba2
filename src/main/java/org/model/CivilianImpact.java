package org.model;

public class CivilianImpact {
    private Long evacuated;
    private Long injured;
    private Long missing;
    private String publicExposureRisk;

    public Long getEvacuated() { return evacuated; }
    public Long getInjured() { return injured; }
    public Long getMissing() { return missing; }
    public String getPublicExposureRisk() { return publicExposureRisk; }

    public void setEvacuated(Long evacuated) { this.evacuated = evacuated; }
    public void setInjured(Long injured) { this.injured = injured; }
    public void setMissing(Long missing) { this.missing = missing; }
    public void setPublicExposureRisk(String publicExposureRisk) { this.publicExposureRisk = publicExposureRisk; }
}
