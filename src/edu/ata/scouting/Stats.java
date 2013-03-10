package edu.ata.scouting;

import static edu.ata.scouting.DataDecompiling.autoPoints;
import static edu.ata.scouting.DataDecompiling.climbPoints;
import static edu.ata.scouting.DataDecompiling.foulPoints;
import static edu.ata.scouting.DataDecompiling.fullPoints;
import static edu.ata.scouting.DataDecompiling.telePoints;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public class Stats {

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