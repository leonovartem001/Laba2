package org.model;

public class EnvironmentConditions {
    private String weather;
    private String timeOfDay;
    private String visibility;
    private Integer cursedEnergyDensity;

    public String getWeather() { return weather; }
    public String getTimeOfDay() { return timeOfDay; }
    public String getVisibility() { return visibility; }
    public Integer getCursedEnergyDensity() { return cursedEnergyDensity; }

    public void setWeather(String weather) { this.weather = weather; }
    public void setTimeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
    public void setCursedEnergyDensity(Integer cursedEnergyDensity) { this.cursedEnergyDensity = cursedEnergyDensity; }
}
