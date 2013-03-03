package edu.ata.scouting;

import java.io.Serializable;

public final class Match implements Serializable, Comparable<Match> {

    private static final long serialVersionUID = Scouter.serialVersionUID;
    private final Alliance blue;
    private final Alliance red;
    private final boolean eliminations;
    private final int matchNumber;

    public Match(Alliance blue, Alliance red, boolean eliminations, int matchNumber) {
        this.blue = blue;
        this.red = red;
        this.eliminations = eliminations;
        this.matchNumber = matchNumber;
    }

    public Match blue(Alliance firstAlliance) {
        return new Match(firstAlliance, red, eliminations, matchNumber);
    }

    public Match red(Alliance red) {
        return new Match(blue, red, eliminations, matchNumber);
    }

    public Match eleminations(boolean eliminations) {
        return new Match(blue, red, eliminations, matchNumber);
    }

    public Match matchNumber(int matchNumber) {
        return new Match(blue, red, eliminations, matchNumber);
    }

    public Match regional(String regional) {
        return new Match(blue, red, eliminations, matchNumber);
    }

    public Team getTeam(int team) throws NoSuchFieldException {
        for (Team t : blue.getTeams()) {
            if (t.getTeamNumber() == team) {
                return t;
            }
        }
        for (Team t : red.getTeams()) {
            if (t.getTeamNumber() == team) {
                return t;
            }
        }
        throw new NoSuchFieldException("Team " + team + " does not exist in " + this);
    }

    public Alliance getBlueAlliance() {
        return blue;
    }

    public Alliance getRedAlliance() {
        return red;
    }

    public boolean isEliminations() {
        return eliminations;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Match(blue, red, eliminations, matchNumber);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Match)
                ? (((Match) obj).eliminations == this.eliminations
                && ((Match) obj).matchNumber == this.matchNumber)
                : false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.eliminations ? 1 : 0);
        hash = 17 * hash + this.matchNumber;
        return hash;
    }

    @Override
    public String toString() {
        return "Match " + matchNumber
                + (eliminations ? " of eliminations" : " of qualifications");
    }

    @Override
    public int compareTo(Match o) {
        if(o.isEliminations() && isEliminations()) {
            return Integer.compare(matchNumber, o.matchNumber);
        } else if(o.isEliminations() && !isEliminations()) {
            return -100;
        } else if (!o.isEliminations() && isEliminations()) {
            return 100;
        } else {
            return Integer.compare(matchNumber, o.matchNumber);
        }
    }
}