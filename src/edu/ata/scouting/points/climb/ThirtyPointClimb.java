package edu.ata.scouting.points.climb;

import edu.ata.scouting.Scouter;
import edu.ata.scouting.points.Points;

public class ThirtyPointClimb extends Points.ClimbPoints {

    private static final long serialVersionUID = Scouter.serialVersionUID;

    public ThirtyPointClimb(double climbTime) {
        super(30, climbTime);
    }
}