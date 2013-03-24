package edu.ata.scouting;

// Immutable

import java.io.Serializable;

public final class Match implements Serializable {

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
