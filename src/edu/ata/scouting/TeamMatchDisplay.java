package edu.ata.scouting;

import static edu.ata.scouting.Scouter.mainWindow;
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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public final class TeamMatchDisplay extends JDialog {

    private static final Font buttonFont = new Font("Default", Font.BOLD, 18);
    private final TeamMatch match;
    private final ScoreDisplay scoreDisplay;
    private JTextArea notes;

    public TeamMatchDisplay(TeamMatch match) {
        super(mainWindow, match.toString());
        this.match = match;
        this.notes = new JTextArea(match.getNotes());
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
        setUndecorated(true);
        setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        setLocationRelativeTo(Scouter.mainWindow);
    }

    private void addScore(Points points) {
        match.addPoints(points);
        scoreDisplay.update();
    }

    private final class ScoreDisplay extends JPanel {

        private final JLabel finalScore, autoScore, teleScore, climbScore, foulScore;

        public ScoreDisplay() {
            this.finalScore = new JLabel("Final Score : " + total());
            this.autoScore = new JLabel("Auto Score : " + auto());
            this.teleScore = new JLabel("Teleop Score : " + tele());
            this.climbScore = new JLabel("Climb Score : " + climb());
            this.foulScore = new JLabel("Foul Points : " + foul());
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

        private int total() {
            int x = 0;
            for (Points p : match.getPoints()) {
                x += p.getPoints();
            }
            return x;
        }

        private int auto() {
            int x = 0;
            for (Points p : match.getPoints()) {
                if (p instanceof Points.AutoPoints) {
                    x += p.getPoints();
                }
            }
            return x;
        }

        private int tele() {
            int x = 0;
            for (Points p : match.getPoints()) {
                if (p instanceof Points.TelePoints) {
                    x += p.getPoints();
                }
            }
            return x;
        }

        private int climb() {
            int x = 0;
            for (Points p : match.getPoints()) {
                if (p instanceof Points.ClimbPoints) {
                    x += p.getPoints();
                }
            }
            return x;
        }

        private int foul() {
            int x = 0;
            for (Points p : match.getPoints()) {
                if (p instanceof Points.FoulPoints) {
                    x += p.getPoints();
                }
            }
            return -x;
        }

        private void update() {
            finalScore.setText("Final Score : " + total());
            autoScore.setText("Auto Score : " + auto());
            teleScore.setText("Teleop Score : " + tele());
            climbScore.setText("Climb Score : " + climb());
            foulScore.setText("Foul Points : " + foul());
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
            JButton front = new Position("Front");
            JButton frontLeft = new Position("Front Left");
            JButton frontRight = new Position("Front Right");
            JButton left = new Position("Left");
            JButton middle = new Position("Middle");
            JButton right = new Position("Right");
            JButton backLeft = new Position("Back Left");
            JButton backRight = new Position("Back Right");
            JButton back = new Position("Back");

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

            private final String position;

            public Position(String position) {
                super(position);
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

        public ClimbDisplay() {
            super(LayoutFactory.createLayout());
            JLabel title = new JLabel("Climbing Points");
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            JButton ten = new ClimbPoint(10);
            JButton twenty = new ClimbPoint(20);
            JButton thirty = new ClimbPoint(30);

            LayoutFactory factory = LayoutFactory.newFactory();
            add(title, factory.setWidth(3));
            add(ten, factory.setY(1).setWidth(1));
            add(twenty, factory.setX(1));
            add(thirty, factory.setX(2));
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
                            p = new TenPointClimb();
                            break;
                        case (20):
                            p = new TwentyPointClimb();
                            break;
                        case (30):
                            p = new ThirtyPointClimb();
                            break;
                        default:
                            p = new TenPointClimb();
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

        private JTextField robotType = new JTextField(match.getRobotType().toString());

        public RobotNotes() {
            super(LayoutFactory.createLayout());
            JLabel robotTypeLabel = new JLabel("Robot Type");
            robotTypeLabel.setFont(robotTypeLabel.getFont().deriveFont(Font.BOLD, 22));
            robotTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JButton offense = new TypeButton(TeamMatch.OFFENSIVE);
            JButton defense = new TypeButton(TeamMatch.DEFENSIVE);
            JLabel notesLabel = new JLabel("Notes");
            notesLabel.setFont(robotTypeLabel.getFont());
            notesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            robotType.setEditable(false);

            LayoutFactory factory = LayoutFactory.newFactory();
            add(robotTypeLabel, factory.setWidth(3));
            add(robotType, factory.setY(1).setWidth(1));
            add(offense, factory.setX(1));
            add(defense, factory.setX(2));
            add(notesLabel, factory.setY(2).setX(0).setWidth(3));
            add(notes, factory.setY(3));
        }

        private final class TypeButton extends JButton {

            private final TeamMatch.RobotType type;

            public TypeButton(TeamMatch.RobotType type) {
                super(type.toString());
                this.type = type;
                setFont(buttonFont);
                addActionListener(new ChangeType());
            }

            private final class ChangeType implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    match.setRobotType(type);
                    robotType.setText(match.getRobotType().toString());
                }
            }
        }
    }

    private final class WinLossDisplay extends JPanel {

        private final JLabel winLabel = new JLabel("Result - " + match.getWin().toString());

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
                    match.setWin(win ? TeamMatch.WIN : TeamMatch.LOSS);
                    winLabel.setText("Result - " + match.getWin().toString());
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
                    match.setNotes(notes.getText());
                    Scouter.mainWindow.addTeamMatch(match);
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
                    match.removeLastPoints();
                    scoreDisplay.update();
                }
            }
        }
    }
}
