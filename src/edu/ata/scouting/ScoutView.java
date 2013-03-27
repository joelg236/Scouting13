package edu.ata.scouting;

import edu.ata.scouting.decompiling.Decompiler;
import edu.ata.scouting.points.Points;
import edu.ata.scouting.points.auto.FourPointAuto;
import edu.ata.scouting.points.auto.SixPointAuto;
import edu.ata.scouting.points.auto.TwoPointAuto;
import edu.ata.scouting.points.climb.TenPointClimb;
import edu.ata.scouting.points.climb.ThirtyPointClimb;
import edu.ata.scouting.points.climb.TwentyPointClimb;
import edu.ata.scouting.points.foul.ThreePointFoul;
import edu.ata.scouting.points.foul.TwentyPointFoul;
import edu.ata.scouting.points.tele.FivePointTele;
import edu.ata.scouting.points.tele.OnePointTele;
import edu.ata.scouting.points.tele.ThreePointTele;
import edu.ata.scouting.points.tele.TwoPointTele;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public final class ScoutView extends JDialog {

    private static final Font buttonFont = new Font("Default", Font.BOLD, 18);
    private final MatchData match;
    private final Team team;
    private final ScoreDisplay scoreDisplay;
    private JTextArea matchNotes;
    private JTextArea teamNotes;
    private JCheckBox ground;
    private JCheckBox feeder;
    private double climbTime = 0;

    public ScoutView(Team team, MatchData match) {
        super(Scouter.getMain(), match.toString());
        this.match = match;
        this.team = team;
        this.matchNotes = new JTextArea(match.getNotes().get(TeamMatch.NoteType.MatchNote));
        this.teamNotes = new JTextArea(match.getNotes().get(TeamMatch.NoteType.TeamNote));
        this.ground = new JCheckBox("Ground Pickup", match.getIntakes().contains(TeamMatch.Intake.GroundPickup));
        this.feeder = new JCheckBox("Feeder Station", match.getIntakes().contains(TeamMatch.Intake.FeederStation));
        setRootPane(new JRootPane());
        setRootPaneCheckingEnabled(true);
        setLayout(LayoutFactory.createLayout());

        JLabel title = new JLabel(match.toString());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 36));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        LayoutFactory factory = LayoutFactory.newFactory();
        add(title, factory.setWidth(2).setWeightY(0).setInsets(new Insets(5, 0, 15, 0)));
        add((scoreDisplay = new ScoreDisplay()), factory.setY(1).setWeightX(0).setInsets(new Insets(5, 5, 5, 5)));
        add(new AutonomousDisplay(), factory.setY(2).setWidth(1).setWeightY(1).setWeightX(1));
        add(new TeleopDisplay(), factory.setX(1));
        add(new StartDisplay(), factory.setY(3).setHeight(2).setX(0));
        add(new ClimbDisplay(), factory.setX(1).setHeight(1));
        add(new FoulDisplay(), factory.setY(4));
        add(new RobotNotes(), factory.setX(0).setY(5));
        add(new WinLossDisplay(), factory.setX(1).setWidth(2));
        add(new ControlDisplay(), factory.setX(0).setY(6));

        setAlwaysOnTop(true);
        setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        setLocationRelativeTo(Scouter.getMain());
    }

    private void addScore(Points points) {
        match.addPoints(points);
        scoreDisplay.update();
    }

    private final class ScoreDisplay extends JPanel {

        private final JLabel finalScore, autoScore, teleScore, climbScore, foulScore;

        public ScoreDisplay() {
            this.finalScore = new JLabel("Final Score : " + Decompiler.total(match));
            this.autoScore = new JLabel("Auto Score : " + Decompiler.auto(match));
            this.teleScore = new JLabel("Teleop Score : " + Decompiler.tele(match));
            this.climbScore = new JLabel("Climb Score : " + Decompiler.climb(match));
            this.foulScore = new JLabel("Foul Points : " + Decompiler.foul(match));
            Font f = finalScore.getFont().deriveFont(Font.BOLD, 24);
            this.finalScore.setFont(f);
            this.finalScore.setHorizontalAlignment(SwingConstants.CENTER);
            this.autoScore.setFont(f);
            this.autoScore.setHorizontalAlignment(SwingConstants.CENTER);
            this.teleScore.setFont(f);
            this.teleScore.setHorizontalAlignment(SwingConstants.CENTER);
            this.climbScore.setFont(f);
            this.climbScore.setHorizontalAlignment(SwingConstants.CENTER);
            this.foulScore.setFont(f);
            this.foulScore.setHorizontalAlignment(SwingConstants.CENTER);

            setRootPaneCheckingEnabled(true);
            setLayout(new GridLayout(1, 0));
            add(finalScore);
            add(autoScore);
            add(teleScore);
            add(climbScore);
            add(foulScore);
        }

        private void update() {
            finalScore.setText("Final Score : " + Decompiler.total(match));
            autoScore.setText("Auto Score : " + Decompiler.auto(match));
            teleScore.setText("Teleop Score : " + Decompiler.tele(match));
            climbScore.setText("Climb Score : " + Decompiler.climb(match));
            foulScore.setText("Foul Points : " + Decompiler.foul(match));
        }
    }

    private final class AutonomousDisplay extends JPanel {

        private final Color autoColor = Color.YELLOW;

        public AutonomousDisplay() {
            super(new GridLayout(2, 2, 5, 5));
            JLabel title = new JLabel("Autonomous");
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            add(title);
            add(new AutoPoint(2));
            add(new AutoPoint(4));
            add(new AutoPoint(6));
        }

        private final class AutoPoint extends JButton {

            private final int points;

            public AutoPoint(int points) {
                super("+" + points);
                this.points = points;
                setBackground(autoColor);
                setFont(buttonFont);
                addActionListener(new AddPoints());
            }

            private final class AddPoints implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Points p;
                    switch (points) {
                        case (6):
                            p = new SixPointAuto();
                            break;
                        case (4):
                            p = new FourPointAuto();
                            break;
                        case (2):
                            p = new TwoPointAuto();
                            break;
                        default:
                            p = new SixPointAuto();
                    }
                    addScore(p);
                }
            }
        }
    }

    private final class TeleopDisplay extends JPanel {

        private final Color teleColor = Color.CYAN;

        public TeleopDisplay() {
            super(LayoutFactory.createLayout());
            JLabel title = new JLabel("Teleoperated (Shooting Points)");
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            JButton onePoint = new TelePoint(1);
            JButton twoPoint = new TelePoint(2);
            JButton threePoint = new TelePoint(3);
            JButton fivePoint = new TelePoint(5);

            LayoutFactory factory = LayoutFactory.newFactory().setInsets(new Insets(5, 5, 5, 5));
            add(title, factory.setWidth(4));
            add(onePoint, factory.setY(1).setWidth(1));
            add(twoPoint, factory.setX(1));
            add(threePoint, factory.setX(2));
            add(fivePoint, factory.setX(3));
        }

        private final class TelePoint extends JButton {

            private final int points;

            public TelePoint(int points) {
                super("+" + points);
                this.points = points;
                setBackground(teleColor);
                setFont(buttonFont);
                addActionListener(new AddPoints());
            }

            private final class AddPoints implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Points p;
                    switch (points) {
                        case (5):
                            p = new FivePointTele();
                            break;
                        case (3):
                            p = new ThreePointTele();
                            break;
                        case (2):
                            p = new TwoPointTele();
                            break;
                        case (1):
                            p = new OnePointTele();
                            break;
                        default:
                            p = new ThreePointTele();
                    }
                    addScore(p);
                }
            }
        }
    }

    private final class StartDisplay extends JPanel {

        private final JLabel startingPosition = new JLabel("Starting Position : " + match.getStartingPosition());

        public StartDisplay() {
            startingPosition.setFont(startingPosition.getFont().deriveFont(Font.BOLD, 22));
            startingPosition.setHorizontalAlignment(SwingConstants.CENTER);
            JButton front = new Position(TeamMatch.StartingPosition.FrontMiddle);
            JButton frontLeft = new Position(TeamMatch.StartingPosition.FrontLeft);
            JButton frontRight = new Position(TeamMatch.StartingPosition.FrontRight);
            JButton left = new Position(TeamMatch.StartingPosition.Left);
            JButton middle = new Position(TeamMatch.StartingPosition.Middle);
            JButton right = new Position(TeamMatch.StartingPosition.Right);
            JButton backLeft = new Position(TeamMatch.StartingPosition.BackLeft);
            JButton backRight = new Position(TeamMatch.StartingPosition.BackRight);
            JButton back = new Position(TeamMatch.StartingPosition.BackMiddle);

            setLayout(LayoutFactory.createLayout());
            LayoutFactory factory = LayoutFactory.newFactory();
            add(startingPosition, factory.setWidth(3));
            add(frontLeft, factory.setY(1).setWidth(1));
            add(front, factory.setX(1));
            add(frontRight, factory.setX(2));
            add(left, factory.setY(2).setX(0));
            add(middle, factory.setX(1));
            add(right, factory.setX(2));
            add(backLeft, factory.setY(3).setX(0));
            add(back, factory.setX(1));
            add(backRight, factory.setX(2));
        }

        private final class Position extends JButton {

            private final TeamMatch.StartingPosition position;

            public Position(TeamMatch.StartingPosition position) {
                super(position.toString());
                this.position = position;
                setFont(buttonFont);
                addActionListener(new SetPosition());
            }

            private final class SetPosition implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    match.setStartingPosition(position);
                    startingPosition.setText("Starting Position : " + position);
                }
            }
        }
    }

    private final class ClimbDisplay extends JPanel {

        private final Color climbColor = Color.BLUE;
        private final JLabel climbTimer = new JLabel(String.format("%.2f",
                match.getClimb() != null ? match.getClimb().getClimbTime() : 0));
        private double startTime = System.currentTimeMillis();
        private boolean climbStopped = false;

        public ClimbDisplay() {
            super(LayoutFactory.createLayout());
            JLabel title = new JLabel("Climbing Points");
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            JButton ten = new ClimbPoint(10);
            JButton twenty = new ClimbPoint(20);
            JButton thirty = new ClimbPoint(30);
            JButton startClimb = new StartClimb();
            JButton stopClimb = new StopClimb();
            climbTimer.setFont(buttonFont);
            climbTimer.setHorizontalAlignment(SwingConstants.CENTER);

            LayoutFactory factory = LayoutFactory.newFactory();
            add(title, factory);
            add(startClimb, factory.setX(1));
            add(stopClimb, factory.setX(2));
            add(climbTimer, factory.setX(3));
            add(ten, factory.setY(1).setX(0));
            add(twenty, factory.setX(1));
            add(thirty, factory.setX(2).setWidth(2));
        }

        private final class StartClimb extends JButton {

            public StartClimb() {
                super("Start Climb");
                setBackground(Color.ORANGE);
                setFont(buttonFont);
                addActionListener(new Start());
            }

            private final class Start implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    startTime = System.currentTimeMillis() / 1000;
                    climbStopped = false;
                    final Timer t = new Timer();
                    t.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            climbTimer.setText(String.format("%.2f", (double) (System.currentTimeMillis() / 1000.0) - (long) startTime));
                            if (climbStopped || !isVisible()) {
                                t.cancel();
                            }
                        }
                    }, 0, 20);
                }
            }
        }

        private final class StopClimb extends JButton {

            public StopClimb() {
                super("Stop");
                setBackground(Color.ORANGE);
                setFont(buttonFont);
                addActionListener(new Stop());
            }

            private final class Stop implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    climbStopped = true;
                    climbTime = (System.currentTimeMillis() / 1000.0) - (long) startTime;
                }
            }
        }

        private final class ClimbPoint extends JButton {

            private final int points;

            public ClimbPoint(int points) {
                super("+" + points);
                this.points = points;
                setBackground(climbColor);
                setFont(buttonFont);
                addActionListener(new AddPoints());
            }

            private final class AddPoints implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Points p;
                    switch (points) {
                        case (10):
                            p = new TenPointClimb(climbTime);
                            break;
                        case (20):
                            p = new TwentyPointClimb(climbTime);
                            break;
                        case (30):
                            p = new ThirtyPointClimb(climbTime);
                            break;
                        default:
                            p = new TenPointClimb(climbTime);
                    }
                    addScore(p);
                }
            }
        }
    }

    private final class FoulDisplay extends JPanel {

        private final Color foulColor = Color.RED;

        public FoulDisplay() {
            super(LayoutFactory.createLayout());
            JLabel title = new JLabel("Foul Points");
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            JButton three = new FoulPoint(3);
            JButton twenty = new FoulPoint(20);

            LayoutFactory factory = LayoutFactory.newFactory();
            add(title, factory.setWidth(2));
            add(three, factory.setY(1).setWidth(1));
            add(twenty, factory.setX(1));
        }

        private final class FoulPoint extends JButton {

            private final int points;

            public FoulPoint(int points) {
                super("-" + points);
                this.points = points;
                setBackground(foulColor);
                setFont(buttonFont);
                addActionListener(new AddPoints());
            }

            private final class AddPoints implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Points p;
                    switch (points) {
                        case (3):
                            p = new ThreePointFoul();
                            break;
                        case (20):
                            p = new TwentyPointFoul();
                            break;
                        default:
                            p = new ThreePointFoul();
                    }
                    addScore(p);
                }
            }
        }
    }

    private final class RobotNotes extends JPanel {

        private JComboBox<TeamMatch.RobotType> robotType = new RobotType();
        private JComboBox<TeamMatch.ShooterType> shooterType = new ShooterType();
        private JSlider drivetrainRating = new DrivetrainRating();

        public RobotNotes() {
            super(LayoutFactory.createLayout());
            JLabel robotTypeLabel = new JLabel("Robot Type");
            robotTypeLabel.setFont(robotTypeLabel.getFont().deriveFont(Font.BOLD, 22));
            robotTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JLabel drivetrainRatingLabel = new JLabel("Drivetrain rating");
            drivetrainRatingLabel.setFont(robotTypeLabel.getFont());
            drivetrainRatingLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JLabel matchNotesLabel = new JLabel("Match Notes");
            JLabel teamNotesLabel = new JLabel("Team Notes");
            matchNotesLabel.setFont(drivetrainRatingLabel.getFont());
            matchNotesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            teamNotesLabel.setFont(matchNotesLabel.getFont());
            teamNotesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            robotType.setSelectedItem(match.getRobotType());
            shooterType.setSelectedItem(match.getShooterType());
            ground.setHorizontalAlignment(SwingConstants.RIGHT);
            matchNotes.setBorder(new BevelBorder(BevelBorder.LOWERED));
            teamNotes.setBorder(new BevelBorder(BevelBorder.LOWERED));

            LayoutFactory factory = LayoutFactory.newFactory().setInsets(new Insets(3, 5, 3, 5));
            add(robotTypeLabel, factory.setWidth(2));
            add(ground, factory.setY(1).setWidth(1));
            add(feeder, factory.setX(1));
            add(drivetrainRatingLabel, factory.setY(2).setX(0).setWidth(2));
            add(drivetrainRating, factory.setY(3));
            add(robotType, factory.setY(4));
            add(shooterType, factory.setY(5));
            add(matchNotesLabel, factory.setY(6).setX(0).setWidth(1));
            add(teamNotesLabel, factory.setX(1));
            add(matchNotes, factory.setY(7).setX(0));
            add(teamNotes, factory.setX(1));
        }

        private class RobotType extends JComboBox<TeamMatch.RobotType> {

            public RobotType() {
                super(TeamMatch.RobotType.values());
                addActionListener(new SetRobotType());
            }

            private class SetRobotType implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    match.setRobotType((TeamMatch.RobotType) getSelectedItem());
                }
            }
        }

        private class ShooterType extends JComboBox<TeamMatch.ShooterType> {

            public ShooterType() {
                super(TeamMatch.ShooterType.values());
                addActionListener(new SetShooterType());
            }

            private class SetShooterType implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    match.setShooterType((TeamMatch.ShooterType) getSelectedItem());
                }
            }
        }

        private class DrivetrainRating extends JSlider {

            public DrivetrainRating() {
                super(1, 5, match.getDrivetrainRating());
                addChangeListener(new SetDrivetrainRating());
            }

            private class SetDrivetrainRating implements ChangeListener {

                @Override
                public void stateChanged(ChangeEvent e) {
                    match.setDrivetrainRating(getValue());
                }
            }
        }
    }

    private final class WinLossDisplay extends JPanel {

        private final JLabel winLabel = new JLabel("Result - " + match.getMatchResult());

        public WinLossDisplay() {
            super(LayoutFactory.createLayout());
            winLabel.setFont(winLabel.getFont().deriveFont(Font.BOLD, 22));
            winLabel.setHorizontalAlignment(SwingConstants.CENTER);
            LayoutFactory factory = LayoutFactory.newFactory();
            add(new WinLossButton(true), factory);
            add(new WinLossButton(false), factory.setX(1));
            add(winLabel, factory.setY(1).setX(0).setWidth(2));
        }

        private final class WinLossButton extends JButton {

            private final boolean win;

            public WinLossButton(boolean win) {
                super(win ? "Win" : "Loss");
                this.win = win;
                setFont(buttonFont);
                setBackground(win ? Color.GREEN : Color.LIGHT_GRAY);
                addActionListener(new SetWin());
            }

            private final class SetWin implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    match.setMatchResult(win ? TeamMatch.MatchResult.Win : TeamMatch.MatchResult.Loss);
                    winLabel.setText("Result - " + match.getMatchResult());
                }
            }
        }
    }

    private final class ControlDisplay extends JPanel {

        public ControlDisplay() {
            super(new GridLayout(1, 3));
            add(new SaveButton());
            add(new LeaveButton());
            add(new UndoButton());
        }

        private final class SaveButton extends JButton {

            public SaveButton() {
                super("Save");
                addActionListener(new Save());
            }

            private final class Save implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (ground.isSelected()) {
                        match.addIntake(TeamMatch.Intake.GroundPickup);
                    } else {
                        match.removeIntake(TeamMatch.Intake.GroundPickup);
                    }
                    if (feeder.isSelected()) {
                        match.addIntake(TeamMatch.Intake.FeederStation);
                    } else {
                        match.removeIntake(TeamMatch.Intake.FeederStation);
                    }
                    match.setNote(TeamMatch.NoteType.MatchNote, matchNotes.getText());
                    match.setNote(TeamMatch.NoteType.TeamNote, teamNotes.getText());
                    Scouter.getMain().putMatch(team, match);
                    dispose();
                }
            }
        }

        private final class LeaveButton extends JButton {

            public LeaveButton() {
                super("Leave (without saving)");
                addActionListener(new Leave());
            }

            private final class Leave implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            }
        }

        private final class UndoButton extends JButton {

            public UndoButton() {
                super("Undo");
                addActionListener(new Undo());
            }

            private final class Undo implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    match.popLastPoints();
                    scoreDisplay.update();
                }
            }
        }
    }
}
