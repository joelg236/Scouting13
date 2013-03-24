package edu.ata.scouting;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.JTextArea;

public final class ScoutView extends JDialog {

    private static final Font buttonFont = new Font("Default", Font.BOLD, 18);
    private final MatchData match;

    public ScoutView(Team team, MatchData match) {
        super(Scouter.getMain(), team + " " + match);
        this.match = match;
        setRootPane(new JRootPane());
        setRootPaneCheckingEnabled(true);
        setLayout(LayoutFactory.createLayout());

        setAlwaysOnTop(true);
        setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        setLocationRelativeTo(null);
    }
}
