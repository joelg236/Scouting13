package edu.ata.scouting;

import edu.ata.scouting.points.Points;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
        for (ArrayList<TeamMatch> t : teams.values()) {
            Collections.sort(t);
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
        String full = "Team,Intake,Total Score,Average Score,Score Range,Score SD,"
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
        full += stats.intake() + COMMA;
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
}