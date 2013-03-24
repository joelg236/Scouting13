package edu.ata.scouting;

import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;

public final class ScoutView extends JDialog {

    private final MatchData match;
    private final Team team;
    private final JLabel autoScore = new JLabel();
    private final JLabel teleopScore = new JLabel();
    private final JLabel climbScore = new JLabel();
    private final JLabel totalScore = new JLabel();
    private final JLabel autoLabel = new JLabel("Auto");
    private final JLabel teleLabel = new JLabel("Teleop");
    private final JLabel climbLabel = new JLabel("Climb");
    private final JLabel climbTimerLabel = new JLabel("Climb Timer");
    private final JButton twoAuto = new JButton("2");
    private final JButton oneTele = new JButton("1");
    private final JButton startClimb = new JButton("Start");
    private final JLabel climbTime = new JLabel("0");
    private final JButton fourAuto = new JButton("4");
    private final JButton twoTele = new JButton("2");
    private final JButton tenClimb = new JButton("10");
    private final JLabel robotTypeLabel = new JLabel("Robot Type");
    private final JButton sixAuto = new JButton("6");
    private final JButton threeTele = new JButton("3");
    private final JButton twentyClimb = new JButton("10");
    private final JComboBox<TeamMatch.RobotType> robotType = new JComboBox<>(TeamMatch.RobotType.values());
    private final StartingPositions startingPositions = new StartingPositions();
    private final JButton fiveTele = new JButton("5");
    private final JButton thirtyClimb = new JButton("30");
    private final JLabel matchNotesLabel = new JLabel("Match Notes");
    private final JLabel autoDiscsLabel = new JLabel("Auto Discs");
    private final JLabel shooterTypeLabel = new JLabel("Shooter Type");
    private final JLabel intake = new JLabel("Intake");
    private final JTextField matchNotes = new JTextField();
    private final JComboBox<TeamMatch.AutoDiscCount> autoDiscCount = new JComboBox<>(TeamMatch.AutoDiscCount.values());
    private final JComboBox<TeamMatch.ShooterType> shooterType = new JComboBox<>(TeamMatch.ShooterType.values());
    private final IntakeSelection intakeSelection = new IntakeSelection();
    private final JLabel teamNotesLabel = new JLabel("Team Notes");
    private final JButton win = new JButton("Win");
    private final JButton lose = new JButton("Lose");
    private final JButton discard = new JButton("Discard");
    private final JTextField teamNotes = new JTextField();

    public ScoutView(Team team, MatchData match) {
        super(Scouter.getMain(), team + " " + match);
        this.team = team;
        this.match = match;
        setRootPane(new JRootPane());
        setRootPaneCheckingEnabled(true);
        setLayout(new GridLayout(9, 4));

        updateScore();
        robotType.setSelectedItem(match.getRobotType());
        startingPositions.setPosition(match.getStartingPosition());
        matchNotes.setText(match.getNotes().get(TeamMatch.NoteType.MatchNote));
        autoDiscCount.setSelectedItem(match.getAutoDiscCount());
        shooterType.setSelectedItem(TeamMatch.ShooterType.NoShooter);
        intakeSelection.setFeeder(match.getIntakes().contains(TeamMatch.Intake.FeederStation));
        intakeSelection.setGround(match.getIntakes().contains(TeamMatch.Intake.GroundPickup));
        teamNotes.setText(match.getNotes().get(TeamMatch.NoteType.TeamNote));

        add(autoScore);
        add(teleopScore);
        add(climbScore);
        add(totalScore);
        add(autoLabel);
        add(teleLabel);
        add(climbLabel);
        add(climbTimerLabel);
        add(twoAuto);
        add(oneTele);
        add(startClimb);
        add(climbTime);
        add(fourAuto);
        add(twoTele);
        add(tenClimb);
        add(robotTypeLabel);
        add(sixAuto);
        add(threeTele);
        add(twentyClimb);
        add(robotType);
        add(startingPositions);
        add(fiveTele);
        add(thirtyClimb);
        add(matchNotesLabel);
        add(autoDiscsLabel);
        add(shooterTypeLabel);
        add(intake);
        add(matchNotes);
        add(autoDiscCount);
        add(shooterType);
        add(intakeSelection);
        add(teamNotesLabel);
        add(win);
        add(lose);
        add(discard);
        add(teamNotes);

        setAlwaysOnTop(true);
        setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        setLocationRelativeTo(null);
    }

    private void saveMatch() {
        match.setRobotType(robotType.getItemAt(robotType.getSelectedIndex()));
        match.setStartingPosition(startingPositions.position);
        match.setNote(TeamMatch.NoteType.MatchNote, matchNotes.getText());
        match.setAutoDiscCount(autoDiscCount.getItemAt(autoDiscCount.getSelectedIndex()));
        match.setShooterType(shooterType.getItemAt(shooterType.getSelectedIndex()));
        if (intakeSelection.feeder) {
            match.addIntake(TeamMatch.Intake.FeederStation);
        }
        if (intakeSelection.ground) {
            match.addIntake(TeamMatch.Intake.GroundPickup);
        }
        match.setNote(TeamMatch.NoteType.TeamNote, teamNotes.getText());

        Scouter.getMain().putMatch(team, match);
    }

    private void updateScore() {
        autoScore.setText(String.valueOf(match.autoPoints()));
        teleopScore.setText(String.valueOf(match.telePoints()));
        climbScore.setText(String.valueOf(match.climbPoints()));
        totalScore.setText(String.valueOf(match.fullPoints()));
    }

    private class StartingPositions extends JPanel {

        private TeamMatch.StartingPosition position;

        public void setPosition(TeamMatch.StartingPosition position) {
            this.position = position;
        }
    }

    private class IntakeSelection extends JPanel {

        private boolean ground, feeder;

        public void setGround(boolean yes) {
            ground = yes;
        }

        public void setFeeder(boolean yes) {
            feeder = yes;
        }
    }
}
