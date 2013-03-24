package edu.ata.scouting;

// Immutable
import java.io.Serializable;
import java.util.Objects;

public final class Alliance implements Serializable {

    private static final long serialVersionUID = Scouter.serialVersionUID;
    private final Team t1, t2, t3;

    public Alliance(Team t1, Team t2, Team t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }

    public Team getTeam1() {
        return t1;
    }

    public Team getTeam2() {
        return t2;
    }

    public Team getTeam3() {
        return t3;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Alliance) {
            return t1.equals(((Alliance) obj).t1)
                    && t2.equals(((Alliance) obj).t2)
                    && t3.equals(((Alliance) obj).t3);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.t1);
        hash = 79 * hash + Objects.hashCode(this.t2);
        hash = 79 * hash + Objects.hashCode(this.t3);
        return hash;
    }

    @Override
    public String toString() {
        return "Alliance " + t1 + ", " + t2 + " and " + t3;
    }
}
