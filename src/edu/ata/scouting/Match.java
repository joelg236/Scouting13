package edu.ata.scouting;

import java.io.Serializable;
import java.util.Objects;

// Immutable
public final class Match implements Serializable, Comparable<Match> {

    private static final long serialVersionUID = Scouter.serialVersionUID;
    private final Alliance red, blue;
    private final int matchNum;
    private final MatchType matchType;

    public Match(Alliance red, Alliance blue, int matchNum, MatchType matchType) {
        this.red = red;
        this.blue = blue;
        this.matchNum = matchNum;
        this.matchType = matchType;
    }

    public Alliance getRed() {
        return red;
    }

    public Alliance getBlue() {
        return blue;
    }

    public int getMatchNum() {
        return matchNum;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    @Override
    public int compareTo(Match o) {
        int x;
        x = matchType.compareTo(o.matchType);
        if (x == 0) {
            x = Integer.compare(matchNum, o.matchNum);
        }
        return x;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Match) {
            return matchType == ((Match) obj).matchType
                    && matchNum == ((Match) obj).matchNum
                    && blue.equals(((Match) obj).blue)
                    && red.equals(((Match) obj).red);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.red);
        hash = 53 * hash + Objects.hashCode(this.blue);
        hash = 53 * hash + this.matchNum;
        hash = 53 * hash + (this.matchType != null ? this.matchType.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return matchType.shortVersion() + " " + matchNum;
    }

    public static enum MatchType {

        Practice {
            @Override
            public String shortVersion() {
                return "PRCT";
            }
        }, Qualifications {
            @Override
            public String shortVersion() {
                return "QUAL";
            }
        }, Eliminations {
            @Override
            public String shortVersion() {
                return "ELIM";
            }
        };

        public abstract String shortVersion();
    }
}
