package edu.ata.scouting;

import edu.ata.scouting.points.Points;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class TeamMatch implements Serializable {

    private static final long serialVersionUID = Scouter.serialVersionUID;
    public static final RobotType OFFENSIVE = new RobotType(RobotType.OFF),
            DEFENSIVE = new RobotType(RobotType.DEF);
    public static final Win WIN = new Win(Win.win, true),
            LOSS = new Win(Win.loss, false),
            UNKNOWN = new Win(Win.unknown, false);
    private final Match match;
    private final Team team;
    private final ArrayList<Points> points = new ArrayList<>();
    private RobotType type = new RobotType("Unknown");
    private Win win = UNKNOWN;
    private String startingPosition = "Unknown";
    private String notes = "";

    public TeamMatch(int teamNumber, Match match) throws NoSuchFieldException {
        this.match = match;
        this.team = match.getTeam(teamNumber);
        if (this.team == null) {
            throw new IllegalArgumentException("TeamMatch was made in a match without the team.");
        }
    }

    public void addPoints(Points points) {
        this.points.add(points);
    }

    public void removePoints(Points points) {
        this.points.remove(points);
    }

    public void removeLastPoints() {
        if (points.size() > 0) {
            points.remove(points.size() - 1);
        }
    }

    public void setRobotType(RobotType type) {
        this.type = type;
    }

    public void setWin(Win win) {
        this.win = win;
    }

    public void setStartingPosition(String startingPosition) {
        this.startingPosition = startingPosition;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Points> getPoints() {
        return Collections.unmodifiableList(points);
    }

    public RobotType getRobotType() {
        return type;
    }

    public Win getWin() {
        return win;
    }

    public String getStartingPosition() {
        return startingPosition;
    }

    public String getNotes() {
        return notes;
    }

    public Match getMatch() {
        return match;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            return new TeamMatch(team.getTeamNumber(), match);
        } catch (NoSuchFieldException ex) {
            throw new CloneNotSupportedException(ex.getMessage());
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TeamMatch)
                ? ((TeamMatch) obj).match.equals(this.match)
                && ((TeamMatch) obj).team.equals(this.team)
                : false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.match);
        hash = 83 * hash + Objects.hashCode(this.team);
        return hash;
    }

    @Override
    public String toString() {
        return team + " in " + match;
    }

    public static final class RobotType implements Serializable {

        private static final long serialVersionUID = Scouter.serialVersionUID;
        private static final String OFF = "Offensive", DEF = "Defensive";
        private final String type;

        private RobotType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public static final class Win implements Serializable {

        private static final long serialVersionUID = Scouter.serialVersionUID;
        private static final String win = "Win", loss = "Loss", unknown = "Unknown";
        private final String string;
        private final boolean isWin;

        private Win(String string, boolean win) {
            this.string = string;
            this.isWin = win;
        }

        public boolean isWin() {
            return isWin;
        }

        @Override
        public String toString() {
            return string;
        }
    }
}