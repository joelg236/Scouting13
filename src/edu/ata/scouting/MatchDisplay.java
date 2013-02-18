package edu.ata.scouting;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public abstract class MatchDisplay extends JPanel {

    private final Match match;
    private final ArrayList<Action> actions = new ArrayList<>();
    private JFormattedTextField finalMatchScore;
    private int currentAction = -1;
    public static String path = System.getProperty("user.home")
            + System.getProperty("file.separator") + "scouting"
            + System.getProperty("file.separator");

    public MatchDisplay(Match match) {
        super(LayoutFactory.createLayout());
        this.match = match;
        init();
    }

    public abstract void closeWindow();

    private void updateMatchScore() {
        finalMatchScore.setValue(match.getTotalPoints());
    }

    private void init() {
        LayoutFactory factory = LayoutFactory.newFactory(GridBagConstraints.BOTH, 1, 1);

        JLabel robotTypeLabel = new JLabel("Robot Type");
        JLabel startingPositionLabel = new JLabel("Starting Position");
        JLabel finalScoreLabel = new JLabel("Final Score");
        JLabel autoScoreLabel = new JLabel("Auto Score");
        JLabel shootingScoreLabel = new JLabel("Shooting Score");
        JLabel pyramidScoreLabel = new JLabel("Pyramid Score");
        JLabel foulScoreLabel = new JLabel("Foul Points");
        JLabel notesLabel = new JLabel("Notes");

        JFormattedTextField robotTypeField = new JFormattedTextField(match.getRobotType());
        JFormattedTextField startingPositionField = new JFormattedTextField(match.getStartingPosition());
        finalMatchScore = new JFormattedTextField(match.getTotalPoints());
        JFormattedTextField autoScoreField = new JFormattedTextField(match.getTotalAutonomousPoints());
        JFormattedTextField shootingScoreField = new JFormattedTextField(match.getTotalShootingPoints());
        JFormattedTextField pyramidScoreField = new JFormattedTextField(match.getTotalPyramidPoints());
        JFormattedTextField foulScoreField = new JFormattedTextField(match.getTotalFoulPoints());
        JFormattedTextField notesField = new JFormattedTextField(match.getNotes());

        JButton offenseButton = new RobotTypeButton("Offense", robotTypeField);
        JButton defenseButton = new RobotTypeButton("Defense", robotTypeField);

        JButton leftFrontButton = new StartingPositionButton(startingPositionField, "Left Front");
        JButton centreFrontButton = new StartingPositionButton(startingPositionField, "Center Front");
        JButton rightFrontButton = new StartingPositionButton(startingPositionField, "Right Front");
        JButton leftButton = new StartingPositionButton(startingPositionField, "Left");
        JButton rightButton = new StartingPositionButton(startingPositionField, "Right");
        JButton leftBackButton = new StartingPositionButton(startingPositionField, "Left Back");
        JButton centreBackButton = new StartingPositionButton(startingPositionField, "Center Back");
        JButton rightBackButton = new StartingPositionButton(startingPositionField, "Right Back");

        JButton autoTwoButton = new AutoScoreButton(new Match.AutonomousPoints.TwoShot(), autoScoreField);
        JButton autoFourButton = new AutoScoreButton(new Match.AutonomousPoints.FourShot(), autoScoreField);
        JButton autoSixButton = new AutoScoreButton(new Match.AutonomousPoints.SixShot(), autoScoreField);

        JButton teleopOneButton = new TeleopScoreButton(new Match.ShootingPoints.OneShot(), shootingScoreField);
        JButton teleopTwoButton = new TeleopScoreButton(new Match.ShootingPoints.TwoShot(), shootingScoreField);
        JButton teleopThreeButton = new TeleopScoreButton(new Match.ShootingPoints.ThreeShot(), shootingScoreField);

        JButton pyramidTenButton = new PyramidScoreButton(new Match.PyramidPoints.TenClimb(), pyramidScoreField);
        JButton pyramidTwentyButton = new PyramidScoreButton(new Match.PyramidPoints.TwentyClimb(), pyramidScoreField);
        JButton pyramidThirtyButton = new PyramidScoreButton(new Match.PyramidPoints.ThirtyClimb(), pyramidScoreField);

        JButton foulThreeButton = new FoulPointsButton(new Match.FoulPoints.ThreeFoul(), foulScoreField);
        JButton foulTwentyButton = new FoulPointsButton(new Match.FoulPoints.TwentyFoul(), foulScoreField);

        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");
        JButton saveButton = new JButton("Save");
        JButton exitButton = new JButton("Discard");

        finalMatchScore.setEditable(false);
        autoScoreField.setEditable(false);
        shootingScoreField.setEditable(false);
        pyramidScoreField.setEditable(false);
        foulScoreField.setEditable(false);

        undoButton.addActionListener(new Undo());
        redoButton.addActionListener(new Redo());
        saveButton.addActionListener(new Save(robotTypeField, startingPositionField, notesField));
        exitButton.addActionListener(new Exit());

        add(robotTypeLabel, factory.setWeightX(0));
        add(startingPositionLabel, factory.setY(1));
        add(finalScoreLabel, factory.setY(3));
        add(autoScoreLabel, factory.setY(4));
        add(shootingScoreLabel, factory.setY(5));
        add(pyramidScoreLabel, factory.setY(6));
        add(foulScoreLabel, factory.setY(7));
        add(notesLabel, factory.setY(8));
        add(undoButton, factory.setWeightX(1).setY(9).setWidth(9));
        add(redoButton, factory.setY(10));
        add(saveButton, factory.setY(11));
        add(exitButton, factory.setY(12));

        add(robotTypeField, factory.setWidth(2).setX(1).setY(0));
        add(startingPositionField, factory.setWidth(8).setY(1));
        add(finalMatchScore, factory.setY(3));
        add(autoScoreField, factory.setWidth(2).setY(4));
        add(shootingScoreField, factory.setY(5));
        add(pyramidScoreField, factory.setY(6));
        add(foulScoreField, factory.setWidth(3).setY(7));
        add(notesField, factory.setWidth(8).setY(8));

        add(offenseButton, factory.setWidth(3).setY(0).setX(3));
        add(defenseButton, factory.setX(6));

        add(leftFrontButton, factory.setWidth(1).setY(2).setX(1));
        add(centreFrontButton, factory.setX(2));
        add(rightFrontButton, factory.setX(3));
        add(leftButton, factory.setX(4));
        add(rightButton, factory.setX(5));
        add(leftBackButton, factory.setX(6));
        add(centreBackButton, factory.setX(7));
        add(rightBackButton, factory.setX(8));

        add(autoTwoButton, factory.setWidth(2).setY(4).setX(3));
        add(autoFourButton, factory.setX(5));
        add(autoSixButton, factory.setX(7));

        add(teleopOneButton, factory.setY(5).setX(3));
        add(teleopTwoButton, factory.setX(5));
        add(teleopThreeButton, factory.setX(7));

        add(pyramidTenButton, factory.setY(6).setX(3));
        add(pyramidTwentyButton, factory.setX(5));
        add(pyramidThirtyButton, factory.setX(7));

        add(foulThreeButton, factory.setWidth(3).setY(7).setX(4));
        add(foulTwentyButton, factory.setX(7));
    }

    private abstract class Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doAction();
            actions.add(++currentAction, this);
        }

        public abstract void doAction();

        public abstract void undoAction();
    }

    private class RobotTypeButton extends JButton {

        private final JFormattedTextField robotType;
        private final String type;

        public RobotTypeButton(String text, JFormattedTextField robotType) {
            super(text);
            this.type = text;
            this.robotType = robotType;
            addActionListener(new SetRobotType());
        }

        private class SetRobotType extends Action {

            private ArrayList<String> prev = new ArrayList<>();
            private int current = -1;

            @Override
            public void doAction() {
                prev.add(match.getRobotType());
                current++;
                match.setRobotType(type);
                robotType.setText(type);
            }

            @Override
            public void undoAction() {
                match.setRobotType(prev.get(current));
                robotType.setText(prev.get(current));
                current--;
            }
        }
    }

    private class StartingPositionButton extends JButton {

        private final JFormattedTextField startingPosition;
        private final String position;

        public StartingPositionButton(JFormattedTextField startingPosition, String position) {
            super(position);
            this.startingPosition = startingPosition;
            this.position = position;
            addActionListener(new SetStartingPosition());
        }

        private class SetStartingPosition extends Action {

            private ArrayList<String> prev = new ArrayList<>();
            private int current = -1;

            @Override
            public void doAction() {
                prev.add(++current, match.getStartingPosition());
                match.setStartingPosition(position);
                startingPosition.setText(position);
            }

            @Override
            public void undoAction() {
                match.setStartingPosition(prev.get(current));
                startingPosition.setText(prev.get(current));
                current--;
            }
        }
    }

    private class AutoScoreButton extends JButton {

        private final Match.AutonomousPoints points;
        private final JFormattedTextField autoScore;

        public AutoScoreButton(Match.AutonomousPoints points, JFormattedTextField autoScore) {
            super("+" + points);
            this.points = points;
            this.autoScore = autoScore;
            addActionListener(new AddAutoScore());
        }

        private class AddAutoScore extends Action {

            @Override
            public void doAction() {
                match.addAutonomousPoints(points);
                autoScore.setValue(match.getTotalAutonomousPoints());
                updateMatchScore();
            }

            @Override
            public void undoAction() {
                match.removePoints(points);
                autoScore.setValue(match.getTotalAutonomousPoints());
                updateMatchScore();
            }
        }
    }

    private class TeleopScoreButton extends JButton {

        private final Match.ShootingPoints points;
        private final JFormattedTextField teleopScore;

        public TeleopScoreButton(Match.ShootingPoints points, JFormattedTextField teleopScore) {
            super("+" + points);
            this.points = points;
            this.teleopScore = teleopScore;
            addActionListener(new AddTeleopScore());
        }

        private class AddTeleopScore extends Action {

            @Override
            public void doAction() {
                match.addShootingPoints(points);
                teleopScore.setValue(match.getTotalShootingPoints());
                updateMatchScore();
            }

            @Override
            public void undoAction() {
                match.removePoints(points);
                teleopScore.setValue(match.getTotalShootingPoints());
                updateMatchScore();
            }
        }
    }

    private class PyramidScoreButton extends JButton {

        private final Match.PyramidPoints points;
        private final JFormattedTextField pyramidPoints;

        public PyramidScoreButton(Match.PyramidPoints points, JFormattedTextField pyramidPoints) {
            super("+" + points);
            this.points = points;
            this.pyramidPoints = pyramidPoints;
            addActionListener(new AddPyramidPoints());
        }

        private class AddPyramidPoints extends Action {

            @Override
            public void doAction() {
                match.addPyramidPoints(points);
                pyramidPoints.setValue(match.getTotalPyramidPoints());
                updateMatchScore();
            }

            @Override
            public void undoAction() {
                match.removePoints(points);
                pyramidPoints.setValue(match.getTotalPyramidPoints());
                updateMatchScore();
            }
        }
    }

    private class FoulPointsButton extends JButton {

        private final Match.FoulPoints points;
        private final JFormattedTextField foulPoints;

        public FoulPointsButton(Match.FoulPoints points, JFormattedTextField foulPoints) {
            super(String.valueOf(points));
            this.points = points;
            this.foulPoints = foulPoints;
            addActionListener(new AddFoulPoints());
        }

        private class AddFoulPoints extends Action {

            @Override
            public void doAction() {
                match.addFoulPoints(points);
                foulPoints.setValue(match.getTotalFoulPoints());
                updateMatchScore();
            }

            @Override
            public void undoAction() {
                match.removePoints(points);
                foulPoints.setValue(match.getTotalFoulPoints());
                updateMatchScore();
            }
        }
    }

    private class Undo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentAction >= 0) {
                actions.get(currentAction--).undoAction();
            }
        }
    }

    private class Redo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentAction + 1 < actions.size()) {
                actions.get(++currentAction).doAction();
            }
        }
    }

    private class Save implements ActionListener {

        private final JFormattedTextField robotType;
        private final JFormattedTextField startingPosition;
        private final JFormattedTextField notes;

        public Save(JFormattedTextField robotType, JFormattedTextField startingPosition, JFormattedTextField notes) {
            this.robotType = robotType;
            this.startingPosition = startingPosition;
            this.notes = notes;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            match.setRobotType(robotType.getText());
            match.setStartingPosition(startingPosition.getText());
            match.setNotes(notes.getText());

            try {
                File f = new File(path + match.toString() + ".data");
                if (!f.exists()) {
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                } else {
                    if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(null, "File already exists. Overrite?")) {
                        return;
                    }
                }
                FileOutputStream output = new FileOutputStream(f);
                ObjectOutputStream stream = new ObjectOutputStream(output);
                stream.writeObject(match);
                closeWindow();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Eror while saving to " + path + match.toString() + ".data");
            }
        }
    }

    private class Exit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            closeWindow();
        }
    }
}
