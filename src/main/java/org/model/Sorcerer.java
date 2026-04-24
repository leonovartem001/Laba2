package org.model;

public class Sorcerer {
    private String name = "";
    private SorcererRank rank = SorcererRank.UNKNOWN;

    public Sorcerer() {}

    public Sorcerer(String name, SorcererRank rank) {
        this.name = name;
        this.rank = rank == null ? SorcererRank.UNKNOWN : rank;
    }

    public String getName() { return name; }
    public SorcererRank getRank() { return rank; }
    public void setName(String name) { this.name = name == null ? "" : name; }
    public void setRank(SorcererRank rank) { this.rank = rank == null ? SorcererRank.UNKNOWN : rank; }
}
