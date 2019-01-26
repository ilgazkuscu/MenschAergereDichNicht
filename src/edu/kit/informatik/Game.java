package edu.kit.informatik;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * Implements the classic Mensch Ärgere Dich Nicht game. The core of the game where most program logic comes together.
 * @author Emre Senliyim
 * @version 1.0
 */
public final class Game {

    private static final String[] PLAYER_NAMES = {"red", "blue", "green", "yellow"};
    private static final int PATH_LENGTH = 40;
    private static Game gameInstance;

    private String possibleMoves;
    private HashMap<String, Player> players;
    private Peg[] gameBoard;
    private int turnCounter;
    private int roll;
    private boolean hasEnded;

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
        hasEnded = false;
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
        hasEnded = false;
    }

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
                + "," + Player.getBlueStartSpots() + "," + Player.getBlueStartSpots() + ","
                + Player.getBlueStartSpots() + ";" + Player.getGreenStartSpots() + "," + Player.getGreenStartSpots()
                + "," + Player.getGreenStartSpots() + "," + Player.getGreenStartSpots() + ";"
                + Player.getYellowStartSpots() + "," + Player.getYellowStartSpots() + "," + Player.getYellowStartSpots()
                + "," + Player.getYellowStartSpots();

        if (!settings.matches(settingsPattern)) {
            return null;
        }

        String[] colorPositions = settings.replaceAll("[S][RGBY]", "").split(";", 4);
        String[][] startPositions = new String[PLAYER_NAMES.length][colorPositions.length];
        Set<String> noDupesPls = new HashSet<>();

        int startCounter = 0;
        for (int i = 0; i < startPositions.length; i++) {
            char playerChar = PLAYER_NAMES[i].toUpperCase().charAt(0);
            int index = 0;
            String pos;
            String moveCheck = "";

            for (int j = 0; j < startPositions[0].length - 1; j++) {
                pos = colorPositions[i].substring(index, colorPositions[i].indexOf(",", index));
                if (pos.equals("")) {
                    pos = "S" + playerChar;
                    startCounter++;
                } else {
                    noDupesPls.add(pos);
                    moveCheck = moveCheck.concat(pos);
                }
                startPositions[i][j] = pos;

                index = colorPositions[i].indexOf(",", index) + 1;
            }
            pos = colorPositions[i].substring(colorPositions[i].lastIndexOf(",") + 1);
            if (pos.equals("")) {
                pos = "S" + playerChar;
                startCounter++;
            } else {
                noDupesPls.add(pos);
                moveCheck = moveCheck.concat(pos);
            }
            startPositions[i][3] = pos;

            if (moveCheck.matches("([ABCD]" + playerChar + "){4}")) {
                return null;
            }
        }

        if (noDupesPls.size() + startCounter == 16) {
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
            public void launchPeg(int position, Peg peg) {
                if (gameBoard[position] != null && gameBoard[position].getOwner() != peg.getOwner()) {
                    gameBoard[position].setHome(true);
                    gameBoard[position].setPosition(4);
                }
                peg.setHome(false);
                peg.setPosition(position);
                gameBoard[position] = peg;
            }
        });
    }

    @Override
    public String toString() {

        String text = "";

        for (String playerName : PLAYER_NAMES) {
            text = text.concat(players.get(playerName).toString() + "\n");
        }

        return text.concat(PLAYER_NAMES[turnCounter]);
    }

    /**
     * Runs after each turn to reevaluate the gameBoard and to determine if anyone has won.
     * @return nothing if the game hasn't ended, " winner" if it has.
     */
    private String checkGameState() {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(PLAYER_NAMES[i]).hasWon()) {
                hasEnded = true;
                return PLAYER_NAMES[turnCounter] + " winner";
            }
        }
        if (roll == 6) {
            return PLAYER_NAMES[turnCounter];
        }
        turnCounter = (turnCounter + 1) % PLAYER_NAMES.length;
        return PLAYER_NAMES[turnCounter];
    }

    /**
     * Called after each valid dice roll, this method generates a list of possible moves that can be made in the current
     * turn.
     * @param roll the number that came out of the dice roll
     * @return the list of possible moves
     */
    String getMoves(int roll) {

        String playerName = String.valueOf(PLAYER_NAMES[turnCounter].toUpperCase().charAt(0));
        String[] endLetters = {"A", "B", "C", "D"};
        String moves = "";
        Player player = players.get(PLAYER_NAMES[turnCounter]);
        int endSpot = player.getEndIndex();
        Peg[] endArea = player.getEndArea();

        if (roll == 6 && player.hasLaunchReady() != null) {
            if (gameBoard[player.getStartIndex()] == null
                    || gameBoard[player.getStartIndex()].getOwner() != turnCounter) {
                moves = moves.concat("S" + playerName + "-" + player.getStartIndex() + "\n");
                this.roll = roll;
                possibleMoves = moves;
                return moves.concat(PLAYER_NAMES[turnCounter]);
            } else if (gameBoard[player.getStartIndex()].getOwner() == turnCounter) {
                //TODO burda işte halledicez bi şekilde rekursiv mi olcak ne olcaks
            }
        }

        for (int i = 0; i < PATH_LENGTH; i++) {
            if (gameBoard[i] != null && gameBoard[i].getOwner() == turnCounter) {
                int target = (i + roll) % PATH_LENGTH;
                if ((target < i ^ ((endSpot - target) < 0 && endSpot > i))) {
                    if ((roll - (endSpot - i) - 1) < endArea.length && endArea[roll - (endSpot - i) - 1] == null) {
                        moves = moves.concat(i + "-" + endLetters[roll - (endSpot - i) - 1] + playerName + "\n");
                    }
                } else if (gameBoard[target] == null || gameBoard[target].getOwner() != turnCounter) {
                    moves = moves.concat(i + "-" + target + "\n");
                }

            }
        }

        if (roll < endArea.length) {
            for (int i = 0; i < endArea.length; i++) {
                if ((i + roll) < endArea.length && endArea[i] != null
                        && endArea[i + roll] == null) {
                    moves = moves.concat(endLetters[i] + playerName + "-"
                            + endLetters[i + roll] + playerName + "\n");
                }
            }
        }

        if (moves.equals("")) {
            turnCounter = (turnCounter + 1) % PLAYER_NAMES.length;
            possibleMoves = null;
        } else {
            this.roll = roll;
            possibleMoves = moves;
        }
        return moves.concat(PLAYER_NAMES[turnCounter]);
    }

    /**
     * returns the player's name
     * @param index the id that will be used as a key to get the name
     * @return the name as a string
     */
    static String getPlayerName(int index) {
        return PLAYER_NAMES[index];
    }

    /**
     * Verifies the move that the user has chosen to make, and executes it if possible.
     * @param choice A String in the form of "XX YY" where XX is the position of the peg to be moved, and YY is where
     *               the peg is supposed to be moved to.
     * @return error message if the choice is invalid, the result of the checkGameState method otherwise
     */
    String executeTheMove(String choice) {
        if (!choice.matches("([SABC][RGBY]|\\w{1,2})\\s([ABCD][RGBY]|\\w{1,2})")) {
            return "Error, invalid move choice!";
        } else if (possibleMoves == null) {
            return "Error, must roll the dice first!";
        }
        String[] posAndTarget = choice.split("\\s", 2);
        /*
        only attempt to execute the move if the user has picked a valid peg to move and a valid spot to move the peg to.
        To make sure of that, the starting position and the target position of the peg are joined together with a "-"
        and the resulting string is compared against the list of possible moves. If it exist there, the move is executed
         */
        Player player = players.get(PLAYER_NAMES[turnCounter]);
        int order = possibleMoves.indexOf(posAndTarget[0] + "-" + posAndTarget[1]);
        if (order != -1) {
            // First it is checked if the user is launching a new peg
            if (posAndTarget[0].charAt(0) == 'S') {
                player.launchAPeg();
                turnCounter = (turnCounter + 1) % PLAYER_NAMES.length;
            } else if (posAndTarget[0].matches("[ABC]" + PLAYER_NAMES[turnCounter].toUpperCase().charAt(0))) {
                // then it is checked if the player is moving a peg within the end area
                switch (posAndTarget[0].charAt(0)) {
                    case 'A':
                        player.moveInsideTheEndArea(0, roll);
                        break;
                    case 'B':
                        player.moveInsideTheEndArea(1, 1 + roll);
                        break;
                    case 'C':
                        player.moveInsideTheEndArea(2, 2 + roll);
                        break;
                    default:
                        break;
                }
            } else if (posAndTarget[0].matches("\\d{1,2}")
                    && posAndTarget[1].matches("[ABCD]" + PLAYER_NAMES[turnCounter].toUpperCase().charAt(0))) {
                int pos = Integer.valueOf(posAndTarget[0]);
                switch (posAndTarget[1].charAt(0)) {
                    case 'A':
                        player.aNewPegHasArrived(0, gameBoard[pos]);
                        gameBoard[pos] = null;
                        break;
                    case 'B':
                        player.aNewPegHasArrived(1, gameBoard[pos]);
                        gameBoard[pos] = null;
                        break;
                    case 'C':
                        player.aNewPegHasArrived(2, gameBoard[pos]);
                        gameBoard[pos] = null;
                        break;
                    case 'D':
                        player.aNewPegHasArrived(3, gameBoard[pos]);
                        gameBoard[pos] = null;
                        break;
                    default:
                        break;
                }
            } else {
                int pos = Integer.valueOf(posAndTarget[0]);
                int target = Integer.valueOf(posAndTarget[1]);
                if (gameBoard[target] != null) {
                    gameBoard[target].setHome(true);
                    gameBoard[target].setPosition(-4);
                }
                gameBoard[target] = gameBoard[pos];
                gameBoard[target].setPosition(target);
                gameBoard[pos] = null;          }
            possibleMoves = null;
            return posAndTarget[1] + "\n" + checkGameState();
        } else {
            return "Error, invalid move choice!";
        }

    }

    /**
     * Resets the game by setting the only existing instance of it to null. Everything gets wiped.
     */
    static void resetGame() {
        gameInstance = null;
    }

    /**
     * @return how long is the path that each piece needs to go before arriving at their destination
     */
    static int getPathLength() {
        return PATH_LENGTH;
    }

    /**
     * @return true if the game is over, false if it continues
     */
    public boolean hasEnded() {
        return hasEnded;
    }
}