package edu.kit.informatik;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Implements the classic Mensch Ärgere Dich Nicht game.
 */
public class Game {

    private static final String[] PLAYER_NAMES = {"red", "green", "blue", "yellow"};
    private static final int PATH_LENGTH = 40;

    private static Game gameInstance;
    private static HashMap<String, Player> players;
    private static Peg[] gameBoard;
    private static int turnCounter;

    /**
     * The Game class is a singleton since per design only one instance of a running game is allowed to exist at any
     * given time. Standard lazy singleton instancing procedure that waits for an instance to get requested before
     * creating it.
     * @return the game instance
     */
    static Game getInstance() {
        if (gameInstance == null) {
            gameInstance = new Game();
        }
        return gameInstance;
    }

    /**
     * The instance getter overload that accepts an initial game state as a parameter.
     * @param settings a string that contains the starting positions of every single piece. Gets parsed and verified
     *                 later.
     * @return the game instance
     */
    static Game getInstance(String settings) {
        if (gameInstance == null) {
            gameInstance = new Game(settings);
        }
        return gameInstance;
    }

    /**
     * Empty constructor for the class.
     * First sets the interface variable for the player class
     * Then initializes the game board
     * Then starts the turn counter
     * Then creates all the players
     */
    private Game() {
        setPlayerPegMover();
        gameBoard = new Peg[PATH_LENGTH];
        turnCounter = 0;
        createPlayers();
    }

    /**
     * Constructor overload. Takes initial game state as a parameter
     * First sets the interface variable for the player class
     * Then initializes the game board
     * Then starts the turn counter
     * Then creates all the players
     * @param settings
     */
    private Game(String settings) {
        setPlayerPegMover();
        gameBoard = new Peg[PATH_LENGTH];
        turnCounter = 0;
        createPlayers(parseSettings(settings));
    }

    /**
     * Creates 4 players with all their pieces at home.
     */
    private void createPlayers() {
        players = new HashMap<>();
        for (int i = 0; i < PLAYER_NAMES.length; i++) {
            players.put(PLAYER_NAMES[i], new Player(i + (PATH_LENGTH / 4) * 10, PLAYER_NAMES[i]));
        }
    }

    /**
     * Overload
     * Creates 4 players with custom starting positions
     * @param startPositions
     */
    private void createPlayers(String[][] startPositions) {
        players = new HashMap<>();
        for (int i = 0; i < PLAYER_NAMES.length; i++) {
            players.put(PLAYER_NAMES[i], new Player(i + (PATH_LENGTH / 4) * 10, startPositions[i], PLAYER_NAMES[i]));
        }
    }

    /**
     * Verifies and processes the initial game state parameter put in by the user.
     * @param settings
     * @return
     */
    private String[][] parseSettings(String settings) {

        String settingsPattern = Player.getRedStartSpots() + "," + Player.getRedStartSpots() + ","
                + Player.getRedStartSpots() + "," + Player.getRedStartSpots() + ";" + Player.getGreenStartSpots()
                + "," + Player.getGreenStartSpots() + "," + Player.getGreenStartSpots() + "," +
                Player.getGreenStartSpots() + ";" + Player.getBlueStartSpots() + "," + Player.getBlueStartSpots()
                + "," + Player.getBlueStartSpots() + "," + Player.getBlueStartSpots() + ";" +
                Player.getYellowStartSpots() + "," + Player.getYellowStartSpots() + "," + Player.getYellowStartSpots()
                + "," + Player.getYellowStartSpots();

        if (!settings.matches(settingsPattern)) {
            //TODO change exception type
            throw new IllegalArgumentException();
        }

        Pattern pattern = Pattern.compile(settingsPattern);
        Matcher matcher = pattern.matcher(settings);
        String[][] startPositions;

        if (matcher.find() || matcher.groupCount() == 16) {
            startPositions = new String[PLAYER_NAMES.length][Player.getNumberOfPegs()];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    startPositions[j][i] = matcher.group((i + 1) + (j * 4));
                }
            }
            return startPositions;
        } else {
            return null;
        }
    }

    private void setPlayerPegMover() {
        Player.setPegMover(new PegMoverInterface() {
            @Override
            public String movePeg(int start, int target) {
                gameBoard[target] = gameBoard[start];
                gameBoard[start] = null;
                return null;            }

            public boolean launchPeg(int position, Peg peg) {
                if (gameBoard[position] != null) {
                    return false;
                } else {
                    gameBoard[position] = peg;
                    return true;
                }
            }

        });
    }

    @Override
    public String toString() {

        String text = "";

        for (String name : players.keySet()) {
            text = text.concat(players.get(name).toString() + "\n");
        }

        return text.concat(PLAYER_NAMES[turnCounter]);
    }


}