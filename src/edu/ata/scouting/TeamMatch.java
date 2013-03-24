package edu.ata.scouting;

// Immutable
import edu.ata.scouting.points.Points;
import java.io.Serializable;
import java.util.Map;

public final class TeamMatch implements Serializable {

    private static final long serialVersionUID = Scouter.serialVersionUID;
    private final Team team;
    private final Points[] points;
    private final RobotType robotType;
    private final StartingPosition startingPosition;
    private final Intake[] intakes;
    private final ShooterType shooterType;
    private final MatchResult matchResult;
    private final AutoDiscCount autoDiscCount;
    private final Map<NoteType, String> notes;

    public TeamMatch(Team team, MatchData matchData) {
        this(team,
                matchData.getPoints().toArray(new Points[matchData.getPoints().size()]),
                matchData.getRobotType(),
                matchData.getStartingPosition(),
                matchData.getIntakes().toArray(new Intake[matchData.getIntakes().size()]),
                matchData.getShooterType(),
                matchData.getMatchResult(),
                matchData.getAutoDiscCount(),
                matchData.getNotes());
    }

    public TeamMatch(Team team, Points[] points, RobotType robotType,
            StartingPosition startingPosition, Intake[] intakes, ShooterType shooterType,
            MatchResult matchResult, AutoDiscCount autoDiscCount, Map<NoteType, String> notes) {
        if (team == null || points == null || robotType == null || startingPosition == null
                || intakes == null || shooterType == null || matchResult == null
                || autoDiscCount == null || notes == null) {
            throw new NullPointerException();
        }
        this.team = team;
        this.points = points;
        this.robotType = robotType;
        this.startingPosition = startingPosition;
        this.intakes = intakes;
        this.shooterType = shooterType;
        this.matchResult = matchResult;
        this.autoDiscCount = autoDiscCount;
        this.notes = notes;
    }

    public Team getTeam() {
        return team;
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

    public Map<NoteType, String> getNotes() {
        return notes;
    }

    public String getMatchNotes() {
        return notes.get(NoteType.MatchNote);
    }

    public String getTeamNotes() {
        return notes.get(NoteType.TeamNote);
    }

    public static enum RobotType {

        Unknown, Offensive, Defensive
    }

    public static enum StartingPosition {

        Unknown,
        FrontLeft, Front, FrontRight,
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
