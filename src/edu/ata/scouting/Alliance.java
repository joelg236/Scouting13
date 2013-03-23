package edu.ata.scouting;

// Immutable
public final class Alliance {

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
}
