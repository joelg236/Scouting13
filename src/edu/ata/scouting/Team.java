package edu.ata.scouting;

// Immutable
import java.io.Serializable;

public final class Team implements Serializable {

    private static final long serialVersionUID = Scouter.serialVersionUID;
    public static final String NO_NAME = "No Name";
    private final int teamNumber;
    private final String teamName;

    public Team(int teamNumber) {
        this(teamNumber, NO_NAME);
    }

    public Team(int teamNumber, String teamName) {
        this.teamNumber = teamNumber;
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Team) {
            return teamNumber == ((Team) obj).teamNumber;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.teamNumber;
        return hash;
    }

    @Override
    public String toString() {
        if (teamName.equals(NO_NAME)) {
            return "Team " + teamNumber;
        } else {
            return "Team " + teamNumber + " - " + teamName;
        }
    }
}
