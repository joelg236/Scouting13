package edu.ata.scouting;

import edu.ata.scouting.decompiling.Decompiler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Scouter {

    public static final long serialVersionUID = 29823L;
    public static final String scoutingDir = System.getProperty("user.home")
            + System.getProperty("file.separator") + "scouting" + System.getProperty("file.separator");
    private static MainWindow main;

    public static MainWindow getMain() {
        if (main == null) {
            main = new MainWindow();
        }
        return main;
    }

    public static void showErr(final Throwable t) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(getMain(), t);
            }
        });
        t.printStackTrace(System.err);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getMain().setVisible(true);
            }
        });
    }
}
