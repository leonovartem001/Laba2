package org.model;

public class Technique {
    private String name = "";
    private TechniqueType type = TechniqueType.UNKNOWN;
    private String owner = "";
    private long damage;

    public Technique() {}

    public Technique(String name, TechniqueType type, String owner, long damage) {
        this.name = name;
        this.type = type == null ? TechniqueType.UNKNOWN : type;
        this.owner = owner;
        this.damage = damage;
    }

    public String getName() { return name; }
    public TechniqueType getType() { return type; }
    public String getOwner() { return owner; }
    public long getDamage() { return damage; }
    public void setName(String name) { this.name = name == null ? "" : name; }
    public void setType(TechniqueType type) { this.type = type == null ? TechniqueType.UNKNOWN : type; }
    public void setOwner(String owner) { this.owner = owner == null ? "" : owner; }
    public void setDamage(long damage) { this.damage = damage; }
}
