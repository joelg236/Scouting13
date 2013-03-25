package edu.ata.scouting.points;

import edu.ata.scouting.Scouter;
import java.io.Serializable;

public class Points implements Serializable {

    private static final long serialVersionUID = Scouter.serialVersionUID;
    private final int points;

    private Points(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public String toNumberString() {
        return (points > 0) ? ("+" + points) : ("" + points);
    }

    @Override
    public String toString() {
        return "Points : " + points;
    }

    public static class AutoPoints extends Points {

        private static final long serialVersionUID = Scouter.serialVersionUID;

        protected AutoPoints(int points) {
            super(points);
        }

        @Override
        public String toString() {
            return "Autonomous Points : " + getPoints();
        }
    }

    public static class TelePoints extends Points {

        private static final long serialVersionUID = Scouter.serialVersionUID;

        protected TelePoints(int points) {
            super(points);
        }

        @Override
        public String toString() {
            return "Teleoperated Points : " + getPoints();
        }
    }

    public static class ClimbPoints extends Points {

        private static final long serialVersionUID = Scouter.serialVersionUID;
        private final double climbTime;

        protected ClimbPoints(int points, double climbTime) {
            super(points);
            this.climbTime = climbTime;
        }

        public double getClimbTime() {
            return climbTime;
        }

        @Override
        public String toString() {
            return "Climbing Points : " + getPoints();
        }
    }

    public static class FoulPoints extends Points {

        private static final long serialVersionUID = Scouter.serialVersionUID;

        protected FoulPoints(int points) {
            super(-points);
        }

        @Override
        public String toString() {
            return "Foul Points : " + (-getPoints());
        }
    }
}