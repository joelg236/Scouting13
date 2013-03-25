package edu.ata.scouting.decompiling;

import edu.ata.scouting.MatchData;
import edu.ata.scouting.Scouter;
import edu.ata.scouting.Team;
import edu.ata.scouting.TeamMatch;
import edu.ata.scouting.points.Points;
import edu.ata.scouting.points.auto.FourPointAuto;
import edu.ata.scouting.points.auto.SixPointAuto;
import edu.ata.scouting.points.auto.TwoPointAuto;
import edu.ata.scouting.points.climb.TenPointClimb;
import edu.ata.scouting.points.climb.ThirtyPointClimb;
import edu.ata.scouting.points.climb.TwentyPointClimb;
import edu.ata.scouting.points.tele.OnePointTele;
import edu.ata.scouting.points.tele.ThreePointTele;
import edu.ata.scouting.points.tele.TwoPointTele;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class Decompiler {

    private static final char COMMA = ',';
    private final ArrayList<TeamMatch> matches;
    private final ArrayList<Team> teams;
    private final File listRecapFile;
    private final String matchesName;

    public Decompiler(ArrayList<TeamMatch> matches, String matchesName) {
        this.matches = matches;
        this.matchesName = matchesName;
        this.listRecapFile = new File(Scouter.scoutingDir + matchesName + " Recap.csv");
        Collections.sort(matches);
        teams = new ArrayList<>();
        for (TeamMatch match : matches) {
            if (!teams.contains(match.getTeam())) {
                teams.add(match.getTeam());
            }
        }
    }

    public static ArrayList<TeamMatch> getMatches(Team team, ArrayList<TeamMatch> matches) {
        ArrayList<TeamMatch> m = new ArrayList<>();
        for (TeamMatch t : matches) {
            if (t.getTeam().equals(team)) {
                m.add(t);
            }
        }
        return m;
    }

    public static String getRobotType(ArrayList<TeamMatch> matches) {
        boolean off = false, def = false;
        for (TeamMatch match : matches) {
            if (match.getRobotType() == TeamMatch.RobotType.Offensive) {
                off = true;
            } else if (match.getRobotType() == TeamMatch.RobotType.Defensive) {
                def = true;
            }
        }
        if (off && !def) {
            return "Offense";
        } else if (!off && def) {
            return "Defense";
        } else {
            return "Both";
        }
    }

    public static String getFeeders(ArrayList<TeamMatch> matches) {
        boolean ground = false, feeder = false;
        for (TeamMatch match : matches) {
            if (Arrays.asList(match.getIntakes()).contains(TeamMatch.Intake.GroundPickup)) {
                ground = true;
            } else if (Arrays.asList(match.getIntakes()).contains(TeamMatch.Intake.FeederStation)) {
                feeder = true;
            }
        }
        if (ground && !feeder) {
            return "Ground";
        } else if (!ground && feeder) {
            return "Feeder Station";
        } else {
            return "Both";
        }
    }

    public static String getStartingPositions(ArrayList<TeamMatch> matches) {
        ArrayList<TeamMatch.StartingPosition> positions = new ArrayList<>();
        for (TeamMatch t : matches) {
            if (!positions.contains(t.getStartingPosition()) && t.getStartingPosition() != TeamMatch.StartingPosition.Unknown) {
                positions.add(t.getStartingPosition());
            }
        }
        StringBuilder s = new StringBuilder();
        for (TeamMatch.StartingPosition sp : positions) {
            s.append(sp).append(" | ");
        }
        return s.toString();
    }

    public static String getShooterType(ArrayList<TeamMatch> matches) {
        boolean curved = false, linear = false;
        for (TeamMatch t : matches) {
            if (t.getShooterType() == TeamMatch.ShooterType.Curved) {
                curved = true;
            } else if (t.getShooterType() == TeamMatch.ShooterType.Linear) {
                linear = true;
            }
        }
        if (curved && !linear) {
            return "Curved";
        } else if (!curved && linear) {
            return "Linear";
        } else if (curved && linear) {
            return "Both";
        } else {
            return "None";
        }
    }

    public static int total(TeamMatch match) {
        int x = 0;
        for (Points p : match.getPoints()) {
            x += p.getPoints();
        }
        return x;
    }

    public static int total(MatchData match) {
        int x = 0;
        for (Points p : match.getPoints()) {
            x += p.getPoints();
        }
        return x;
    }

    public static int totalTotal(ArrayList<TeamMatch> matches) {
        int x = 0;
        for (TeamMatch t : matches) {
            x += total(t);
        }
        return x;
    }

    public static int auto(TeamMatch match) {
        int x = 0;
        for (Points p : match.getPoints()) {
            if (p instanceof Points.AutoPoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public static int auto(MatchData match) {
        int x = 0;
        for (Points p : match.getPoints()) {
            if (p instanceof Points.AutoPoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public static int totalAuto(ArrayList<TeamMatch> matches) {
        int x = 0;
        for (TeamMatch t : matches) {
            x += auto(t);
        }
        return x;
    }

    public static int tele(TeamMatch match) {
        int x = 0;
        for (Points p : match.getPoints()) {
            if (p instanceof Points.TelePoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public static int tele(MatchData match) {
        int x = 0;
        for (Points p : match.getPoints()) {
            if (p instanceof Points.TelePoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public static int totalTele(ArrayList<TeamMatch> matches) {
        int x = 0;
        for (TeamMatch t : matches) {
            x += tele(t);
        }
        return x;
    }

    public static int climb(TeamMatch match) {
        int x = 0;
        for (Points p : match.getPoints()) {
            if (p instanceof Points.ClimbPoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public static int climb(MatchData match) {
        int x = 0;
        for (Points p : match.getPoints()) {
            if (p instanceof Points.ClimbPoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public static int totalClimb(ArrayList<TeamMatch> matches) {
        int x = 0;
        for (TeamMatch t : matches) {
            x += climb(t);
        }
        return x;
    }

    public static int commonClimb(ArrayList<TeamMatch> matches) {
        int ten = 0, twenty = 0, thirty = 0;

        for (TeamMatch match : matches) {
            for (Points p : match.getPoints()) {
                if (p instanceof TenPointClimb) {
                    ten++;
                } else if (p instanceof TwentyPointClimb) {
                    twenty++;
                } else if (p instanceof ThirtyPointClimb) {
                    thirty++;
                }
            }
        }

        int max = Math.max(ten, Math.max(twenty, thirty));
        return max == ten ? 10 : (max == twenty ? 20 : (max == thirty ? 30 : 0));
    }

    public static int climbs(ArrayList<TeamMatch> matches) {
        int x = 0;
        for (TeamMatch t : matches) {
            for (Points p : t.getPoints()) {
                if (p instanceof Points.ClimbPoints) {
                    x++;
                }
            }
        }
        return x;
    }

    public static double avgClimbTime(ArrayList<TeamMatch> matches) {
        int climbs = 0;
        double time = 0;
        for (TeamMatch t : matches) {
            for (Points p : t.getPoints()) {
                if (p instanceof Points.ClimbPoints) {
                    climbs++;
                    time += ((Points.ClimbPoints) p).getClimbTime();
                }
            }
        }
        return time / ((double) climbs);
    }

    public static int foul(TeamMatch match) {
        int x = 0;
        for (Points p : match.getPoints()) {
            if (p instanceof Points.FoulPoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public static int foul(MatchData match) {
        int x = 0;
        for (Points p : match.getPoints()) {
            if (p instanceof Points.FoulPoints) {
                x += p.getPoints();
            }
        }
        return x;
    }

    public static int totalFoul(ArrayList<TeamMatch> matches) {
        int x = 0;
        for (TeamMatch t : matches) {
            x += foul(t);
        }
        return x;
    }

    public static int commonAuto(TeamMatch match) {
        int two, four, six;
        two = four = six = 0;

        for (Points p : match.getPoints()) {
            if (p instanceof TwoPointAuto) {
                two++;
            } else if (p instanceof FourPointAuto) {
                four++;
            } else if (p instanceof SixPointAuto) {
                six++;
            }
        }

        int max = Math.max(two, Math.max(four, six));
        return max == six ? 6 : (max == four ? 4 : (max == two ? 2 : 0));
    }

    public static int commonAuto(MatchData match) {
        int two, four, six;
        two = four = six = 0;

        for (Points p : match.getPoints()) {
            if (p instanceof TwoPointAuto) {
                two++;
            } else if (p instanceof FourPointAuto) {
                four++;
            } else if (p instanceof SixPointAuto) {
                six++;
            }
        }

        int max = Math.max(two, Math.max(four, six));
        return max == six ? 6 : (max == four ? 4 : (max == two ? 2 : 0));
    }

    public static int totalCommonAuto(ArrayList<TeamMatch> matches) {
        int two, four, six;
        two = four = six = 0;

        for (TeamMatch match : matches) {
            int common = commonAuto(match);
            if (common == 2) {
                two++;
            } else if (common == 4) {
                four++;
            } else if (common == 6) {
                six++;
            }
        }

        int max = Math.max(two, Math.max(four, six));
        return max == six ? 6 : (max == four ? 4 : (max == two ? 2 : 0));
    }

    public static int commonTele(TeamMatch match) {
        int one, two, three;
        one = two = three = 0;

        for (Points p : match.getPoints()) {
            if (p instanceof OnePointTele) {
                one++;
            } else if (p instanceof TwoPointTele) {
                two++;
            } else if (p instanceof ThreePointTele) {
                three++;
            }
        }

        int max = Math.max(one, Math.max(two, three));
        return max == three ? 3 : (max == two ? 2 : (max == one ? 1 : 0));
    }

    public static int commonTele(MatchData match) {
        int one, two, three;
        one = two = three = 0;

        for (Points p : match.getPoints()) {
            if (p instanceof OnePointTele) {
                one++;
            } else if (p instanceof TwoPointTele) {
                two++;
            } else if (p instanceof ThreePointTele) {
                three++;
            }
        }

        int max = Math.max(one, Math.max(two, three));
        return max == three ? 3 : (max == two ? 2 : (max == one ? 1 : 0));
    }

    public static int totalCommonTele(ArrayList<TeamMatch> matches) {
        int one, two, three;
        one = two = three = 0;

        for (TeamMatch t : matches) {
            if (commonTele(t) == 1) {
                one++;
            } else if (commonTele(t) == 2) {
                two++;
            } else if (commonTele(t) == 3) {
                three++;
            }
        }

        int max = Math.max(one, Math.max(two, three));
        return max == three ? 3 : (max == two ? 2 : (max == one ? 1 : 0));
    }

    public static int wins(ArrayList<TeamMatch> matches) {
        int wins = 0;
        for (TeamMatch t : matches) {
            if (t.getMatchResult() == TeamMatch.MatchResult.Win) {
                wins++;
            }
        }
        return wins;
    }

    public static int losses(ArrayList<TeamMatch> matches) {
        int losses = 0;
        for (TeamMatch t : matches) {
            if (t.getMatchResult() == TeamMatch.MatchResult.Loss) {
                losses++;
            }
        }
        return losses;
    }

    public void decompileAll() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(listRecapFile)) {
            listRecapFile.getParentFile().mkdirs();
            listRecapFile.createNewFile();
            fileOutputStream.write(getAllMatchData().getBytes());
            System.out.println("Wrote to " + listRecapFile.getPath());
        } catch (IOException ex) {
            Scouter.showErr(ex);
        }

        for (Team team : teams) {
            File f = new File(Scouter.scoutingDir + team + " in " + matchesName + ".csv");
            try (FileOutputStream fileOutputStream = new FileOutputStream(f)) {
                f.getParentFile().mkdirs();
                f.createNewFile();
                fileOutputStream.write(teamBio(team).getBytes());
                System.out.println("Wrote to " + f.getPath());
            } catch (IOException ex) {
                Scouter.showErr(ex);
            }
        }
    }

    public String getAllMatchData() {
        StringBuilder builder = new StringBuilder("Match,Team,Match Result,Total Points,"
                + "Auto Points,Auto Discs,Most Common Auto Shot,Teleop Points,"
                + "Most Common Teleop Shot,Climb Points,Foul Points,Ground Intake,"
                + "Feeder Station,Robot Type,Shooter Type,Starting Position" + System.lineSeparator());
        for (TeamMatch tm : matches) {
            builder.append(tm.getMatch()).append(COMMA);
            builder.append(tm.getTeam()).append(COMMA);
            builder.append(tm.getMatchResult()).append(COMMA);
            builder.append(total(tm)).append(COMMA);
            builder.append(auto(tm)).append(COMMA);
            builder.append(tm.getAutoDiscCount()).append(COMMA);
            builder.append(commonAuto(tm)).append(COMMA);
            builder.append(tele(tm)).append(COMMA);
            builder.append(commonTele(tm)).append(COMMA);
            builder.append(climb(tm)).append(COMMA);
            builder.append(foul(tm)).append(COMMA);
            builder.append(Arrays.asList(tm.getIntakes()).contains(TeamMatch.Intake.GroundPickup)).append(COMMA);
            builder.append(Arrays.asList(tm.getIntakes()).contains(TeamMatch.Intake.FeederStation)).append(COMMA);
            builder.append(tm.getRobotType()).append(COMMA);
            builder.append(tm.getShooterType()).append(COMMA);
            builder.append(tm.getStartingPosition()).append(COMMA);
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

    public String teamBio(Team team) {
        StringBuilder builder = new StringBuilder("Record,Avg Points,Robot Type,"
                + "Team Notes,Most Common Auto Discs,Feeders,Starting Positions,"
                + "Avg Auto Points,Avg Shooting Points,Most Common Shot,Shooter Type,Climbs,"
                + "Avg Climb Points,Avg Climb Time,Most Common Climb,Foul Points" + System.lineSeparator());
        builder.append(wins(matches)).append("-").append(losses(matches)).append(COMMA);
        builder.append(((double) totalTotal(matches)) / ((double) matches.size())).append(COMMA);
        builder.append(getRobotType(matches)).append(COMMA);
        for (TeamMatch t : getMatches(team, matches)) {
            if (t.getTeamNotes() != null && !t.getTeamNotes().equals("")) {
                builder.append(t.getTeamNotes()).append(" | ");
            }
        }
        builder.append(COMMA);
        builder.append(totalCommonAuto(matches)).append(COMMA);
        builder.append(getFeeders(matches)).append(COMMA);
        builder.append(getStartingPositions(matches)).append(COMMA);
        builder.append(((double) totalAuto(matches)) / ((double) matches.size())).append(COMMA);
        builder.append(((double) totalTele(matches)) / ((double) matches.size())).append(COMMA);
        builder.append(totalCommonTele(matches)).append(COMMA);
        builder.append(getShooterType(matches)).append(COMMA);
        builder.append(climbs(matches)).append(COMMA);
        builder.append(((double) totalClimb(matches)) / ((double) matches.size())).append(COMMA);
        builder.append(avgClimbTime(matches)).append(COMMA);
        builder.append(commonClimb(matches)).append(COMMA);
        builder.append(totalFoul(matches)).append(COMMA);
        builder.append(System.lineSeparator());

        return builder.toString();
    }
}