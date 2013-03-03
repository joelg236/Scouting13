package edu.ata.scouting;

import java.io.Serializable;

public final class Team implements Serializable {

    private static final long serialVersionUID = Scouter.serialVersionUID;
    private final int teamNumber;

    public Team(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public Team teamNumber(int teamNumber) {
        return new Team(teamNumber);
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Team(teamNumber);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Team)
                ? ((Team) obj).teamNumber == this.teamNumber
                : false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.teamNumber;
        return hash;
    }

    @Override
    public String toString() {
        return "Team " + teamNumber;
    }
}