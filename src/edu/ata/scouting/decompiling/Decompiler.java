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

    private static final char COMMA = ',', QUOTE = '\"';
    private final ArrayList<TeamMatch> matches;
    private final ArrayList<Team> teams;
    private final File listRecapFile;
    private final File allMatches;

    public Decompiler(ArrayList<TeamMatch> matches, String matchesName) {
        this.matches = matches;
        this.listRecapFile = new File(Scouter.scoutingDir + matchesName + " Recap.csv");
        this.allMatches = new File(Scouter.scoutingDir + "Teams in " + matchesName + ".csv");
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
        } else if(off && def) {
            return "Both";
        } else {
            return "None";
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
        } else if (ground && feeder) {
            return "Both";
        } else {
            return "None";
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
        return climbs == 0 ? 0 : time / ((double) climbs);
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
    
    public static int drivetrainRating(ArrayList<TeamMatch> matches) {
        int sum = 0;
        for(TeamMatch t : matches) {
            sum += t.getDrivetrainRating();
        }
        return Math.round((float) (((double) sum) / ((double) matches.size())));
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

        try (FileOutputStream fileOutputStream = new FileOutputStream(allMatches)) {
            allMatches.getParentFile().mkdirs();
            allMatches.createNewFile();
            fileOutputStream.write(teams(teams).getBytes());
            System.out.println("Wrote to " + allMatches.getPath());
        } catch (IOException ex) {
            Scouter.showErr(ex);
        }
    }

    public String getAllMatchData() {
        StringBuilder builder = new StringBuilder("Match,Team,Match Result,Total Points,"
                + "Auto Points,Auto Discs,Most Common Auto Shot,Teleop Points,"
                + "Most Common Teleop Shot,Climb Points,Foul Points,Ground Intake,"
                + "Feeder Station,Drivetrain Rating,Robot Type,Shooter Type,Starting Position" 
                + System.lineSeparator());
        for (TeamMatch tm : matches) {
            builder.append(QUOTE).append(tm.getMatch()).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(tm.getTeam()).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(tm.getMatchResult()).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(total(tm)).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(auto(tm)).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(tm.getAutoDiscCount()).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(commonAuto(tm)).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(tele(tm)).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(commonTele(tm)).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(climb(tm)).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(foul(tm)).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(Arrays.asList(tm.getIntakes()).contains(TeamMatch.Intake.GroundPickup)).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(Arrays.asList(tm.getIntakes()).contains(TeamMatch.Intake.FeederStation)).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(tm.getDrivetrainRating()).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(tm.getRobotType()).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(tm.getShooterType()).append(QUOTE).append(COMMA);
            builder.append(QUOTE).append(tm.getStartingPosition()).append(QUOTE).append(COMMA);
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

    public String teams(ArrayList<Team> teams) {
        StringBuilder builder = new StringBuilder("Team,Record,Avg Points,Robot Type,"
                + "Team Notes,Drivetrain Rating,Most Common Auto Discs,Feeders,Starting Positions,"
                + "Avg Auto Points,Avg Shooting Points,Most Common Shot,Shooter Type,Climbs,"
                + "Avg Climb Points,Avg Climb Time,Most Common Climb,Foul Points" + System.lineSeparator());
        for (Team t : teams) {
            builder.append(teamBio(t));
        }

        return builder.toString();
    }

    public String teamBio(Team team) {
        StringBuilder builder = new StringBuilder();
        ArrayList<TeamMatch> teamsMatches = getMatches(team, matches);
        builder.append(QUOTE).append(team).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(wins(teamsMatches)).append("-").append(losses(teamsMatches)).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(((double) totalTotal(teamsMatches)) / ((double) teamsMatches.size())).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(getRobotType(teamsMatches)).append(QUOTE).append(COMMA);
        builder.append(QUOTE);
        for (TeamMatch t : teamsMatches) {
            if (t.getTeamNotes() != null && !t.getTeamNotes().equals("")) {
                builder.append(t.getTeamNotes()).append(" | ");
            }
        }
        builder.append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(drivetrainRating(teamsMatches)).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(totalCommonAuto(teamsMatches)).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(getFeeders(teamsMatches)).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(getStartingPositions(teamsMatches)).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(((double) totalAuto(teamsMatches)) / ((double) teamsMatches.size())).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(((double) totalTele(teamsMatches)) / ((double) teamsMatches.size())).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(totalCommonTele(teamsMatches)).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(getShooterType(teamsMatches)).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(climbs(teamsMatches)).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(((double) totalClimb(teamsMatches)) / ((double) teamsMatches.size())).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(avgClimbTime(teamsMatches)).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(commonClimb(teamsMatches)).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(totalFoul(teamsMatches)).append(QUOTE).append(COMMA);
        builder.append(QUOTE).append(System.lineSeparator());

        return builder.toString();
    }
}