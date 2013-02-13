package edu.ATA;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

public class TeamMatch implements Serializable {

    public static final String OFF = "Offensive", DEF = "Defensive";
    public static final String CENT_FRONT = "Centre front", CENT_BACK = "Centre back",
            LEFT_FRONT = "Left front", RIGHT_FRONT = "Right front", LEFT = "Left",
            RIGHT = "Right", LEFT_BACK = "Left back", RIGHT_BACK = "Right back";
    private final int TEAM_NUM;
    private final int MATCH_NUM;
    private final String ROBOT_TYPE;
    private String startingPosition = "N/A";
    private int onePointers;
    private int twoPointers;
    private int threePointers;
    private int tenClimbs;
    private int twentyClimbs;
    private int thirtyClimbs;
    private int threeFouls;
    private int twentyFouls;
    private int finalScore;
    private int score;
    private int fouls;
    private int pyramids;
    private String notes = "None";
    private int currentAction = 0;
    private ArrayList<UndoableAction> lastActions = new ArrayList<>();
    // Components
    private final JTextField robotType;
    private final JTextField startingPos;
    private final JFormattedTextField finalScoreComponent;
    private final JFormattedTextField scoreComponent;
    private final JFormattedTextField foulsComponent;
    private final JFormattedTextField pyramidsComponent;
    public final JButton centreFront, centreBack, leftFront, rightFront, left,
            right, leftBack, rightBack;
    public final JButton plusOne, plusTwo, plusThree, tenClimb, twentyClimb, thirtyClimb, threeFoul, twentyFoul;

    public TeamMatch(int TEAM_NUM, int MATCH_NUM, String ROBOT_TYPE) {
        this.TEAM_NUM = TEAM_NUM;
        this.MATCH_NUM = MATCH_NUM;
        this.ROBOT_TYPE = ROBOT_TYPE;
        this.robotType = new JTextField(ROBOT_TYPE);
        robotType.setEditable(false);
        this.startingPos = new JTextField(startingPosition);
        startingPos.setEditable(false);
        this.finalScoreComponent = new JFormattedTextField(Integer.valueOf(finalScore));
        finalScoreComponent.setEditable(false);
        this.scoreComponent = new JFormattedTextField(Integer.valueOf(score));
        scoreComponent.setEditable(false);
        this.foulsComponent = new JFormattedTextField(Integer.valueOf(fouls));
        foulsComponent.setEditable(false);
        this.pyramidsComponent = new JFormattedTextField(Integer.valueOf(pyramids));
        pyramidsComponent.setEditable(false);

        centreFront = new JButton(CENT_FRONT);
        centreFront.addActionListener(new SetPosition(CENT_FRONT));
        centreBack = new JButton(CENT_BACK);
        centreBack.addActionListener(new SetPosition(CENT_BACK));
        leftFront = new JButton(LEFT_FRONT);
        leftFront.addActionListener(new SetPosition(LEFT_FRONT));
        rightFront = new JButton(RIGHT_FRONT);
        rightFront.addActionListener(new SetPosition(RIGHT_FRONT));
        left = new JButton(LEFT);
        left.addActionListener(new SetPosition(LEFT));
        right = new JButton(RIGHT);
        right.addActionListener(new SetPosition(RIGHT));
        leftBack = new JButton(LEFT_BACK);
        leftBack.addActionListener(new SetPosition(LEFT_BACK));
        rightBack = new JButton(RIGHT_BACK);
        rightBack.addActionListener(new SetPosition(RIGHT_BACK));

        plusOne = new Score(1);
        plusTwo = new Score(2);
        plusThree = new Score(3);
        tenClimb = new Climb(10);
        twentyClimb = new Climb(20);
        thirtyClimb = new Climb(30);
        threeFoul = new Foul(3);
        twentyFoul = new Foul(20);
    }

    public void undo() {
        lastActions.get(currentAction).undo();
        currentAction--;
    }

    private void addToFinalScore(final int score) {
        this.finalScore += score;
        finalScoreComponent.setText(String.valueOf(finalScore));
    }

    public void addScore(final int additionalScore) {
        switch (additionalScore) {
            case (1):
                onePointers++;
                break;
            case (2):
                twoPointers++;
                break;
            case (3):
                threePointers++;
                break;
        }
        this.score += additionalScore;
        scoreComponent.setValue(Integer.valueOf(this.score));
        addToFinalScore(additionalScore);
        lastActions.add(new UndoableAction() {
            @Override
            public void undo() {
                score -= additionalScore;
                scoreComponent.setValue(Integer.valueOf(score));
                addToFinalScore(-additionalScore);
                switch (additionalScore) {
                    case (1):
                        onePointers--;
                        break;
                    case (2):
                        twoPointers--;
                        break;
                    case (3):
                        threePointers--;
                        break;
                }
            }
        });
        currentAction = lastActions.size() - 1;
    }

