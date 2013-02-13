package edu.ATA;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class Scouting extends JFrame {

    static final long serialVersionUID = 81L;
    private static Scouting s;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                }
                s = new Scouting();
                s.setSize(1115, 430);
                s.setLocationRelativeTo(null);
                s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                s.setVisible(true);
            }
        });
    }
    private final GridBagConstraints constraints = new GridBagConstraints();
    private final MatchesDisplay matchesDisplay = new MatchesDisplay();

    public Scouting() {
        super("Scouting App 4334");
        setRootPaneCheckingEnabled(true);
        setLayout(new GridBagLayout());
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.gridx = 0;
        add(new NewMatchButton(), constraints);
        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(matchesDisplay, constraints);

        MenuBar bar = new MenuBar();
        Menu file = new Menu("File");
        MenuItem load = new MenuItem("Open");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenDialog o = new OpenDialog("Open File", "Open", JFileChooser.FILES_ONLY, null) {
                    static final long serialVersionUID = 81L;

                    @Override
                    public void setFile(String path) {
                        try {
                            File file = new File(path);
                            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
                            matchesDisplay.addMatch((TeamMatch) inputStream.readObject());
                        } catch (IOException | ClassNotFoundException ex) {
                            JOptionPane.showMessageDialog(rootPane, "Could not load file. Make sure it is the correct file.");
                        }
                    }
                };
                o.setSize(400, 300);
                o.setLocationRelativeTo(null);
                o.setVisible(true);
            }
        });
        file.add(load);
        bar.add(file);
        setMenuBar(bar);
    }

    private class NewMatchButton extends JButton {

        static final long serialVersionUID = 81L;

        public NewMatchButton() {
            super("New Match");
            addActionListener(new OpenNewMatch());
        }

        private class OpenNewMatch implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                NewMatchDialog dia = new NewMatchDialog();
                dia.setSize(420, 200);
                dia.setLocationRelativeTo(null);
                dia.setVisible(true);
            }

            private class NewMatchDialog extends JDialog {

                static final long serialVersionUID = 81L;

                public NewMatchDialog() {
                    super(s, "New Match");
                    setRootPaneCheckingEnabled(true);
                    setLayout(new GridBagLayout());
                    JButton create = new JButton("Create match");
                    JLabel teamNumLabel = new JLabel("Team Number");
                    JLabel matchNumLabel = new JLabel("Match Number");
                    JLabel robotTypeLabel = new JLabel("Robot Type");
                    final JTextField teamNum = new JTextField();
                    final JTextField matchNum = new JTextField();
                    final JTextField robotType = new JTextField();

                    JButton off = new JButton("Offensive"),
                            def = new JButton("Defensive");
                    off.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            robotType.setText(TeamMatch.OFF);
                        }
                    });
                    def.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            robotType.setText(TeamMatch.DEF);
                        }
                    });
                    create.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int team = 0;
                            int match = 0;
                            String robot = robotType.getText();
                            try {
                                team = teamNum.getText().isEmpty() ? 0 : Integer.parseInt(teamNum.getText());
                                match = matchNum.getText().isEmpty() ? 0 : Integer.parseInt(matchNum.getText());
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(rootPane, "Invalid input");
                                ex.printStackTrace(System.err);
                            }
                            matchesDisplay.addMatch(new TeamMatch(team, match, robot));
                            dispose();
                        }
                    });
                    GridBagConstraints constraint = new GridBagConstraints();
                    constraint.fill = GridBagConstraints.BOTH;
                    constraint.weighty = 1;
                    add(teamNumLabel, constraint);
                    constraint.gridy = 1;
                    add(matchNumLabel, constraint);
                    constraint.gridy = 2;
                    add(robotTypeLabel, constraint);
                    constraint.gridy = 0;
                    constraint.gridx = 1;
                    constraint.weightx = 1;
                    constraint.gridwidth = 3;
                    add(teamNum, constraint);
                    constraint.gridy = 1;
                    add(matchNum, constraint);
                    constraint.gridy = 2;
                    constraint.gridwidth = 1;
                    add(robotType, constraint);
                    constraint.gridx = 2;
                    constraint.weightx = 0;
                    add(off, constraint);
                    constraint.gridx = 3;
                    add(def, constraint);
                    constraint.gridy = 3;
                    constraint.gridx = 0;
                    constraint.gridwidth = 4;
                    add(create, constraint);
                }
            }
        }
    }

    private class MatchesDisplay extends JTabbedPane {

        static final long serialVersionUID = 81L;
        private ArrayList<String> tabs = new ArrayList<>();

        public MatchesDisplay() {
            super(TOP, WRAP_TAB_LAYOUT);
            setRootPaneCheckingEnabled(true);
        }

        public void addMatch(TeamMatch match) {
            addTab(match.toString(), new MatchDisplay(match));
            setSelectedIndex(getTabCount() - 1);
        }

        @Override
        public void addTab(String title, Component component) {
            tabs.add(title);
            super.addTab(title, component);
        }

        @Override
        public void removeTabAt(int index) {
            tabs.remove(index);
            super.removeTabAt(index);
        }

        private class MatchDisplay extends JPanel {

            static final long serialVersionUID = 81L;
            private final TeamMatch match;

            public MatchDisplay(final TeamMatch match) {
                this.match = match;
                setRootPaneCheckingEnabled(true);
                setLayout(new GridBagLayout());

                JTextField robotType = match.getRobotTypeComponent();
                JTextField startingPosition = match.getStartingPositionComponent();
                JFormattedTextField finalScore = match.getFinalScoreComponent();
                JFormattedTextField score = match.getScoreComponent();
                JFormattedTextField pyramids = match.getPyramidPointsComponent();
                JFormattedTextField fouls = match.getFoulsComponent();
                final JTextField notes = new JTextField(match.getNotes());

                JButton undo = new JButton("Undo last action");
                undo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        match.undo();
                    }
                });
                JButton delete = new JButton("Delete Match (do not save)");
                delete.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeTabAt(tabs.indexOf(match.toString()));
                    }
                });
                JButton save = new JButton("Save and Close Match");
                save.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        match.setNotes(notes.getText());
                        File output = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "scouting" + System.getProperty("file.separator") + match.toString() + ".data");
                        try {
                            if (!output.exists()) {
                                output.getParentFile().mkdirs();
                                output.createNewFile();
                            }
                            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(output));
                            outputStream.writeObject(match);
                            removeTabAt(tabs.indexOf(match.toString()));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(rootPane, "Error while saving. Check to make sure " + output.getPath() + " exists.");
                            ex.printStackTrace(System.err);
                        }
                    }
                });

                GridBagConstraints constraints = new GridBagConstraints();
                constraints.fill = GridBagConstraints.BOTH;
                constraints.weighty = 1;

                add(new JLabel("Robot Type"), constraints);
                constraints.gridy = 1;
                add(new JLabel("Starting Position"), constraints);
                constraints.gridy = 3;
                add(new JLabel("Final Score"), constraints);
                constraints.gridy = 4;
                add(new JLabel("Score"), constraints);
                constraints.gridy = 5;
                add(new JLabel("Pyramid Points"), constraints);
                constraints.gridy = 6;
                add(new JLabel("Foul Points"), constraints);
                constraints.gridy = 7;
                add(new JLabel("Notes"), constraints);

                constraints.weightx = 1;
                constraints.gridy = 0;
                constraints.gridx = 1;
                constraints.gridwidth = 4;
                add(robotType, constraints);
                constraints.gridy = 1;
                add(startingPosition, constraints);
                constraints.gridy = 2;
                add(new StartingPositions(), constraints);
                constraints.gridy = 3;
                add(finalScore, constraints);
                constraints.gridy = 4;
                constraints.gridwidth = 1;
                add(score, constraints);
                constraints.gridx = 2;
                add(match.plusOne, constraints);
                constraints.gridx = 3;
                add(match.plusTwo, constraints);
                constraints.gridx = 4;
                add(match.plusThree, constraints);
                constraints.gridx = 1;
                constraints.gridy = 5;
                add(pyramids, constraints);
                constraints.gridx = 2;
                add(match.tenClimb, constraints);
                constraints.gridx = 3;
                add(match.twentyClimb, constraints);
                constraints.gridx = 4;
                add(match.thirtyClimb, constraints);
                constraints.gridx = 1;
                constraints.gridy = 6;
                constraints.gridwidth = 2;
                add(fouls, constraints);
                constraints.gridwidth = 1;
                constraints.gridx = 3;
                add(match.threeFoul, constraints);
                constraints.gridx = 4;
                add(match.twentyFoul, constraints);
                constraints.gridx = 1;
                constraints.gridy = 7;
                constraints.gridwidth = 4;
                add(notes, constraints);

                constraints.gridwidth = 5;
                constraints.gridx = 0;
                constraints.gridy = 8;
                add(undo, constraints);
                constraints.gridy = 9;
                add(save, constraints);
                constraints.gridy = 10;
                add(delete, constraints);
            }

            private class StartingPositions extends JPanel {

                static final long serialVersionUID = 81L;

                public StartingPositions() {
                    setRootPaneCheckingEnabled(true);
                    setLayout(new GridLayout(1, 8));

                    add(match.centreFront);
                    add(match.leftFront);
                    add(match.rightFront);
                    add(match.left);
                    add(match.right);
                    add(match.leftBack);
                    add(match.rightBack);
                    add(match.centreBack);
                }
            }
        }
    }
}
