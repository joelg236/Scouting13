package edu.ata.scouting;

import edu.ata.scouting.points.Points;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;

public class DataDecompiling {

    private static final String COMMA = ",";

    public static void compile(final Regional regional, final ArrayList<TeamMatch> matches) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    compileAll(regional, matches);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(Scouter.mainWindow, ex);
                }
            }
        }).start();
    }

    private static void compileAll(Regional regional, ArrayList<TeamMatch> matches) throws IOException {
        HashMap<Team, ArrayList<TeamMatch>> teams = new HashMap<>();
        for (TeamMatch match : matches) {
            if (teams.containsKey(match.getTeam())) {
                teams.get(match.getTeam()).add(match);
            } else {
                ArrayList<TeamMatch> m = new ArrayList<>();
                m.add(match);
                teams.put(match.getTeam(), m);
            }
        }

        for (Team team : teams.keySet()) {
            File f = new File(Scouter.scoutingDir + regional + " Progress - " + team + ".csv");
            FileOutputStream stream = new FileOutputStream(f);
            stream.write(progressReport(team, teams.get(team)).getBytes());
            File g = new File(Scouter.scoutingDir + regional + " Game By Game - " + team + ".csv");
            FileOutputStream fos = new FileOutputStream(g);
            fos.write(gameByGame(team, teams.get(team)).getBytes());
        }
        File s = new File(Scouter.scoutingDir + regional + " Stats.csv");
        FileOutputStream f = new FileOutputStream(s);
        f.write(allStats(matches).getBytes());
    }

    public static int fullPoints(List<Points> points) {
        int x = 0;
        for (Points p : points) {
            x += p.getPoints();
        }
        return x;
    }

    public static int autoPoints(List<Points> points) {
        int x = 0;
        for (Points p : points) {
            if (p instanceof Points.AutoPoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public static int telePoints(List<Points> points) {
        int x = 0;
        for (Points p : points) {
            if (p instanceof Points.TelePoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public static int climbPoints(List<Points> points) {
        int x = 0;
        for (Points p : points) {
            if (p instanceof Points.ClimbPoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public static int foulPoints(List<Points> points) {
        int x = 0;
        for (Points p : points) {
            if (p instanceof Points.FoulPoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    private static String progressReport(Team t, ArrayList<TeamMatch> matches) {
        String fullReport = t + "\nMatch,Match Result,Robot Type,Starting Position,"
                + "Total Points,Auto Points,Tele Points,Climb Points,Foul Points,"
                + "Notes,Full Match Schedule\n";
        for (TeamMatch match : matches) {
            fullReport += match.getMatch() + COMMA;
            fullReport += match.getWin() + COMMA;
            fullReport += match.getRobotType() + COMMA;
            fullReport += match.getStartingPosition() + COMMA;
            fullReport += fullPoints(match.getPoints()) + COMMA;
            fullReport += autoPoints(match.getPoints()) + COMMA;
            fullReport += telePoints(match.getPoints()) + COMMA;
            fullReport += climbPoints(match.getPoints()) + COMMA;
            fullReport += foulPoints(match.getPoints()) + COMMA;
            fullReport += match.getNotes() + COMMA;

            for (Points p : match.getPoints()) {
                fullReport += p + " | ";
            }
            fullReport += "\n";
        }
        return fullReport;
    }

    private static String gameByGame(Team t, ArrayList<TeamMatch> matches) {
        String full = "Match,Points\n";
        for (TeamMatch match : matches) {
            full += match.getMatch() + COMMA;
            full += fullPoints(match.getPoints()) + "\n";
        }
        return full;
    }

    private static String allStats(ArrayList<TeamMatch> allMatches) {
        String full = "Team,Total Score,Average Score,Score Range,Score SD,"
                + "Total Auto,Average Auto,Auto Range,Auto SD,Total Tele,Average"
                + " Tele,Tele Range,Tele SD,Total Climb,Average Climb,Average "
                + "Climb Time,Climb SD,Total Fouls\n";
        HashMap<Team, ArrayList<TeamMatch>> teams = new HashMap<>();
        for (TeamMatch match : allMatches) {
            if (teams.containsKey(match.getTeam())) {
                teams.get(match.getTeam()).add(match);
            } else {
                ArrayList<TeamMatch> m = new ArrayList<>();
                m.add(match);
                teams.put(match.getTeam(), m);
            }
        }

        for (Team team : teams.keySet()) {
            full += stats(team, teams.get(team));
        }

        return full;
    }

    private static String stats(Team team, ArrayList<TeamMatch> matches) {
        String full = "";
        Stats stats = new Stats(matches);
        full += team + COMMA;
        full += stats.totalScore() + COMMA;
        full += stats.avgScore() + COMMA;
        full += stats.scoreRange() + COMMA;
        full += stats.scoreSD() + COMMA;
        full += stats.totalAuto() + COMMA;
        full += stats.avgAuto() + COMMA;
        full += stats.autoRange() + COMMA;
        full += stats.autoSD() + COMMA;
        full += stats.totalTele() + COMMA;
        full += stats.avgTele() + COMMA;
        full += stats.teleRange() + COMMA;
        full += stats.teleSD() + COMMA;
        full += stats.totalClimb() + COMMA;
        full += stats.avgClimb() + COMMA;
        full += stats.avgClimbTime() + COMMA;
        full += stats.climbSD() + COMMA;
        full += stats.totalFoul() + COMMA;
        full += "\n";
        return full;
    }

    private static class Stats {

        private final ArrayList<TeamMatch> matches;

        public Stats(ArrayList<TeamMatch> matches) {
            this.matches = matches;
        }

        private static int sum(List<Integer> a) {
            if (a.size() > 0) {
                int sum = 0;
                for (Integer i : a) {
                    sum += i;
                }
                return sum;
            }
            return 0;
        }

        private static double mean(List<Integer> a) {
            int sum = sum(a);
            double mean;
            mean = sum / (a.size() * 1.0);
            return mean;
        }

        private static double median(List<Integer> a) {
            int middle = a.size() / 2;
            if (a.size() % 2 == 1) {
                return a.get(middle);
            } else {
                return (a.get(middle - 1) + a.get(middle)) / 2.0;
            }
        }

        private static double sd(List<Integer> a) {
            if (a.size() < 2) {
                return 0;
            }
            int sum = 0;
            double mean = mean(a);
            for (Integer i : a) {
                sum += Math.pow((i - mean), 2);
            }
            return Math.sqrt(sum / (a.size() - 1));
        }

        private static int maxValue(int[] nums) {
            int max = nums[0];
            for (int x = 0; x < nums.length; x++) {
                if (nums[x] > max) {
                    max = nums[x];
                }
            }
            return max;
        }

        private static int minValue(int[] nums) {
            int min = nums[0];
            for (int ktr = 0; ktr < nums.length; ktr++) {
                if (nums[ktr] < min) {
                    min = nums[ktr];
                }
            }
            return min;
        }

        double matches() {
            return matches.size();
        }

        int totalScore() {
            int x = 0;
            for (TeamMatch t : matches) {
                x += fullPoints(t.getPoints());
            }
            return x;
        }

        double avgScore() {
            return ((double) totalScore()) / matches();
        }

        int scoreRange() {
            int[] scores = new int[matches.size()];
            for (int x = 0; x < scores.length; x++) {
                scores[x] = fullPoints(matches.get(x).getPoints());
            }
            return maxValue(scores) - minValue(scores);
        }

        double scoreSD() {
            ArrayList<Integer> points = new ArrayList<>();
            for (TeamMatch t : matches) {
                points.add(fullPoints(t.getPoints()));
            }
            return sd(points);
        }

        int totalAuto() {
            int x = 0;
            for (TeamMatch m : matches) {
                x += autoPoints(m.getPoints());
            }
            return x;
        }

        double avgAuto() {
            return ((double) totalAuto()) / matches();
        }

        int autoRange() {
            int[] scores = new int[matches.size()];
            for (int x = 0; x < scores.length; x++) {
                scores[x] = autoPoints(matches.get(x).getPoints());
            }
            return maxValue(scores) - minValue(scores);
        }

        double autoSD() {
            ArrayList<Integer> points = new ArrayList<>();
            for (TeamMatch t : matches) {
                points.add(autoPoints(t.getPoints()));
            }
            return sd(points);
        }

        int totalTele() {
            int x = 0;
            for (TeamMatch m : matches) {
                x += telePoints(m.getPoints());
            }
            return x;
        }

        double avgTele() {
            return ((double) totalTele()) / matches();
        }

        int teleRange() {
            int[] scores = new int[matches.size()];
            for (int x = 0; x < scores.length; x++) {
                scores[x] = telePoints(matches.get(x).getPoints());
            }
            return maxValue(scores) - minValue(scores);
        }

        double teleSD() {
            ArrayList<Integer> points = new ArrayList<>();
            for (TeamMatch t : matches) {
                points.add(telePoints(t.getPoints()));
            }
            return sd(points);
        }

        int totalClimb() {
            int x = 0;
            for (TeamMatch m : matches) {
                x += climbPoints(m.getPoints());
            }
            return x;
        }

        double avgClimb() {
            return ((double) totalClimb()) / matches();
        }

        double avgClimbTime() {
            double x = 0;
            for (TeamMatch m : matches) {
                if (m.getClimbTime() != -1) {
                    x += m.getClimbTime();
                }
            }
            return x / matches();
        }

        double climbSD() {
            ArrayList<Integer> points = new ArrayList<>();
            for (TeamMatch t : matches) {
                points.add(climbPoints(t.getPoints()));
            }
            return sd(points);
        }

        int totalFoul() {
            int x = 0;
            for (TeamMatch m : matches) {
                x += foulPoints(m.getPoints());
            }
            return x;
        }
    }
}