package edu.ata.scouting;

import edu.ata.scouting.decompiling.Decompiler;
import edu.ata.scouting.user.NewMatch;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import javax.swing.JButton;
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
import javax.swing.SwingConstants;

public final class MainWindow extends JFrame {

    private final HashMap<Match, ArrayList<TeamMatch>> matches = new HashMap<>();
    private final JPanel panel = new JPanel(new GridLayout(0, 8));
    private final JScrollPane pane = new JScrollPane(panel);
    private String matchListName = "";

    public MainWindow() {
        super("4334");
        setRootPane(new JRootPane());
        setRootPaneCheckingEnabled(true);
        setMinimumSize(new Dimension(500, 0));
        setMaximizedBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(false);

        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("What is this match list called?", matchListName);
                if (name == null || name.equals("")) {
                    Scouter.showErr(new NullPointerException("Invalid option"));
                    return;
                }

                try {
                    File matchList = new File(Scouter.scoutingDir + name);
                    matchList.getParentFile().mkdirs();
                    matchList.createNewFile();
                    try (FileOutputStream matchListStream = new FileOutputStream(matchList)) {
                        ArrayList<Match> m = new ArrayList<>(matches.keySet());
                        try (ObjectOutputStream stream = new ObjectOutputStream(matchListStream)) {
                            stream.writeObject(m);
                        }
                        System.out.println("Saved to " + matchList.getPath());
                    }

                    for (ArrayList<TeamMatch> list : matches.values()) {
                        for (TeamMatch tm : list) {
                            File f = new File(Scouter.scoutingDir + name + " Matches" + System.getProperty("file.separator") + tm);
                            f.getParentFile().mkdirs();
                            f.createNewFile();
                            try (FileOutputStream st = new FileOutputStream(f)) {
                                try (ObjectOutputStream stream = new ObjectOutputStream(st)) {
                                    stream.writeObject(tm);
                                    System.out.println("Saved to " + f.getPath());
                                }
                            }
                        }
                    }
                } catch (IOException ex) {
                    Scouter.showErr(ex);
                }
            }
        });
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

        JMenuItem decompile = new JMenuItem("Decompile All");
        decompile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<TeamMatch> all = new ArrayList<>();
                for(ArrayList<TeamMatch> m : matches.values()) {
                    all.addAll(m);
                }
                new Decompiler(all, matchListName).decompileAll();
            }
        });

        JMenuItem newMatch = new JMenuItem("New Match");
        newMatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewMatch(Scouter.getMain()).setVisible(true);
            }
        });

        file.add(save);
        file.add(decompile);
        edit.add(newMatch);

        bar.add(file);
        bar.add(edit);

        setJMenuBar(bar);

        updateMatches();
    }

    private void updateMatches() {
        panel.removeAll();

        Set<Match> m = matches.keySet();
        Match[] matchList = new Match[m.size()];
        matchList = m.toArray(matchList);
        Arrays.sort(matchList);

        for (Match current : matchList) {
            ArrayList<TeamMatch> teamMatches = matches.get(current);
            final MatchData[] matchData = new MatchData[6];
            for (int x = 0; x < matchData.length; x++) {
                matchData[x] = new MatchData(current);
            }
            final Team[] teams = new Team[6];
            teams[0] = current.getBlue().getTeam1();
            teams[1] = current.getBlue().getTeam2();
            teams[2] = current.getBlue().getTeam3();
            teams[3] = current.getRed().getTeam1();
            teams[4] = current.getRed().getTeam2();
            teams[5] = current.getRed().getTeam3();

            JButton[] buttons = new JButton[6];
            buttons[0] = new JButton(String.valueOf(teams[0].getTeamNumber()));
            buttons[0].setBackground(Color.BLUE);
            buttons[1] = new JButton(String.valueOf(teams[1].getTeamNumber()));
            buttons[1].setBackground(Color.BLUE);
            buttons[2] = new JButton(String.valueOf(teams[2].getTeamNumber()));
            buttons[2].setBackground(Color.BLUE);
            buttons[3] = new JButton(String.valueOf(teams[3].getTeamNumber()));
            buttons[3].setBackground(Color.RED);
            buttons[4] = new JButton(String.valueOf(teams[4].getTeamNumber()));
            buttons[4].setBackground(Color.RED);
            buttons[5] = new JButton(String.valueOf(teams[5].getTeamNumber()));
            buttons[5].setBackground(Color.RED);

            int red = 0, blue = 0;
            for (TeamMatch match : teamMatches) {
                if (match.getTeam().equals(current.getBlue().getTeam1())) {
                    blue += match.getScore();
                    buttons[0].setBackground(Color.GRAY);
                    matchData[0] = new MatchData(match);
                } else if (match.getTeam().equals(current.getBlue().getTeam2())) {
                    blue += match.getScore();
                    buttons[1].setBackground(Color.GRAY);
                    matchData[1] = new MatchData(match);
                } else if (match.getTeam().equals(current.getBlue().getTeam3())) {
                    blue += match.getScore();
                    buttons[2].setBackground(Color.GRAY);
                    matchData[2] = new MatchData(match);
                } else if (match.getTeam().equals(current.getRed().getTeam1())) {
                    red += match.getScore();
                    buttons[3].setBackground(Color.GRAY);
                    matchData[3] = new MatchData(match);
                } else if (match.getTeam().equals(current.getRed().getTeam2())) {
                    red += match.getScore();
                    buttons[4].setBackground(Color.GRAY);
                    matchData[4] = new MatchData(match);
                } else if (match.getTeam().equals(current.getRed().getTeam3())) {
                    red += match.getScore();
                    buttons[5].setBackground(Color.GRAY);
                    matchData[5] = new MatchData(match);
                }
            }

            for (int x = 0; x < 6; x++) {
                final int index = x;
                buttons[x].setForeground(Color.WHITE);
                buttons[x].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ScoutView s = new ScoutView(teams[index], matchData[index]);
                        s.setVisible(true);
                    }
                });
            }

            JLabel score = new JLabel(blue + " - " + red);
            score.setHorizontalAlignment(SwingConstants.CENTER);

            panel.add(new JLabel(current.toString()));
            panel.add(buttons[0]);
            panel.add(buttons[1]);
            panel.add(buttons[2]);
            panel.add(score);
            panel.add(buttons[3]);
            panel.add(buttons[4]);
            panel.add(buttons[5]);
        }

        add(pane);
        pack();
        setLocationRelativeTo(null);
    }

    public void createMatch(Match match) {
        matches.put(match, new ArrayList<TeamMatch>());
        updateMatches();
    }

    public void putMatch(Team team, MatchData data) {
        ArrayList<TeamMatch> m = matches.get(data.getMatch());
        ArrayList<TeamMatch> toRemove = new ArrayList<>();
        for (TeamMatch a : m) {
            if (a.getTeam().equals(team)) {
                toRemove.add(a);
            }
        }
        for (TeamMatch a : toRemove) {
            m.remove(a);
        }
        matches.get(data.getMatch()).add(new TeamMatch(team, data));
        updateMatches();
    }

    // Ugly file stuff
    {
        String subDir = null;

        File dir = new File(Scouter.scoutingDir);
        if (dir.exists() && dir.listFiles() != null) {
            for (File f : dir.listFiles()) {
                if (f.isFile()) {
                    try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f))) {
                        Object o = stream.readObject();

                        if (o instanceof ArrayList) {
                            if (JOptionPane.YES_OPTION
                                    == JOptionPane.showConfirmDialog(this,
                                    "Found match list " + f.getPath() + ", open it?")) {
                                System.out.println("Using match list " + o);
                                matchListName = f.getPath().substring(f.getPath().lastIndexOf(System.getProperty("file.separator")) + 1);
                                for (Match m : (ArrayList<Match>) o) {
                                    matches.put(m, new ArrayList<TeamMatch>());
                                }
                                subDir = f.getPath().substring(f.getPath().lastIndexOf(System.getProperty("file.separator"))) + " Matches";
                                break;
                            }
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        Scouter.showErr(ex);
                    }
                }
            }
        }

        if (subDir != null) {
            File subdir = new File(Scouter.scoutingDir + System.getProperty("file.separator") + subDir);
            if (subdir.exists() && subdir.isDirectory() && subdir.listFiles() != null) {
                for (File f : subdir.listFiles()) {
                    try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f))) {
                        Object o = stream.readObject();

                        if (o instanceof TeamMatch) {
                            ArrayList<TeamMatch> m = matches.get(((TeamMatch) o).getMatch());
                            if (m != null) {
                                m.add((TeamMatch) o);
                                System.out.println("Found completed match " + o);
                            } else {
                                System.out.println(o + " did not exist in the match list");
                            }
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        Scouter.showErr(ex);
                    }
                }
            }
        }
    }
}
