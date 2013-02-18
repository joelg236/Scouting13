package edu.ata.scouting;

import java.io.Serializable;
import java.util.ArrayList;

public class Match implements Serializable {

    private static final long serialVersionUID = 95L;
    private final int teamNumber;
    private final int matchNumber;
    private String robotType = "";
    private String startingPosition = "";
    private String notes = "";
    private final ArrayList<Points> points = new ArrayList<>();

    public Match(int teamNumber, int matchNumber) {
        this.teamNumber = teamNumber;
        this.matchNumber = matchNumber;
    }

    public void setRobotType(String robotType) {
        this.robotType = robotType;
    }

    public void setStartingPosition(String startingPosition) {
        this.startingPosition = startingPosition;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void removePoints(Points points) {
        this.points.remove(this.points.lastIndexOf(points));
    }

    public void addAutonomousPoints(AutonomousPoints autonomousPoints) {
        points.add(autonomousPoints);
    }

    public void addShootingPoints(ShootingPoints shootingPoints) {
        points.add(shootingPoints);
    }

    public void addPyramidPoints(PyramidPoints pyramidPoints) {
        points.add(pyramidPoints);
    }

    public void addFoulPoints(FoulPoints foulPoints) {
        points.add(foulPoints);
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public String getRobotType() {
        return robotType;
    }

    public String getStartingPosition() {
        return startingPosition;
    }

    public String getNotes() {
        return notes;
    }

    public ArrayList<Points> getPoints() {
        return points;
    }

    public int getTotalPoints() {
        int p = 0;
        for (Points point : points) {
            p += point.getPoints();
        }
        return p;
    }

    public int getTotalAutonomousPoints() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof AutonomousPoints) {
                p += point.getPoints();
            }
        }
        return p;
    }

    public int getTotalShootingPoints() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof ShootingPoints) {
                p += point.getPoints();
            }
        }
        return p;
    }

    public int getTotalPyramidPoints() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof PyramidPoints) {
                p += point.getPoints();
            }
        }
        return p;
    }

    public int getTotalFoulPoints() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof FoulPoints) {
                p += point.getPoints();
            }
        }
        return p;
    }

    public int getAutoTwoPointers() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof AutonomousPoints.TwoShot) {
                p++;
            }
        }
        return p;
    }

    public int getAutoFourPointers() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof AutonomousPoints.FourShot) {
                p++;
            }
        }
        return p;
    }

    public int getAutoSixPointers() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof AutonomousPoints.SixShot) {
                p++;
            }
        }
        return p;
    }

    public int getAutoShotsMade() {
        return getAutoTwoPointers() + getAutoFourPointers() + getAutoSixPointers();
    }

    public int getTeleOnePointers() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof ShootingPoints.OneShot) {
                p++;
            }
        }
        return p;
    }

    public int getTeleTwoPointers() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof ShootingPoints.TwoShot) {
                p++;
            }
        }
        return p;
    }

    public int getTeleThreePointers() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof ShootingPoints.ThreeShot) {
                p++;
            }
        }
        return p;
    }

    public int getTeleShotsMade() {
        return getTeleOnePointers() + getTeleTwoPointers() + getTeleThreePointers();
    }

    public int getTotalShotsMade() {
        return getAutoShotsMade() + getTeleShotsMade();
    }

    public int getClimbTenPointers() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof PyramidPoints.TenClimb) {
                p++;
            }
        }
        return p;
    }

    public int getClimbTwentyPointers() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof PyramidPoints.TwentyClimb) {
                p++;
            }
        }
        return p;
    }

    public int getClimbThirtyPointers() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof PyramidPoints.ThirtyClimb) {
                p++;
            }
        }
        return p;
    }

    public int getFoulThreePointers() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof FoulPoints.ThreeFoul) {
                p++;
            }
        }
        return p;
    }

    public int getFoulTwentyPointers() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof FoulPoints.TwentyFoul) {
                p++;
            }
        }
        return p;
    }

    public int getFoulsDone() {
        return getFoulThreePointers() + getFoulTwentyPointers();
    }

    @Override
    public String toString() {
        return "Team " + getTeamNumber() + " - Match " + getMatchNumber();
    }

    public static class Points implements Serializable {

        private static final long serialVersionUID = Match.serialVersionUID;
        private final int points;

        Points(int points) {
            this.points = points;
        }

        public final int getPoints() {
            return points;
        }

        @Override
        public String toString() {
            return String.valueOf(getPoints());
        }
    }

    public static class AutonomousPoints extends Points {

        private AutonomousPoints(int points) {
            super(points);
        }

        public static class SixShot extends AutonomousPoints {

            public SixShot() {
                super(6);
            }
        }

        public static class FourShot extends AutonomousPoints {

            public FourShot() {
                super(4);
            }
        }

        public static class TwoShot extends AutonomousPoints {

            public TwoShot() {
                super(2);
            }
        }
    }

    public static class ShootingPoints extends Points {

        private ShootingPoints(int points) {
            super(points);
        }

        public static class ThreeShot extends ShootingPoints {

            public ThreeShot() {
                super(3);
            }
        }

        public static class TwoShot extends ShootingPoints {

            public TwoShot() {
                super(2);
            }
        }

        public static class OneShot extends ShootingPoints {

            public OneShot() {
                super(1);
            }
        }
    }

    public static class PyramidPoints extends Points {

        private PyramidPoints(int points) {
            super(points);
        }

        public static class ThirtyClimb extends PyramidPoints {

            public ThirtyClimb() {
                super(30);
            }
        }

        public static class TwentyClimb extends PyramidPoints {

            public TwentyClimb() {
                super(20);
            }
        }

        public static class TenClimb extends PyramidPoints {

            public TenClimb() {
                super(10);
            }
        }
    }

    public static class FoulPoints extends Points {

        private FoulPoints(int points) {
            super(-points);
        }

        public static class ThreeFoul extends FoulPoints {

            public ThreeFoul() {
                super(3);
            }
        }

        public static class TwentyFoul extends FoulPoints {

            public TwentyFoul() {
                super(20);
            }
        }
    }
}
