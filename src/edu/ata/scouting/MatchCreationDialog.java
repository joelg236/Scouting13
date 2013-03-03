package edu.ata.scouting;

import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public abstract class MatchCreationDialog extends JDialog {

    public MatchCreationDialog(Window owner) {
        super(owner, "Create Match");
        setRootPane(new JRootPane());
        setRootPaneCheckingEnabled(true);

        setLayout(LayoutFactory.createLayout());

        LayoutFactory factory = LayoutFactory.newFactory().setInsets(new Insets(2, 3, 2, 3));

        JLabel red = new JLabel("Red");
        red.setFont(red.getFont().deriveFont(Font.BOLD));
        red.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel blue = new JLabel("Blue");
        blue.setFont(red.getFont());
        blue.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField red1 = new JTextField("0");
        JTextField red2 = new JTextField("0");
        JTextField red3 = new JTextField("0");
        JTextField blue1 = new JTextField("0");
        JTextField blue2 = new JTextField("0");
        JTextField blue3 = new JTextField("0");

        JCheckBox eliminations = new JCheckBox("Elimination");

        JLabel matchNumberLabel = new JLabel("Match Number");
        JTextField matchNumber = new JTextField("0");

        add(red, factory.setWidth(2));
        add(blue, factory.setX(2));
        add(red1, factory.setY(1).setX(0));
        add(red2, factory.setY(2));
        add(red3, factory.setY(3));
        add(blue1, factory.setY(1).setX(2));
        add(blue2, factory.setY(2));
        add(blue3, factory.setY(3));
        add(eliminations, factory.setX(0).setY(4));
        add(matchNumberLabel, factory.setX(2).setWeightX(0).setWidth(1));
        add(matchNumber, factory.setX(3));

        JButton done = new JButton("Done");
        JButton cancel = new JButton("Cancel");

        done.addActionListener(new CreateMatch(new JTextField[]{blue1, blue2, blue3},
                new JTextField[]{red1, red2, red3}, eliminations, matchNumber));
        cancel.addActionListener(new Close());

        add(done, factory.setY(5).setX(0).setWidth(2));
        add(cancel, factory.setX(2));

        setSize(450, 250);
        setLocationRelativeTo(Scouter.mainWindow);
    }

    public void showDialog() {
        setVisible(true);
    }

    public abstract void useMatch(Match newMatch);

    private final class CreateMatch implements ActionListener {

        private final JTextField[] blue;
        private final JTextField[] red;
        private final JCheckBox elims;
        private final JTextField num;

        public CreateMatch(JTextField[] blue, JTextField[] red, JCheckBox elims, JTextField match) {
            this.blue = blue;
            this.red = red;
            this.elims = elims;
            this.num = match;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Team blue1 = new Team(Integer.parseInt(blue[0].getText()));
                Team blue2 = new Team(Integer.parseInt(blue[1].getText()));
                Team blue3 = new Team(Integer.parseInt(blue[2].getText()));
                Team red1 = new Team(Integer.parseInt(red[0].getText()));
                Team red2 = new Team(Integer.parseInt(red[1].getText()));
                Team red3 = new Team(Integer.parseInt(red[2].getText()));
                boolean elimination = elims.isSelected();
                int matchNum = Integer.parseInt(num.getText());
                Match match = new Match(new Alliance(blue1, blue2, blue3), new Alliance(red1, red2, red3), elimination, matchNum);
                dispose();
                useMatch(match);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Scouter.mainWindow, ex);
            }
        }
    }

    private final class Close implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
}