    public void addClimb(final int climb) {
        switch (climb) {
            case (10):
                tenClimbs++;
                break;
            case (20):
                twentyClimbs++;
                break;
            case (30):
                thirtyClimbs++;
                break;
        }
        pyramids += climb;
        pyramidsComponent.setValue(Integer.valueOf(pyramids));
        addToFinalScore(climb);
        lastActions.add(new UndoableAction() {
            @Override
            public void undo() {
                pyramids -= climb;
                pyramidsComponent.setValue(Integer.valueOf(pyramids));
                addToFinalScore(-climb);
                switch (climb) {
                    case (10):
                        tenClimbs--;
                        break;
                    case (20):
                        twentyClimbs--;
                        break;
                    case (30):
                        thirtyClimbs--;
                        break;
                }
            }
        });
        currentAction = lastActions.size() - 1;
    }

    public void addFoul(final int foul) {
        switch (foul) {
            case (3):
                threeFouls++;
                break;
            case (20):
                twentyFouls++;
                break;
        }
        this.fouls += foul;
        foulsComponent.setValue(Integer.valueOf(fouls));
        addToFinalScore(-foul);
        lastActions.add(new UndoableAction() {
            @Override
            public void undo() {
                fouls -= foul;
                foulsComponent.setValue(Integer.valueOf(fouls));
                addToFinalScore(foul);
                switch (foul) {
                    case (3):
                        threeFouls--;
                        break;
                    case (20):
                        twentyFouls--;
                        break;
                }
            }
        });
        currentAction = lastActions.size() - 1;
    }

    public void setStartingPosition(String position) {
        final String lastPosition = this.startingPosition;
        this.startingPosition = position;
        startingPos.setText(position);
        lastActions.add(new UndoableAction() {
            @Override
            public void undo() {
                startingPosition = lastPosition;
                startingPos.setText(startingPosition);
            }
        });
        currentAction = lastActions.size() - 1;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRobotType() {
        return ROBOT_TYPE;
    }

    public JTextField getRobotTypeComponent() {
        return robotType;
    }

    public String getStartingPosition() {
        return startingPosition;
    }

    public JTextField getStartingPositionComponent() {
        return startingPos;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public JFormattedTextField getFinalScoreComponent() {
        return finalScoreComponent;
    }

    public int getScore() {
        return score;
    }

    public JFormattedTextField getScoreComponent() {
        return scoreComponent;
    }

    public int getFouls() {
        return fouls;
    }

    public JFormattedTextField getFoulsComponent() {
        return foulsComponent;
    }

    public int getPyramidPoints() {
        return pyramids;
    }

    public JFormattedTextField getPyramidPointsComponent() {
        return pyramidsComponent;
    }

    public int getTeamNumber() {
        return TEAM_NUM;
    }

    public int getMatchNumber() {
        return MATCH_NUM;
    }

    public String getNotes() {
        return notes;
    }

    public int getOnePointers() {
        return onePointers;
    }

    public int getTwoPointers() {
        return twoPointers;
    }

    public int getThreePointers() {
        return threePointers;
    }

    public int getTenClimbs() {
        return tenClimbs;
    }

    public int getTwentyClimbs() {
        return twentyClimbs;
    }

    public int getThirtyClimbs() {
        return thirtyClimbs;
    }

    public int getThreeFouls() {
        return threeFouls;
    }

    public int getTwentyFouls() {
        return twentyFouls;
    }

    @Override
    public String toString() {
        return "Team " + TEAM_NUM + " - Match " + MATCH_NUM;
    }

    private class SetPosition implements ActionListener, Serializable {

        private final String position;

        public SetPosition(String position) {
            this.position = position;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setStartingPosition(position);
        }
    }

    private class Score extends JButton implements Serializable {

        private final int score;

        public Score(final int score) {
            super(score > 0 ? ("+" + score) : String.valueOf(score));
            this.score = score;
            addActionListener(new AddScore());
        }

        private class AddScore implements ActionListener, Serializable {

            @Override
            public void actionPerformed(ActionEvent e) {
                addScore(score);
            }
        }
    }

    private class Climb extends JButton implements Serializable {

        private final int climb;

        public Climb(final int climb) {
            super(String.valueOf(climb));
            this.climb = climb;
            addActionListener(new AddClimb());
        }

        private class AddClimb implements ActionListener, Serializable {

            @Override
            public void actionPerformed(ActionEvent e) {
                addClimb(climb);
            }
        }
    }

    private class Foul extends JButton implements Serializable {

        private final int foul;

        public Foul(final int foul) {
            super(String.valueOf(foul));
            this.foul = foul;
            addActionListener(new AddFoul());
        }

        private class AddFoul implements ActionListener, Serializable {

            @Override
            public void actionPerformed(ActionEvent e) {
                addFoul(foul);
            }
        }
    }

    private interface UndoableAction extends Serializable {

        void undo();
    }
}
