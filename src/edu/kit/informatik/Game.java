package edu.kit.informatik;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Implements the classic Mensch Ärgere Dich Nicht game.
 */
public class Game {

    private static final String[] PLAYER_NAMES = {"red", "blue", "green", "yellow"};
    private static final int PATH_LENGTH = 40;

    private static String possibleMoves;
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

        String[][] startPositions = parseSettings(settings);
        if (startPositions == null) {
            return null;
        }
        if (gameInstance == null) {
            gameInstance = new Game(startPositions);
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
     * @param startPositions a 2D array that contains the starting positions of all individual players
     */
    private Game(String[][] startPositions) {
        setPlayerPegMover();
        gameBoard = new Peg[PATH_LENGTH];
        turnCounter = 0;
        createPlayers(startPositions);
    }

    /**
     * Creates 4 players with all their pieces at home.
     */
    private void createPlayers() {
        players = new HashMap<>();
        for (int i = 0; i < PLAYER_NAMES.length; i++) {
            players.put(PLAYER_NAMES[i], new Player(i * (PATH_LENGTH / 4), i));
        }
    }

    /**
     * Overload
     * Creates 4 players with custom starting positions
     * @param startPositions a 2D array that contains all the individual starting positions of every single player piece
     */
    private void createPlayers(String[][] startPositions) {
        players = new HashMap<>();
        for (int i = 0; i < PLAYER_NAMES.length; i++) {
            players.put(PLAYER_NAMES[i], new Player(i * (PATH_LENGTH / 4), startPositions[i], i));
        }
    }

    /**
     * Verifies and processes the initial game state parameter put in by the user. A settings parameter will be deemed
     * invalid if it
     * - does not match the expected input pattern
     * - describes a game that has no playable moves left
     * - has two starting positions pointing at the same spot that is not a player's home area
     * @param settings The raw settings string as given by the player.
     * @return a 2D array that contains all the individual starting positions of every single player piece if the input
     * argument is valid, a null object if it is not.
     */
    private static String[][] parseSettings(String settings) {

        String settingsPattern = Player.getRedStartSpots() + "," + Player.getRedStartSpots() + ","
                + Player.getRedStartSpots() + "," + Player.getRedStartSpots() + ";" + Player.getBlueStartSpots()
                + "," + Player.getBlueStartSpots() + "," + Player.getBlueStartSpots() + "," +
                Player.getBlueStartSpots() + ";" + Player.getGreenStartSpots() + "," + Player.getGreenStartSpots()
                + "," + Player.getGreenStartSpots() + "," + Player.getGreenStartSpots() + ";" +
                Player.getYellowStartSpots() + "," + Player.getYellowStartSpots() + "," + Player.getYellowStartSpots()
                + "," + Player.getYellowStartSpots();


        Pattern pattern = Pattern.compile(settingsPattern);
        Matcher matcher = pattern.matcher(settings);
        String[][] startPositions;

        if (matcher.find() && matcher.groupCount() == PLAYER_NAMES.length * Player.getNumberOfPegs()) {
            startPositions = new String[PLAYER_NAMES.length][Player.getNumberOfPegs()];
            for (int i = 0; i < PLAYER_NAMES.length; i++) {
                for (int j = 0; j < Player.getNumberOfPegs(); j++) {
                    startPositions[j][i] = matcher.group((i + 1) + (j * PLAYER_NAMES.length));
                }
            }

            for (int i = 0; i < startPositions.length; i++) {
                int endFlag = 0;
                String previous = "";
                for (String pos : startPositions[i]) {
                    if (pos.charAt(0) == 'A' || pos.charAt(0) == 'B' || pos.charAt(0) == 'C' || pos.charAt(0) == 'D') {
                        endFlag++;
                    }
                    if (pos.charAt(0) != 'S' && pos.equals(previous)) {
                        return null;
                    }
                    previous = pos;
                }
                if (endFlag == 4) {
                    return null;
                }
            }
            return startPositions;
        } else {
            return null;
        }
    }

    /**
     * Sets the peg mover interface instance of the Player class, so that it can communicate with this class and
     * perform actions on gameBoard without referencing this class or its elements.
     */
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

    /**
     * Runs after each turn to reevaluate the gameBoard and to determine if anyone has won.
     * @return
     */
    private static String checkGameState() {
        return null;
    }

    /**
     * Called after each valid dice roll, this method generates a list of possible moves that can be made in the current
     * turn.
     * @param roll the number that came out of the dice roll
     * @return the list of possible moves
     */
    String getMoves(int roll) {


        String moves = "";
        String playerName = PLAYER_NAMES[turnCounter];
        Player player = players.get(playerName);
        char[] endLetters = {'A', 'B', 'C', 'D'};
        int endSpot = (player.getStartIndex() + PATH_LENGTH - 1) % PATH_LENGTH;

        if (roll == 6 && player.hasLaunchReady() != null &&
                (gameBoard[player.getStartIndex()] == null ||
                        gameBoard[player.getStartIndex()].getOwner() != turnCounter)) {
            moves = moves.concat("S" + playerName.toUpperCase().charAt(0) + "-" + player.getStartIndex() + "\n");
        }

        for (int i = 0; i < PATH_LENGTH; i++) {
            int target = (i + roll) % PATH_LENGTH;
            if (gameBoard[i] != null && gameBoard[i].getOwner() == turnCounter) {
                // is in range of the end area
                if (target > endSpot && player.checkEndArea(endSpot - target - 1) == null) {
                    moves = moves.concat(i + "-" + endLetters[roll - (endSpot - target) + 1] +
                            playerName.toUpperCase().charAt(0) + "\n");
                    break;
                }
                // different owner or an empty cell, AND not in range of the end area
                if (gameBoard[target] == null || gameBoard[target].getOwner() != turnCounter) {
                    moves = moves.concat(i + "-" + target + "\n");
                }
            }
        }
        possibleMoves = moves;
        return moves.concat(playerName);
    }

    static String getPlayerName(int index) {
        return PLAYER_NAMES[index];
    }

}