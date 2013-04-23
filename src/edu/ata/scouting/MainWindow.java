package edu.ata.scouting;

import edu.ata.scouting.decompiling.Decompiler;
import edu.ata.scouting.user.NewMatch;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
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
import javax.swing.SwingConstants;

public final class MainWindow extends JFrame {

    private Team[] ignoredTeams;
    private final HashMap<Match, ArrayList<TeamMatch>> matches = new HashMap<>();
    private final JPanel panel = new JPanel(new GridLayout(0, 8));
    private final JScrollPane pane = new JScrollPane(panel);
    private ScoutView scoutView;
    private String matchListName = "Unknown";

    public MainWindow() {
        super("4334");
        setRootPane(new JRootPane());
        setRootPaneCheckingEnabled(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu stats = new JMenu("Stats");
        JMenu about = new JMenu("About");

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scoutView != null && scoutView.isVisible()) {
                    System.err.println("Did not save because match is in progress");
                    return;
                }
                matchListName = JOptionPane.showInputDialog("What is this match list called?", matchListName);
                if (matchListName == null || matchListName.equals("")) {
                    Scouter.showErr(new NullPointerException("Invalid option"));
                    return;
                }

                try {
                    File matchList = new File(Scouter.scoutingDir + matchListName);
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
                            File f = new File(Scouter.scoutingDir + matchListName + " Matches" + System.getProperty("file.separator") + tm);
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

        JMenuItem saveTo = new JMenuItem("Save to...");
        saveTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser chooser = new JFileChooser();
                chooser.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                            File f = chooser.getSelectedFile();
                            for (ArrayList<TeamMatch> list : matches.values()) {
                                for (TeamMatch tm : list) {
                                    File d = new File(f.getPath() + System.getProperty("file.separator") + tm);
                                    try {
                                        d.createNewFile();
                                        try (FileOutputStream st = new FileOutputStream(d)) {
                                            try (ObjectOutputStream stream = new ObjectOutputStream(st)) {
                                                stream.writeObject(tm);
                                                System.out.println("Saved to " + d.getPath());
                                            }
                                        }
                                    } catch (IOException ex) {
                                        Scouter.showErr(ex);
                                    }
                                }
                            }
                        }
                    }
                });
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.showDialog(Scouter.getMain(), "Save");
            }
        });

        JMenuItem decompile = new JMenuItem("Decompile All");
        decompile.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Decompiler(getAllMatches(), matchListName).decompileAll();
            }
        });

        JMenuItem newMatch = new JMenuItem("New Match");
        newMatch.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewMatch(Scouter.getMain()).setVisible(true);
            }
        });

        JMenuItem removeMatch = new JMenuItem("Remove Match");
        removeMatch.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = JOptionPane.showInputDialog("Enter match name");
                for (Match m : matches.keySet()) {
                    if (m.toString().equals(s)) {
                        matches.remove(m);
                        updateMatches();
                        break;
                    }
                }
            }
        });

        JMenuItem parseQuals = new JMenuItem("Parse from web (Quals)");
        parseQuals.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog("Event ID (Qualifications only)");
                if (id != null) {
                    List<Match> m = new Parser(id).matches();
                    for (Match s : m) {
                        matches.put(s, new ArrayList<TeamMatch>());
                    }
                    updateMatches();
                }
            }
        });

        JMenuItem parseElims = new JMenuItem("Parse from web (Elims)");
        parseElims.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog("Event ID (Eliminations only)");
                if (id != null) {
                    List<Match> m = new Parser(id, true).matches();
                    for (Match s : m) {
                        matches.put(s, new ArrayList<TeamMatch>());
                    }
                    updateMatches();
                }
            }
        });

        JMenuItem predict = new JMenuItem("Predict Matches");
        predict.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<TeamMatch> l = getAllMatches();

                HashMap<Team, Integer> tally = new HashMap<>();
                for (Match m : matches.keySet()) {
                    tally.put(m.getBlue().getTeam1(), 0);
                    tally.put(m.getBlue().getTeam2(), 0);
                    tally.put(m.getBlue().getTeam3(), 0);
                    tally.put(m.getRed().getTeam1(), 0);
                    tally.put(m.getRed().getTeam2(), 0);
                    tally.put(m.getRed().getTeam3(), 0);
                }

                Set<Match> matchList = getMatchList();
                Match[] matches = new Match[matchList.size()];
                matchList.toArray(matches);
                Arrays.sort(matches);

                int start = Integer.parseInt(JOptionPane.showInputDialog("Starting Match"));
                int x = 0;
                for (Match match : matches) {
                    if (++x < start) {
                        continue;
                    }

                    boolean blueWon = false, redWon = false;

                    Alliance blue = match.getBlue();

                    ArrayList<TeamMatch> oneBlue = Decompiler.getMatches(blue.getTeam1(), l);
                    double onepointsBlue = oneBlue.size() > 0
                            ? ((double) Decompiler.totalTotal(oneBlue)) / ((double) oneBlue.size())
                            : 0;

                    ArrayList<TeamMatch> twoBlue = Decompiler.getMatches(blue.getTeam2(), l);
                    double twopointsBlue = twoBlue.size() > 0
                            ? ((double) Decompiler.totalTotal(twoBlue)) / ((double) twoBlue.size())
                            : 0;

                    ArrayList<TeamMatch> threeBlue = Decompiler.getMatches(blue.getTeam3(), l);
                    double threepointsBlue = threeBlue.size() > 0
                            ? ((double) Decompiler.totalTotal(threeBlue)) / ((double) threeBlue.size())
                            : 0;

                    double blueAllianceScore = onepointsBlue + twopointsBlue + threepointsBlue;

                    Alliance red = match.getRed();

                    ArrayList<TeamMatch> oneRed = Decompiler.getMatches(red.getTeam1(), l);
                    double onepointsRed = oneRed.size() > 0
                            ? ((double) Decompiler.totalTotal(oneRed)) / ((double) oneRed.size())
                            : 0;

                    ArrayList<TeamMatch> twoRed = Decompiler.getMatches(red.getTeam2(), l);
                    double twopointsRed = twoRed.size() > 0
                            ? ((double) Decompiler.totalTotal(twoRed)) / ((double) twoRed.size())
                            : 0;

                    ArrayList<TeamMatch> threeRed = Decompiler.getMatches(red.getTeam3(), l);
                    double threepointsRed = threeRed.size() > 0
                            ? ((double) Decompiler.totalTotal(threeRed)) / ((double) threeRed.size())
                            : 0;

                    double redAllianceScore = onepointsRed + twopointsRed + threepointsRed;

                    System.out.println(match
                            + " Red ("
                            + red.getTeam1() + ", " + red.getTeam2() + ", " + red.getTeam3() + ") - "
                            + redAllianceScore
                            + " Blue ("
                            + blue.getTeam1() + ", " + blue.getTeam2() + ", " + blue.getTeam3() + ")- "
                            + blueAllianceScore);

                    for (TeamMatch tm : l) {
                        if (tm.getMatch().equals(match)) {
                            if (tm.getMatchResult() == TeamMatch.MatchResult.Loss) {
                                if (tm.getTeam().equals(blue.getTeam1())
                                        || tm.getTeam().equals(blue.getTeam2())
                                        || tm.getTeam().equals(blue.getTeam3())) {
                                    redWon = true;
                                }
                                if (tm.getTeam().equals(red.getTeam1())
                                        || tm.getTeam().equals(red.getTeam2())
                                        || tm.getTeam().equals(red.getTeam3())) {
                                    blueWon = true;
                                }
                            } else if (tm.getMatchResult() == TeamMatch.MatchResult.Win) {
                                if (tm.getTeam().equals(blue.getTeam1())
                                        || tm.getTeam().equals(blue.getTeam2())
                                        || tm.getTeam().equals(blue.getTeam3())) {
                                    blueWon = true;
                                }
                                if (tm.getTeam().equals(red.getTeam1())
                                        || tm.getTeam().equals(red.getTeam2())
                                        || tm.getTeam().equals(red.getTeam3())) {
                                    redWon = true;
                                }
                            }
                        }
                    }

                    if (blueWon || blueAllianceScore > redAllianceScore) {
                        tally.put(blue.getTeam1(), tally.get(blue.getTeam1()) + 2);
                        tally.put(blue.getTeam2(), tally.get(blue.getTeam2()) + 2);
                        tally.put(blue.getTeam3(), tally.get(blue.getTeam3()) + 2);
                    } else if (redWon || redAllianceScore > blueAllianceScore) {
                        tally.put(red.getTeam1(), tally.get(red.getTeam1()) + 2);
                        tally.put(red.getTeam2(), tally.get(red.getTeam2()) + 2);
                        tally.put(red.getTeam3(), tally.get(red.getTeam3()) + 2);
                    } else {
                        tally.put(blue.getTeam1(), tally.get(blue.getTeam1()) + 1);
                        tally.put(blue.getTeam2(), tally.get(blue.getTeam2()) + 1);
                        tally.put(blue.getTeam3(), tally.get(blue.getTeam3()) + 1);
                        tally.put(red.getTeam1(), tally.get(red.getTeam1()) + 1);
                        tally.put(red.getTeam2(), tally.get(red.getTeam2()) + 1);
                        tally.put(red.getTeam3(), tally.get(red.getTeam3()) + 1);
                    }
                }

                StringBuilder builder = new StringBuilder();
                for (Team t : new TreeMap<>(tally).keySet()) {
                    builder.append(t).append(',').append(tally.get(t)).append('\n');
                }
                System.out.println(builder);
                File output = new File(Scouter.scoutingDir + "Predictions in " + matchListName + ".csv");
                try (FileOutputStream fileOutputStream = new FileOutputStream(output)) {
                    output.getParentFile().mkdirs();
                    output.createNewFile();
                    fileOutputStream.write(builder.toString().getBytes());
                    System.out.println("Wrote to " + output.getPath());
                } catch (IOException ex) {
                    Scouter.showErr(ex);
                }
            }
        });

        JMenuItem version = new JMenuItem("Version");

        version.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Scouter.getMain(), "Version " + Scouter.serialVersionUID);
            }
        });

        file.add(save);
        file.add(saveTo);
        file.add(decompile);
        edit.add(newMatch);
        edit.add(removeMatch);
        edit.add(parseQuals);
        edit.add(parseElims);
        stats.add(predict);
        about.add(version);
        bar.add(file);
        bar.add(edit);
        bar.add(stats);
        bar.add(about);
        setJMenuBar(bar);
        updateMatches();
        add(pane);
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void pack() {
        super.pack();
        setSize(600, 500);
    }

    public void updateMatches() {
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
                List<Team> ignored = Arrays.asList(ignoredTeams);
                boolean ignore = false;
                switch (x) {
                    case (0):
                        if (ignored.contains(current.getBlue().getTeam1())) {
                            buttons[x].setBackground(Color.BLACK);
                            ignore = true;
                        }
                        break;
                    case (1):
                        if (ignored.contains(current.getBlue().getTeam2())) {
                            buttons[x].setBackground(Color.BLACK);
                            ignore = true;
                        }
                        break;
                    case (2):
                        if (ignored.contains(current.getBlue().getTeam3())) {
                            buttons[x].setBackground(Color.BLACK);
                            ignore = true;
                        }
                        break;
                    case (3):
                        if (ignored.contains(current.getRed().getTeam1())) {
                            buttons[x].setBackground(Color.BLACK);
                            ignore = true;
                        }
                        break;
                    case (4):
                        if (ignored.contains(current.getRed().getTeam2())) {
                            buttons[x].setBackground(Color.BLACK);
                            ignore = true;
                        }
                        break;
                    case (5):
                        if (ignored.contains(current.getRed().getTeam3())) {
                            buttons[x].setBackground(Color.BLACK);
                            ignore = true;
                        }
                        break;
                }
                if (!ignore) {
                    buttons[x].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            scoutView = new ScoutView(teams[index], matchData[index]);
                            scoutView.setVisible(true);
                        }
                    });
                }
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

        pack();
    }

    public ScoutView getScoutView() {
        return scoutView;
    }

    public Set<Match> getMatchList() {
        return matches.keySet();
    }

    public ArrayList<TeamMatch> getAllMatches() {
        ArrayList<TeamMatch> all = new ArrayList<>();
        for (ArrayList<TeamMatch> m : matches.values()) {
            all.addAll(m);
        }
        return all;
    }

    public void createMatch(Match match) {
        matches.put(match, new ArrayList<TeamMatch>());
        updateMatches();
    }

    public void putMatch(TeamMatch data) {
        ArrayList<TeamMatch> m = matches.get(data.getMatch());
        ArrayList<TeamMatch> toRemove = new ArrayList<>();
        for (TeamMatch a : m) {
            if (a.getTeam().equals(data.getTeam())) {
                toRemove.add(a);
            }
        }
        for (TeamMatch a : toRemove) {
            m.remove(a);
        }
        matches.get(data.getMatch()).add(data);
        updateMatches();
    }

    // Ugly file stuff
    {
        String subDir = null;

        File dir = new File(Scouter.scoutingDir);
        if (dir.exists() && dir.listFiles() != null) {
            for (File f : dir.listFiles()) {
                if (f.isFile() && !f.getPath().contains(".")) {
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
                        Scouter.showErr(new IOException("Problem loading "
                                + f.getPath() + ", make sure it was created with the same program version", ex));
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

        File ignored = new File(Scouter.scoutingDir + "ignore.txt");
        try {
            String ignore = new Scanner(ignored).useDelimiter("\\A").next();
            StringTokenizer tokenizer = new StringTokenizer(ignore, ",");
            ArrayList<Team> teams = new ArrayList<>();
            while (tokenizer.hasMoreElements()) {
                try {
                    int team = Integer.parseInt(tokenizer.nextToken());
                    teams.add(new Team(team));
                } catch (NumberFormatException ex) {
                }
            }
            ignoredTeams = new Team[teams.size()];
            teams.toArray(ignoredTeams);
        } catch (FileNotFoundException ex) {
            ignoredTeams = new Team[0];
        }
    }
}
