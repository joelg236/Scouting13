package edu.ata.scouting;

// Immutable
import edu.ata.scouting.points.Points;
import java.io.Serializable;

public final class TeamMatch implements Serializable, Comparable<TeamMatch> {

    private static final long serialVersionUID = Scouter.serialVersionUID;
    private final Team team;
    private final Match match;
    private final Points[] points;
    private final RobotType robotType;
    private final StartingPosition startingPosition;
    private final Intake[] intakes;
    private final ShooterType shooterType;
    private final MatchResult matchResult;
    private final AutoDiscCount autoDiscCount;
    private final String matchNote;
    private final String teamNote;

    public TeamMatch(Team team, MatchData matchData) {
        this(team,
                matchData.getMatch(),
                matchData.getPoints().toArray(new Points[matchData.getPoints().size()]),
                matchData.getRobotType(),
                matchData.getStartingPosition(),
                matchData.getIntakes().toArray(new Intake[matchData.getIntakes().size()]),
                matchData.getShooterType(),
                matchData.getMatchResult(),
                matchData.getAutoDiscCount(),
                matchData.getNotes().get(NoteType.MatchNote),
                matchData.getNotes().get(NoteType.TeamNote));
    }

    public TeamMatch(Team team, Match match, Points[] points, RobotType robotType,
            StartingPosition startingPosition, Intake[] intakes, ShooterType shooterType,
            MatchResult matchResult, AutoDiscCount autoDiscCount, String matchNote, String teamNote) {
        if (team == null || match == null || points == null || robotType == null
                || startingPosition == null || intakes == null || shooterType == null
                || matchResult == null || autoDiscCount == null) {
            throw new NullPointerException();
        }
        if(matchNote == null) {
            matchNote = "";
        }
        if(teamNote == null) {
            teamNote = "";
        }
        this.team = team;
        this.match = match;
        this.points = points;
        this.robotType = robotType;
        this.startingPosition = startingPosition;
        this.intakes = intakes;
        this.shooterType = shooterType;
        this.matchResult = matchResult;
        this.autoDiscCount = autoDiscCount;
        this.matchNote = matchNote;
        this.teamNote = teamNote;
    }

    public Team getTeam() {
        return team;
    }

    public Match getMatch() {
        return match;
    }

    public int getScore() {
        int score = 0;
        for (Points p : points) {
            score += p.getPoints();
        }
        return score;
    }

    public Points[] getPoints() {
        return points;
    }

    public RobotType getRobotType() {
        return robotType;
    }

    public StartingPosition getStartingPosition() {
        return startingPosition;
    }

    public Intake[] getIntakes() {
        return intakes;
    }

    public ShooterType getShooterType() {
        return shooterType;
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }

    public AutoDiscCount getAutoDiscCount() {
        return autoDiscCount;
    }

    public String getMatchNotes() {
        return matchNote;
    }

    public String getTeamNotes() {
        return teamNote;
    }

    @Override
    public int compareTo(TeamMatch o) {
        return match.compareTo(o.match);
    }

    @Override
    public String toString() {
        return team + " in match " + match;
    }

    public static enum RobotType {

        Unknown, Offensive, Defensive
    }

    public static enum StartingPosition {

        Unknown,
        FrontLeft, FrontMiddle, FrontRight,
        Left, Middle, Right,
        BackLeft, BackMiddle, BackRight
    }

    public static enum Intake {

        FeederStation, GroundPickup
    }

    public static enum ShooterType {

        NoShooter, Linear, Curved
    }

    public static enum MatchResult {

        Unknown, Win, Loss
    }

    public static enum AutoDiscCount {

        NoDisc, TwoDisc, ThreeDisc, FiveDisc, SevenDisc, NineDisc
    }

    public static enum NoteType {

        MatchNote, TeamNote
    }
}
