package edu.ata.scouting.points.climb;

import edu.ata.scouting.Scouter;
import edu.ata.scouting.points.Points;

public class TenPointClimb extends Points.ClimbPoints {

    private static final long serialVersionUID = Scouter.serialVersionUID;

    public TenPointClimb(double climbTime) {
        super(10, climbTime);
    }
}