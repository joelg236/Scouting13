package edu.ATA;

import edu.ata.scouting.Match;
import edu.ata.scouting.Team;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.JOptionPane;

public class ScoutingDecompiler {

    private static FileOutputStream MATCHES_OUTPUT_STREAM, COMPILED_OUTPUT_STREAM, DATA_OUTPUT_STREAM;
    public final static String scouting = System.getProperty("user.home")
            + System.getProperty("file.separator") + "scouting" + System.getProperty("file.separator");

    private static void init() {
        try {
            File f1 = new File(scouting + "MATCHES.csv");
            if (!f1.exists()) {
                f1.getParentFile().mkdirs();
                f1.createNewFile();
            }
            MATCHES_OUTPUT_STREAM = new FileOutputStream(f1);
            File f2 = new File(scouting + "COMPILED.csv");
            if (!f2.exists()) {
                f2.getParentFile().mkdirs();
                f2.createNewFile();
            }
            COMPILED_OUTPUT_STREAM = new FileOutputStream(f2);
            File f3 = new File(scouting + "DATA.csv");
            if (!f3.exists()) {
                f3.getParentFile().mkdirs();
                f3.createNewFile();
            }
            DATA_OUTPUT_STREAM = new FileOutputStream(f3);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(15);
        }
    }

    public static void main(String[] args) throws IOException {
        init();
        String matchesBanner = "Team Number,Match Number,Robot Type,Starting Position,"
                + "Total Points,Total Autonomous Points,Total Shooting Points,"
                + "Total Pyramid Points,Total Foul Points,Total Shots Made,"
                + "Auto Shots Made,Teleop Shots Made,Auto Two Pointers,Auto Four "
                + "Pointers,Auto Six Pointers,Teleop One Pointers,Teleop Two Pointers,"
                + "Teleop Three Pointers,Ten Climbs,Twenty Climbs,Thirty Climbs,"
                + "Fouls Done,Three Point Fouls,Twenty Point Fouls,Notes,Match Schedule\n";
        String compiledBanner = "Team Number,Offensive Rating,Total Points,Average Points,"
                + "Total Shots Made,Total Autonomous Points,Total Shooting Points,"
                + "Total Pyramid Points,Total Foul Points\n";
        String dataBanner = "Team Number,Matches,Most Common Starting Position,"
                + "Average Points,Total Shots Made,Total Autonomous Points,"
                + "Total Autonomous Shots Made,Total Foul Points,Total Points,"
                + "Total Pyramid Points,Total Shooting Points,Total Teleop Shots "
                + "Made,Average Autonomous Points,Average Shooting Points,Average"
                + " Pyramid Points,Average Foul Points,Autonomous Mean,Autonomous "
                + "Standard Deviation,Most Common Autonomous Shot,Auto Two Pointers,"
                + "Auto Four Pointers,Auto Six Pointers,Teleop Mean,Teleop Standard "
                + "Deviation,Most Common Teleop Shot,Teleop One Pointers,Teleop Two "
                + "Pointers,Teleop Three Pointers,Pyramid Mean,Pyramid Standard "
                + "Deviation,Most Common Climb,Ten Climbs,Twenty Climbs,Thirty "
                + "Climbs,Foul Mean,Foul Standard Deviation,Fouls Commited,Three"
                + " Point Fouls,Twenty Point Fouls\n";
        MATCHES_OUTPUT_STREAM.write(matchesBanner.getBytes());
        COMPILED_OUTPUT_STREAM.write(compiledBanner.getBytes());
        DATA_OUTPUT_STREAM.write(dataBanner.getBytes());

        try {
            matches();
            data();
            compiled();
        } catch (ClassNotFoundException | IOException ex) {
            JOptionPane.showMessageDialog(null, "Error while compiling");
        }
    }

