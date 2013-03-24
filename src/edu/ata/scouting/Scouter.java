package edu.ata.scouting;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Scouter {

    public static final long serialVersionUID = 29122L;
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
