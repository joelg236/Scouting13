package edu.ata.scouting;

import edu.ata.scouting.points.Points;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MatchData {

    private final Match match;
    private List<Points> points;
    private TeamMatch.RobotType robotType;
    private TeamMatch.StartingPosition startingPosition;
    private List<TeamMatch.Intake> intakes;
    private TeamMatch.ShooterType shooterType;
    private TeamMatch.MatchResult matchResult;
    private TeamMatch.AutoDiscCount autoDiscCount;
    private Map<TeamMatch.NoteType, String> notes;

    public MatchData(Match match) {
        this.match = match;
        points = new ArrayList<>();
        robotType = TeamMatch.RobotType.Unknown;
        startingPosition = TeamMatch.StartingPosition.Unknown;
        intakes = new ArrayList<>();
        shooterType = TeamMatch.ShooterType.NoShooter;
        matchResult = TeamMatch.MatchResult.Unknown;
        autoDiscCount = TeamMatch.AutoDiscCount.NoDisc;
        notes = new HashMap<>();
    }

    public MatchData(MatchData matchData) {
        this.match = matchData.match;
        points = matchData.points;
        robotType = matchData.robotType;
        startingPosition = matchData.startingPosition;
        intakes = matchData.intakes;
        shooterType = matchData.shooterType;
        matchResult = matchData.matchResult;
        autoDiscCount = matchData.autoDiscCount;
        notes = matchData.notes;
    }

    public MatchData(TeamMatch match) {
        this.match = match.getMatch();
        points = Arrays.asList(match.getPoints());
        robotType = match.getRobotType();
        startingPosition = match.getStartingPosition();
        intakes = Arrays.asList(match.getIntakes());
        shooterType = match.getShooterType();
        matchResult = match.getMatchResult();
        autoDiscCount = match.getAutoDiscCount();
        notes = new HashMap<>();
        notes.put(TeamMatch.NoteType.MatchNote, match.getMatchNotes());
        notes.put(TeamMatch.NoteType.TeamNote, match.getTeamNotes());
    }

    public void addPoints(Points p) {
        points.add(p);
    }

    public void popLastPoints() {
        points.remove(points.size() - 1);
    }

    public void setRobotType(TeamMatch.RobotType robotType) {
        this.robotType = robotType;
    }

    public void setStartingPosition(TeamMatch.StartingPosition startingPosition) {
        this.startingPosition = startingPosition;
    }

    public void addIntake(TeamMatch.Intake i) {
        if (!intakes.contains(i)) {
            intakes.add(i);
        }
    }

    public void removeIntake(TeamMatch.Intake i) {
        intakes.remove(i);
    }

    public void setShooterType(TeamMatch.ShooterType shooterType) {
        this.shooterType = shooterType;
    }

    public void setMatchResult(TeamMatch.MatchResult matchResult) {
        this.matchResult = matchResult;
    }

    public void setAutoDiscCount(TeamMatch.AutoDiscCount autoDiscCount) {
        this.autoDiscCount = autoDiscCount;
    }

    public void setNote(TeamMatch.NoteType noteType, String note) {
        notes.put(noteType, note);
    }

    public Match getMatch() {
        return match;
    }

    public List<Points> getPoints() {
        return points;
    }

    public int autoPoints() {
        int x = 0;
        for (Points p : points) {
            if (p instanceof Points.AutoPoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public int telePoints() {
        int x = 0;
        for (Points p : points) {
            if (p instanceof Points.TelePoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public int climbPoints() {
        int x = 0;
        for (Points p : points) {
            if (p instanceof Points.ClimbPoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public int fullPoints() {
        int x = 0;
        for (Points p : points) {
            x += p.getPoints();
        }
        return x;
    }

    public TeamMatch.RobotType getRobotType() {
        return robotType;
    }

    public TeamMatch.StartingPosition getStartingPosition() {
        return startingPosition;
    }

    public List<TeamMatch.Intake> getIntakes() {
        return intakes;
    }

    public TeamMatch.ShooterType getShooterType() {
        return shooterType;
    }

    public TeamMatch.MatchResult getMatchResult() {
        return matchResult;
    }

    public TeamMatch.AutoDiscCount getAutoDiscCount() {
        return autoDiscCount;
    }

    public Map<TeamMatch.NoteType, String> getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "Data for match " + match;
    }
}
