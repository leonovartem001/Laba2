package org.model;

public class EconomicAssessment {
    private Long totalDamageCost;
    private Long infrastructureDamage;
    private Long transportDamage;
    private Long commercialDamage;
    private Long recoveryEstimateDays;
    private Boolean insuranceCovered;

    public Long getTotalDamageCost() { return totalDamageCost; }
    public Long getInfrastructureDamage() { return infrastructureDamage; }
    public Long getTransportDamage() { return transportDamage; }
    public Long getCommercialDamage() { return commercialDamage; }
    public Long getRecoveryEstimateDays() { return recoveryEstimateDays; }
    public Boolean getInsuranceCovered() { return insuranceCovered; }

    public void setTotalDamageCost(Long totalDamageCost) { this.totalDamageCost = totalDamageCost; }
    public void setInfrastructureDamage(Long infrastructureDamage) { this.infrastructureDamage = infrastructureDamage; }
    public void setTransportDamage(Long transportDamage) { this.transportDamage = transportDamage; }
    public void setCommercialDamage(Long commercialDamage) { this.commercialDamage = commercialDamage; }
    public void setRecoveryEstimateDays(Long recoveryEstimateDays) { this.recoveryEstimateDays = recoveryEstimateDays; }
    public void setInsuranceCovered(Boolean insuranceCovered) { this.insuranceCovered = insuranceCovered; }
}
