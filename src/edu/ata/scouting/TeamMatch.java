package edu.ata.scouting;

// Immutable
import edu.ata.scouting.points.Points;
import java.io.Serializable;

public final class TeamMatch implements Serializable {

    private static final long serialVersionUID = Scouter.serialVersionUID;
    private final Points[] points;
}
