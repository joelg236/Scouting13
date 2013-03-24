package edu.ata.scouting;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Scouter {

    public static final long serialVersionUID = 29122L;
    private static JFrame main;

    public static JFrame getMain() {
        if (main == null || !main.isVisible()) {
            throw new NullPointerException("Main window not initialized.");
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
                (main = new MainWindow()).setVisible(true);
            }
        });
    }
}