    private static Match[] getMatches() throws IOException, ClassNotFoundException {
        File[] files = new File(scouting).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(".data");
            }
        });
        Match[] matches = new Match[files.length];
        for (int x = 0; x < files.length; x++) {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(files[x]));
            matches[x] = (Match) stream.readObject();
        }
        return matches;
    }

    private static Collection<Team> getTeams() throws IOException, ClassNotFoundException {
        HashMap<Integer, Team> teams = new HashMap<>();
        for (Match match : getMatches()) {
            if (teams.containsKey(match.getTeamNumber())) {
                teams.get(match.getTeamNumber()).addMatch(match);
            } else {
                Team t = new Team(match.getTeamNumber());
                t.addMatch(match);
                teams.put(t.getTeamNumber(), t);
            }
        }
        return teams.values();
    }

    private static void matches() throws IOException, ClassNotFoundException {
        for (Match match : getMatches()) {
            String s =
                    match.getTeamNumber() + ","
                    + match.getMatchNumber() + ","
                    + match.getRobotType() + ","
                    + match.getStartingPosition() + ","
                    + match.getTotalPoints() + ","
                    + match.getTotalAutonomousPoints() + ","
                    + match.getTotalShootingPoints() + ","
                    + match.getTotalPyramidPoints() + ","
                    + match.getTotalFoulPoints() + ","
                    + match.getTotalShotsMade() + ","
                    + match.getAutoShotsMade() + ","
                    + match.getTeleShotsMade() + ","
                    + match.getAutoTwoPointers() + ","
                    + match.getAutoFourPointers() + ","
                    + match.getAutoSixPointers() + ","
                    + match.getTeleOnePointers() + ","
                    + match.getTeleTwoPointers() + ","
                    + match.getTeleThreePointers() + ","
                    + match.getClimbTenPointers() + ","
                    + match.getClimbTwentyPointers() + ","
                    + match.getClimbThirtyPointers() + ","
                    + match.getFoulsDone() + ","
                    + match.getFoulThreePointers() + ","
                    + match.getFoulTwentyPointers() + ","
                    + match.getNotes() + ",";
            s += "\"";
            for (Match.Points point : match.getPoints()) {
                s += "" + point.getPoints() + ",";
            }
            s = s.substring(0, s.length() - 1) + "\"\n";
            MATCHES_OUTPUT_STREAM.write(s.getBytes());
        }
    }

    private static void data() throws IOException, ClassNotFoundException {
        for (Team team : getTeams()) {
            String s = team.getTeamNumber() + ","
                    + team.getMatches() + ","
                    + team.getMostCommonStartingPosition() + ","
                    + team.getAveragePoints() + ","
                    + team.getTotalShotsMade() + ","
                    + team.getTotalAutonomousPoints() + ","
                    + team.getTotalAutonomousShotsMade() + ","
                    + team.getTotalFoulPoints() + ","
                    + team.getTotalPoints() + ","
                    + team.getTotalPyramidPoints() + ","
                    + team.getTotalShootingPoints() + ","
                    + team.getTotalTeleopShotsMade() + ","
                    + team.getAverageAutonomousPoints() + ","
                    + team.getAverageShootingPoints() + ","
                    + team.getAveragePyramidPoints() + ","
                    + team.getAverageFoulPoints() + ","
                    + team.autonomousMean() + ","
                    + team.autonomousStandardDeviation() + ","
                    + team.getMostCommonAutonomousShot() + ","
                    + team.getAutoTwoPointers() + ","
                    + team.getAutoFourPointers() + ","
                    + team.getAutoSixPointers() + ","
                    + team.teleopMean() + ","
                    + team.teleopStandardDeviation() + ","
                    + team.getMostCommonTeleopShot() + ","
                    + team.getTeleopOnePointers() + ","
                    + team.getTeleopTwoPointers() + ","
                    + team.getTeleopThreePointers() + ","
                    + team.pyramidMean() + ","
                    + team.pyramidStandardDeviation() + ","
                    + team.getMostCommonClimb() + ","
                    + team.getTenClimbs() + ","
                    + team.getTwentyClimbs() + ","
                    + team.getThirtyClimbs() + ","
                    + team.foulMean() + ","
                    + team.foulStandardDeviation() + ","
                    + team.getFoulsDone() + ","
                    + team.getThreePointFouls() + ","
                    + team.getTwentyPointFouls() + "\n";
            DATA_OUTPUT_STREAM.write(s.getBytes());
        }
    }

    private static void compiled() throws IOException, ClassNotFoundException {
        for (Team team : getTeams()) {
            String s = team.getTeamNumber() + ","
                    + offensiveRating(team) + ","
                    + team.getTotalPoints() + ","
                    + team.getAveragePoints() + ","
                    + team.getTotalShotsMade() + ","
                    + team.getTotalAutonomousPoints() + ","
                    + team.getTotalShootingPoints() + ","
                    + team.getTotalPyramidPoints() + ","
                    + team.getTotalFoulPoints()
                    + "\n";
            COMPILED_OUTPUT_STREAM.write(s.getBytes());
        }
    }

    private static double offensiveRating(Team t) {
        return 100 * ((((t.autonomousMean() / 15) + (t.teleopMean() / 120) + (t.pyramidMean() / 135))
                * (t.getTotalShotsMade() / t.getMatches()))
                / (t.autonomousStandardDeviation() + t.teleopStandardDeviation() + t.pyramidStandardDeviation()));
    }
}
