package edu.ATA;

import edu.ata.scouting.Match;
import edu.ata.scouting.MatchDisplay;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

public class Scouting extends JFrame {

    private static final long serialVersionUID = Match.serialVersionUID;
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
                s.setSize(800, 500);
                s.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
                OpenDialog o = new OpenDialog("", JFileChooser.FILES_ONLY, new FileFilter() {
                    private static final long serialVersionUID = Match.serialVersionUID;

                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getAbsolutePath().contains(".data");
                    }

                    @Override
                    public String getDescription() {
                        return ".data Files";
                    }
                }) {
                    private static final long serialVersionUID = Match.serialVersionUID;

                    @Override
                    public void setFile(String path) {
                        try {
                            File file = new File(path);
                            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
                            matchesDisplay.addMatch((Match) inputStream.readObject());
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
        MenuItem compile = new MenuItem("Compile into " + ScoutingDecompiler.scouting);
        compile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ScoutingDecompiler.main(new String[0]);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Error while compiling");
                    ex.printStackTrace(System.err);
                }
            }
        });
        file.add(load);
        file.add(compile);
        bar.add(file);
        setMenuBar(bar);
    }

    private class NewMatchButton extends JButton {

        private static final long serialVersionUID = Match.serialVersionUID;

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

                private static final long serialVersionUID = Match.serialVersionUID;

                public NewMatchDialog() {
                    super(s, "New Match");
                    setRootPaneCheckingEnabled(true);
                    setLayout(new GridBagLayout());
                    JButton create = new JButton("Create match");
                    JLabel teamNumLabel = new JLabel("Team Number");
                    JLabel matchNumLabel = new JLabel("Match Number");
                    final JTextField teamNum = new JTextField();
                    final JTextField matchNum = new JTextField();

                    create.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int team;
                            int match;
                            try {
                                team = teamNum.getText().isEmpty() ? 0 : Integer.parseInt(teamNum.getText());
                                match = matchNum.getText().isEmpty() ? 0 : Integer.parseInt(matchNum.getText());
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(rootPane, "Invalid input");
                                ex.printStackTrace(System.err);
                                return;
                            }
                            matchesDisplay.addMatch(new Match(team, match));
                            dispose();
                        }
                    });
                    GridBagConstraints constraint = new GridBagConstraints();
                    constraint.fill = GridBagConstraints.BOTH;
                    constraint.weighty = 1;
                    add(teamNumLabel, constraint);
                    constraint.gridy = 1;
                    add(matchNumLabel, constraint);
                    constraint.gridy = 0;
                    constraint.gridx = 1;
                    constraint.weightx = 1;
                    constraint.gridwidth = 3;
                    add(teamNum, constraint);
                    constraint.gridy = 1;
                    add(matchNum, constraint);
                    constraint.gridy = 2;
                    constraint.gridx = 0;
                    constraint.gridwidth = 4;
                    add(create, constraint);
                }
            }
        }
    }

    private class MatchesDisplay extends JTabbedPane {

        private static final long serialVersionUID = Match.serialVersionUID;
        private ArrayList<String> tabs = new ArrayList<>();

        public MatchesDisplay() {
            super(TOP, WRAP_TAB_LAYOUT);
            setRootPaneCheckingEnabled(true);
        }

        public void addMatch(final Match match) {
            addTab(match.toString(), new MatchDisplay(match) {
                private static final long serialVersionUID = Match.serialVersionUID;

                @Override
                public void closeWindow() {
                    removeTabAt(tabs.indexOf(match.toString()));
                }
            });
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
    }
}
