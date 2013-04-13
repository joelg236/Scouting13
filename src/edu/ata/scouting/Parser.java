package edu.ata.scouting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class Parser {

    private final String URL;
    private final Match.MatchType matchType;

    public Parser(String eventID) {
        this(eventID, false);
    }

    public Parser(String eventID, boolean elims) {
        if (!elims) {
            this.URL = "http://www2.usfirst.org/2013comp/Events/" + eventID + "/ScheduleQual.html";
            this.matchType = Match.MatchType.Qualifications;
        } else {
            this.URL = "http://www2.usfirst.org/2013comp/Events/" + eventID + "/ScheduleElim.html";
            this.matchType = Match.MatchType.Eliminations;
        }
    }

    public List<Match> matches() {
        String html = "";
        try {
            URL website = new URL(URL);
            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            html = response.toString();
        } catch (IOException ex) {
            Scouter.showErr(new IOException("Error fetching data", ex));
            return new ArrayList<>();
        }

        html = html.substring(html.indexOf("<TR style=\"background-color:#FFFFFF;\" >"), html.lastIndexOf("<!-- end data -->"));
        html = html.replaceAll("</TR><TR style=\"background-color:#FFFFFF;\" >", "");
        html = html.replaceAll("<TD style=\"font-family:arial;font-weight:normal;font-size:9.0pt\">", "");
        html = html.replaceAll("<TD align=center style=\"font-family:arial;font-weight:normal;font-size:9.0pt\"","");

        StringTokenizer tokenizer = new StringTokenizer(html, ">");

        ArrayList<Match> matches = new ArrayList<>();
        int count = 0;
        int matchNum = 0;
        int red1 = 0, red2 = 0, red3 = 0;
        int blue1 = 0, blue2 = 0, blue3 = 0;
        while (tokenizer.hasMoreElements()) {
            String s = tokenizer.nextToken().replaceAll("</TD", "");
            int x;
            try {
                x = Integer.parseInt(s);

                count++;

                switch (count) {
                    case (1):
                        matchNum = x;
                        break;
                    case (2):
                        red1 = x;
                        break;
                    case (3):
                        red2 = x;
                        break;
                    case (4):
                        red3 = x;
                        break;
                    case (5):
                        blue1 = x;
                        break;
                    case (6):
                        blue2 = x;
                        break;
                    case (7):
                        blue3 = x;
                        count = 0;
                        Alliance red = new Alliance(new Team(red1), new Team(red2), new Team(red3));
                        Alliance blue = new Alliance(new Team(blue1), new Team(blue2), new Team(blue3));
                        Match m = new Match(red, blue, matchNum, matchType);
                        matches.add(m);
                        break;
                }
            } catch (NumberFormatException ex) {
            }
        }
        return matches;
    }
}