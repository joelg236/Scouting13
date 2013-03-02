package edu.ata.scouting;

import java.io.Serializable;
import java.util.ArrayList;

public class Match implements Serializable {

    public static final long serialVersionUID = 96L;
    private final int teamNumber;
    private final int matchNumber;
    private String robotType = "";
    private String startingPosition = "";
    private String notes = "";
    private WinLoss winLoss = new WinLoss.NotComplete();
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

    public void setWin(WinLoss wl) {
        this.winLoss = wl;
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

    public WinLoss getWinLoss() {
        return winLoss;
    }

    public boolean isWin() {
        return winLoss.isWin();
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
    
    public int getTeleFivePointers() {
        int p = 0;
        for (Points point : points) {
            if (point instanceof ShootingPoints.FiveShot) {
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

    public static class AutonomousPoints extends Points implements Serializable {

        private static final long serialVersionUID = Match.serialVersionUID;

        private AutonomousPoints(int points) {
            super(points);
        }

        public static final class SixShot extends AutonomousPoints {

            private static final long serialVersionUID = Match.serialVersionUID;

            public SixShot() {
                super(6);
            }
        }

        public static final class FourShot extends AutonomousPoints {

            private static final long serialVersionUID = Match.serialVersionUID;

            public FourShot() {
                super(4);
            }
        }

        public static final class TwoShot extends AutonomousPoints {

            private static final long serialVersionUID = Match.serialVersionUID;

            public TwoShot() {
                super(2);
            }
        }
    }

    public static class ShootingPoints extends Points implements Serializable {

        private static final long serialVersionUID = Match.serialVersionUID;

        private ShootingPoints(int points) {
            super(points);
        }
        
        public static final class FiveShot extends ShootingPoints {

            private static final long serialVersionUID = Match.serialVersionUID;

            public FiveShot() {
                super(5);
            }
        }

        public static final class ThreeShot extends ShootingPoints {

            private static final long serialVersionUID = Match.serialVersionUID;

            public ThreeShot() {
                super(3);
            }
        }

        public static final class TwoShot extends ShootingPoints {

            private static final long serialVersionUID = Match.serialVersionUID;

            public TwoShot() {
                super(2);
            }
        }

        public static final class OneShot extends ShootingPoints {

            private static final long serialVersionUID = Match.serialVersionUID;

            public OneShot() {
                super(1);
            }
        }
    }

    public static class PyramidPoints extends Points implements Serializable {

        private static final long serialVersionUID = Match.serialVersionUID;

        private PyramidPoints(int points) {
            super(points);
        }

        public static final class ThirtyClimb extends PyramidPoints {

            private static final long serialVersionUID = Match.serialVersionUID;

            public ThirtyClimb() {
                super(30);
            }
        }

        public static final class TwentyClimb extends PyramidPoints {

            private static final long serialVersionUID = Match.serialVersionUID;

            public TwentyClimb() {
                super(20);
            }
        }

        public static final class TenClimb extends PyramidPoints {

            private static final long serialVersionUID = Match.serialVersionUID;

            public TenClimb() {
                super(10);
            }
        }
    }

    public static class FoulPoints extends Points implements Serializable {

        private static final long serialVersionUID = Match.serialVersionUID;

        private FoulPoints(int points) {
            super(-points);
        }

        public static final class ThreeFoul extends FoulPoints {

            private static final long serialVersionUID = Match.serialVersionUID;

            public ThreeFoul() {
                super(3);
            }
        }

        public static final class TwentyFoul extends FoulPoints {

            private static final long serialVersionUID = Match.serialVersionUID;

            public TwentyFoul() {
                super(20);
            }
        }
    }

    public static class WinLoss implements Serializable {

        private static final long serialVersionUID = Match.serialVersionUID;
        private final boolean win;

        WinLoss(boolean win) {
            this.win = win;
        }

        public final boolean isWin() {
            return win;
        }

        @Override
        public String toString() {
            return "Win: " + win;
        }

        public static final class Win extends WinLoss {

            private static final long serialVersionUID = Match.serialVersionUID;

            public Win() {
                super(true);
            }
        }

        public static final class Loss extends WinLoss {

            private static final long serialVersionUID = Match.serialVersionUID;

            public Loss() {
                super(false);
            }
        }

        public static final class NotComplete extends WinLoss {

            private static final long serialVersionUID = Match.serialVersionUID;

            public NotComplete() {
                super(false);
            }
        }
    }
}
