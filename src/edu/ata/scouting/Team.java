package edu.ata.scouting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Team {

    private final ArrayList<Match> matches = new ArrayList<>();
    private final int teamNumber;

    public Team(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public Team(int teamNumber, Match[] initalMatches) {
        this.teamNumber = teamNumber;
        Collections.addAll(matches, initalMatches);
    }

    public Match getMatch(int matchNum) {
        for (Match m : matches) {
            if (m.getMatchNumber() == matchNum) {
                return m;
            }
        }
        throw new NullPointerException("Match " + matchNum + " does not exist in " + toString());
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    public void addMatches(Match[] matches) {
        Collections.addAll(this.matches, matches);
    }

    public void addMatches(List<Match> matches) {
        this.matches.addAll(matches);
    }

    public int getMatches() {
        return matches.size();
    }

    public String getMostCommonStartingPosition() {
        ArrayList<String> positions = new ArrayList<>();
        ArrayList<Integer> times = new ArrayList<>();
        for (Match match : matches) {
            String s = match.getStartingPosition();
            if (!s.isEmpty()) {
                if (positions.contains(s)) {
                    times.set(positions.indexOf(s), times.get(positions.indexOf(s)) + 1);
                } else {
                    positions.add(s);
                    times.add(1);
                }
            }
        }
        String mostCommon = "";
        int ts = 0;
        for (Integer t : times) {
            if (t > ts) {
                ts = t;
                mostCommon = positions.get(times.indexOf(t));
            }
        }
        return mostCommon;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public int getTotalPoints() {
        int p = 0;
        for (Match match : matches) {
            p += match.getTotalPoints();
        }
        return p;
    }

    public double getAveragePoints() {
        return (double) getTotalPoints() / (double) getMatches();
    }

    public int getTotalAutonomousPoints() {
        int a = 0;
        for (Match match : matches) {
            a += match.getTotalAutonomousPoints();
        }
        return a;
    }

    public double getAverageAutonomousPoints() {
        return (double) getTotalAutonomousPoints() / (double) getMatches();
    }

    public int getTotalShootingPoints() {
        int s = 0;
        for (Match match : matches) {
            s += match.getTotalShootingPoints();
        }
        return s;
    }

    public double getAverageShootingPoints() {
        return (double) getTotalShootingPoints() / (double) getMatches();
    }

    public int getTotalPyramidPoints() {
        int p = 0;
        for (Match match : matches) {
            p += match.getTotalPyramidPoints();
        }
        return p;
    }

    public double getAveragePyramidPoints() {
        return (double) getTotalPyramidPoints() / (double) getMatches();
    }

    public int getTotalFoulPoints() {
        int f = 0;
        for (Match match : matches) {
            f += match.getTotalFoulPoints();
        }
        return f;
    }

    public double getAverageFoulPoints() {
        return (double) getTotalFoulPoints() / (double) getMatches();
    }

    public int getAutoSixPointers() {
        int six = 0;
        for (Match match : matches) {
            six += match.getAutoSixPointers();
        }
        return six;
    }

    public int getAutoFourPointers() {
        int four = 0;
        for (Match match : matches) {
            four += match.getAutoFourPointers();
        }
        return four;
    }

    public int getAutoTwoPointers() {
        int two = 0;
        for (Match match : matches) {
            two += match.getAutoTwoPointers();
        }
        return two;
    }

    public int getMostCommonAutonomousShot() {
        int six = getAutoSixPointers(), four = getAutoFourPointers(), two = getAutoTwoPointers();
        int max = Math.max(six, Math.max(four, two));
        return max == 0 ? 0 : ((max == six) ? 6 : ((max == four) ? 4 : ((max == two) ? 2 : 0)));
    }

    public int getTotalAutonomousShotsMade() {
        int a = 0;
        for (Match match : matches) {
            a += match.getAutoShotsMade();
        }
        return a;
    }

    public int getTeleopThreePointers() {
        int three = 0;
        for (Match match : matches) {
            three += match.getTeleThreePointers();
        }
        return three;
    }

    public int getTeleopTwoPointers() {
        int two = 0;
        for (Match match : matches) {
            two += match.getTeleTwoPointers();
        }
        return two;
    }

    public int getTeleopOnePointers() {
        int one = 0;
        for (Match match : matches) {
            one += match.getTeleOnePointers();
        }
        return one;
    }

    public int getMostCommonTeleopShot() {
        int three = getTeleopThreePointers(), two = getTeleopTwoPointers(), one = getTeleopOnePointers();
        int max = Math.max(three, Math.max(two, one));
        return max == 0 ? 0 : ((max == three) ? 3 : ((max == two) ? 2 : ((max == one) ? 1 : 0)));
    }

    public int getTotalTeleopShotsMade() {
        int t = 0;
        for (Match match : matches) {
            t += match.getTeleShotsMade();
        }
        return t;
    }

    public int getTotalShotsMade() {
        int s = 0;
        for (Match match : matches) {
            s += match.getTotalShotsMade();
        }
        return s;
    }

    public int getTenClimbs() {
        int ten = 0;
        for (Match match : matches) {
            ten += match.getClimbTenPointers();
        }
        return ten;
    }

    public int getTwentyClimbs() {
        int twenty = 0;
        for (Match match : matches) {
            twenty += match.getClimbTwentyPointers();
        }
        return twenty;
    }

    public int getThirtyClimbs() {
        int thirty = 0;
        for (Match match : matches) {
            thirty += match.getClimbThirtyPointers();
        }
        return thirty;
    }

    public int getMostCommonClimb() {
        int thirty = getThirtyClimbs(), twenty = getTwentyClimbs(), ten = getTenClimbs();
        int max = Math.max(thirty, Math.max(twenty, ten));
        return max == 0 ? 0 : ((max == thirty) ? 30 : ((max == twenty) ? 20 : ((max == ten) ? 10 : 0)));
    }

    public int getThreePointFouls() {
        int three = 0;
        for (Match match : matches) {
            three += match.getFoulThreePointers();
        }
        return three;
    }

    public int getTwentyPointFouls() {
        int twenty = 0;
        for (Match match : matches) {
            twenty += match.getFoulTwentyPointers();
        }
        return twenty;
    }

    public int getFoulsDone() {
        int fouls = 0;
        for (Match match : matches) {
            fouls += match.getFoulsDone();
        }
        return fouls;
    }

    public double autonomousStandardDeviation() {
        return standardDeviation(autonomous());
    }

    public double autonomousMean() {
        return mean(autonomous());
    }

    public double teleopStandardDeviation() {
        return standardDeviation(teleop());
    }

    public double teleopMean() {
        return mean(teleop());
    }

    public double pyramidStandardDeviation() {
        return standardDeviation(pyramid());
    }

    public double pyramidMean() {
        return mean(pyramid());
    }

    public double foulStandardDeviation() {
        return standardDeviation(foul());
    }

    public double foulMean() {
        return mean(foul());
    }

    private List<Integer> autonomous() {
        ArrayList<Integer> a = new ArrayList<>();
        for (Match match : matches) {
            a.add(match.getTotalAutonomousPoints());
        }
        return a;
    }

    private List<Integer> teleop() {
        ArrayList<Integer> t = new ArrayList<>();
        for (Match match : matches) {
            t.add(match.getTotalShootingPoints());
        }
        return t;
    }

    private List<Integer> pyramid() {
        ArrayList<Integer> p = new ArrayList<>();
        for (Match match : matches) {
            p.add(match.getTotalPyramidPoints());
        }
        return p;
    }

    private List<Integer> foul() {
        ArrayList<Integer> f = new ArrayList<>();
        for (Match match : matches) {
            f.add(match.getTotalFoulPoints());
        }
        return f;
    }

    private double standardDeviation(List<Integer> a) {
        int sum = 0;
        double mean = mean(a);
        for (Integer i : a) {
            sum += Math.pow((i - mean), 2);
        }
        return Math.sqrt(sum / (a.size() - 1));
    }

    private double mean(List<Integer> a) {
        int sum = sum(a);
        double mean = sum / (a.size() * 1.0);
        return mean;
    }

    private int sum(List<Integer> a) {
        int sum = 0;
        for (Integer i : a) {
            sum += i;
        }
        return sum;
    }
}
