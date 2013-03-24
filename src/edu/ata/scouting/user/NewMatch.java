package edu.ata.scouting.user;

import edu.ata.scouting.Scouter;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;

public final class NewMatch extends JDialog {

    public NewMatch() {
        super(Scouter.getMain(), "Create new match");
        setRootPaneCheckingEnabled(true);
        setLayout(new GridLayout(0, 2));
        
        add(new JLabel("Blue"));
        add(new JLabel("Red"));
    }
}
