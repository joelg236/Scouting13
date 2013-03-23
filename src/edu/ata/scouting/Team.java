package edu.ata.scouting;

// Immutable
public final class Team {

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
}
