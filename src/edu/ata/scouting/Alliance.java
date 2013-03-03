package edu.ata.scouting;

import java.io.Serializable;
import java.util.Arrays;

public final class Alliance implements Serializable {

    private static final long serialVersionUID = Scouter.serialVersionUID;
    private final Team[] teams;

    public Alliance(Team team1, Team team2, Team team3) {
        this.teams = new Team[]{team1, team2, team3};
    }

    private Alliance(Team[] teams) {
        this.teams = teams;
    }

    public Team[] getTeams() {
        return teams;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Alliance(teams);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Alliance)
                ? Arrays.equals(((Alliance) obj).teams, this.teams)
                : false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Arrays.deepHashCode(this.teams);
        return hash;
    }

    @Override
    public String toString() {
        return "Alliance of " + teams[0] + ", " + teams[1] + " and " + teams[2];
    }
}