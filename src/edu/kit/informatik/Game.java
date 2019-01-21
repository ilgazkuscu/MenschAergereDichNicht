package edu.kit.informatik;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

    private static final String[] PLAYER_NAMES = {"red", "green", "blue", "yellow"};
    private static final int PATH_LENGTH = 40;
        private static final String SETTINGS_PATTERN = "(" + Player.getRedStartSpots() + "){4};("
                + Player.getGreenStartSpots() + "){4};(" + Player.getBlueStartSpots() + "){4};("
                + Player.getYellowStartSpots() + "){4}";

    private static Game gameInstance;
    private static HashMap<String, Player> players;
    private static LinkedList<Spot> board;

    static Game getInstance() {
        if (gameInstance == null) {
            gameInstance = new Game();
        }
        return gameInstance;
    }

    static Game getInstance(String settings) {
        if (gameInstance == null) {
            gameInstance = new Game(settings);
        }
        return gameInstance;
    }

    private Game() {
        initializeBoard();
        createPlayers();
    }

    private Game(String settings) throws IllegalArgumentException {
        if (!settings.matches(SETTINGS_PATTERN)) {
            throw new IllegalArgumentException();
        }
        Pattern pattern = Pattern.compile(SETTINGS_PATTERN);
        Matcher matcher = pattern.matcher(settings);
        if (matcher.find() || matcher.groupCount() == 7) {
            String redStart = matcher.group(1);
            String greenStart = matcher.group(3);
            String blueStart = matcher.group(5);
            String yellowStart = matcher.group(7);
        }
        initializeBoard();
        createPlayers();
    }

    private void createPlayers() {
        players = new HashMap<>();
        for (int i = 0; i < PLAYER_NAMES.length; i++) {
            players.put(PLAYER_NAMES[i], new Player(i + (PATH_LENGTH / 4) * 10));
        }
    }

    private void createPlayers(String redStart, String greenStart, String blueStart, String yellowStart) {
        players = new HashMap<>();
        //kırmızı için
        ListIterator[] redIterators = new ListIterator[PLAYER_NAMES.length];
        //sr,sr,sr,sr
        for (int i = 1; i < redIterators.length; i++) {
            String otbok = redStart.substring(0, redStart.indexOf(","));
            if (otbok.matches("[]))
        }

        for (int i = 0; i < PLAYER_NAMES.length; i++) {
            players.put(PLAYER_NAMES[i], new Player(i + (PATH_LENGTH / 4) * 10));
        }
    }

    private void initializeBoard() {
        board = new LinkedList<>();
        for (int i = 0; i < PATH_LENGTH; i++) {
            board.add(new Spot());
        }
    }

}
