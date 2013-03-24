package edu.ata.scouting;

import javax.swing.JDialog;

public final class ScoutView extends JDialog {

    private final Team team;
    private final MatchData matchData;

    public ScoutView(Team team, MatchData data) {
        super(Scouter.getMain(), team + " in " + data.getMatch(), true);
        this.team = team;
        this.matchData = data;

        setSize(400, 400);
    }
}
