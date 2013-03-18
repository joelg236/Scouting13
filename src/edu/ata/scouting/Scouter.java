package edu.ata.scouting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public final class Scouter extends JFrame {

    public static final long serialVersionUID = 104L;
    public static Scouter mainWindow;
    public static final String scoutingDir = System.getProperty("user.home")
            + System.getProperty("file.separator") + "scouting" + System.getProperty("file.separator");
    private Regional regional;
    private JScrollPane scrollPane = new JScrollPane();
    public final ArrayList<TeamMatch> matches = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                (mainWindow = new Scouter()).setVisible(true);
            }
        });
    }

    public Scouter() {
        super("Team 4334 Scouting System");

        setRootPane(new JRootPane());
        setRootPaneCheckingEnabled(true);

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");

        JMenuItem save = new JMenuItem("Save Matches");
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        JMenuItem compile = new JMenuItem("Compile All");
        compile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DataDecompiling.compile((Regional) regional.clone(), (ArrayList<TeamMatch>) matches.clone());
                } catch (CloneNotSupportedException ex) {
                    ex.printStackTrace(System.err);
                    JOptionPane.showMessageDialog(Scouter.mainWindow, ex);
                }
            }
        });

        JMenuItem create = new JMenuItem("Create Match");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MatchCreationDialog(mainWindow) {
                    @Override
                    public void useMatch(Match newMatch) {
                        addMatch(newMatch);
                    }
                }.showDialog();
            }
        });

        JMenuItem remove = new JMenuItem("Remove Match");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String r = JOptionPane.showInputDialog("Type match you want to "
                        + "remove in the form of \"Q/E1\", where the first character is"
                        + "'Q' or 'E' (qualifications or eliminations), and the second "
                        + "charater is the match number.");
                try {
                    Match h = null;
                    if (r.startsWith("Q")) {
                        int m = Integer.parseInt(r.substring(1));
                        for (Match match : regional.getMatches()) {
                            if (!match.isEliminations() && match.getMatchNumber() == m) {
                                h = match;
                            }
                        }
                    } else if (r.startsWith("E")) {
                        int m = Integer.parseInt(r.substring(1));
                        for (Match match : regional.getMatches()) {
                            if (match.isEliminations() && match.getMatchNumber() == m) {
                                h = match;
                            }
                        }
                    }
                    regional.removeMatch(h);
                    for (int x = 0; x < matches.size(); x++) {
                        if (matches.get(x).getMatch().equals(h)) {
                            matches.remove(x);
                            break;
                        }
                    }
                    showMatches();
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                    JOptionPane.showMessageDialog(Scouter.mainWindow, ex);
                }
            }
        });

        file.add(save);
        file.add(compile);
        edit.add(create);
        edit.add(remove);

        menuBar.add(file);
        menuBar.add(edit);

        setJMenuBar(menuBar);

        setLayout(new GridLayout(0, 1));
        init();

        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(false);
    }

    private void init() {
        initRegional();
        showMatches();
    }

    private void initRegional() {
        if (regional == null) {
            JFileChooser chooser = new JFileChooser(scoutingDir);
            if (chooser.showDialog(this, "Open Regional") == JFileChooser.APPROVE_OPTION) {
                if (!chooser.getSelectedFile().getPath().contains(".regional")) {
                    JOptionPane.showMessageDialog(Scouter.mainWindow, "Only .regional files can be opened.");
                } else {
                    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()))) {
                        regional = (Regional) inputStream.readObject();
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace(System.err);
                        JOptionPane.showMessageDialog(Scouter.mainWindow, "Error while importing regional");
                    }
                }
            } else {
                regional = new Regional(JOptionPane.showInputDialog("Choose name for new regional"));
            }
            if (regional == null || regional.getName() == null) {
                JOptionPane.showMessageDialog(Scouter.mainWindow, "Regional is needed to run this program.");
                System.exit(10);
            }
        }

        for (File f : new File(scoutingDir).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains("tm");
            }
        })) {
            if (f.getPath().contains(regional.getName())) {
                try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f))) {
                    matches.add((TeamMatch) stream.readObject());
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        }
    }

    public void save() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (TeamMatch match : matches) {
                    File f = new File(scoutingDir + regional.getName() + " - " + match + ".tm");

                    try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(f))) {
                        stream.writeObject(match);
                        System.out.println("Saved match " + match + " in " + f.getPath());
                    } catch (IOException ex) {
                        ex.printStackTrace(System.err);
                        JOptionPane.showMessageDialog(Scouter.mainWindow, ex);
                    }
                }

                File r = new File(scoutingDir + regional.getName() + ".regional");

                try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(r))) {
                    stream.writeObject(regional);
                    System.out.println("Saved regional " + regional + " in " + r.getPath());
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                    JOptionPane.showMessageDialog(Scouter.mainWindow, ex);
                }
            }
        }).start();
    }

    public void addTeamMatch(TeamMatch teamMatch) {
        matches.remove(teamMatch);
        matches.add(teamMatch);
        showMatches();
    }

    public void addMatch(Match match) {
        regional.addMatch(match);
        showMatches();
    }

    public void showMatches() {
        getRootPane().setVisible(false);
        remove(scrollPane);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        for (Match m : regional.getMatches()) {
            panel.add(new MatchDisplay(m));
        }
        scrollPane = new JScrollPane(panel);
        add(scrollPane);
        getRootPane().setVisible(true);
    }

    public final class MatchDisplay extends JPanel {

        public MatchDisplay(Match match) {
            setLayout(new FlowLayout());
            add(new JLabel((match.isEliminations() ? "Elim " : "Qual ") + match.getMatchNumber()));
            for (Team t : match.getRedAlliance().getTeams()) {
                try {
                    if (matches.contains(new TeamMatch(t.getTeamNumber(), match))) {
                        JButton b = new TeamMatchButton(t, match, Color.RED);
                        b.setBackground(Color.LIGHT_GRAY);
                        add(b);
                    } else {
                        JButton b = new TeamMatchButton(t, match, Color.RED);
                        add(b);
                    }
                } catch (NoSuchFieldException ex) {
                    ex.printStackTrace(System.err);
                }
            }
            for (Team t : match.getBlueAlliance().getTeams()) {
                try {
                    if (matches.contains(new TeamMatch(t.getTeamNumber(), match))) {
                        JButton b = new TeamMatchButton(t, match, Color.RED);
                        b.setBackground(Color.LIGHT_GRAY);
                        add(b);
                    } else {
                        JButton b = new TeamMatchButton(t, match, Color.BLUE);
                        add(b);
                    }
                } catch (NoSuchFieldException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        }

        private final class TeamMatchButton extends JButton {

            private final Team team;
            private final Match match;

            public TeamMatchButton(Team team, Match match, Color color) {
                super(String.valueOf(team.getTeamNumber()));
                this.team = team;
                this.match = match;
                setBackground(color);
                setForeground(Color.WHITE);
                addActionListener(new OpenTeamMatch());
            }

            private final class OpenTeamMatch implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        TeamMatch teamMatch = new TeamMatch(team.getTeamNumber(), match);
                        if (matches.contains(teamMatch)) {
                            teamMatch = matches.get(matches.indexOf(teamMatch));
                        }
                        new TeamMatchDisplay(teamMatch).setVisible(true);
                    } catch (NoSuchFieldException ex) {
                        ex.printStackTrace(System.err);
                        JOptionPane.showMessageDialog(Scouter.mainWindow, ex);
                    }
                }
            }
        }
    }
}
