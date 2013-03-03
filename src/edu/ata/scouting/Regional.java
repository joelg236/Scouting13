package edu.ata.scouting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Regional implements Serializable {

    private static final long serialVersionUID = Scouter.serialVersionUID;
    private final String name;
    private final ArrayList<Match> matches = new ArrayList<>();

    public Regional(String name) {
        this.name = name;
    }

    public void addMatch(Match match) {
        matches.add(match);
        Collections.sort(matches);
    }

    public void removeMatch(Match match) {
        matches.remove(match);
    }
    
    public String getName() {
        return name;
    }

    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Regional r = new Regional(name);
        for (Match m : matches) {
            r.addMatch(m);
        }
        return r;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Regional)
                ? ((Regional) obj).name.equals(this.name)
                : false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public String toString() {
        return name + " Regional";
    }
}