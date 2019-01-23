package edu.kit.informatik;

/**
 * As the name suggests, the controller class for the game. Manages the interaction between the game logic and the
 * program interface, in addition to doing more low-level operations like starting or ending the game
 * @author Emre Senliyim
 * @version 1.0
 */
public class Controller {

    private Game game;

    /**
     * Empty controller because the game does not need to start until the controller is instructed to do so.
     */
    public Controller() {

    }

    /**
     * Starts the series of events that launches a new game with the default settings, which is where each player
     * starts with all their pieces at home.
     * @return "OK" if the game has been started, error message if there already is one in progress
     */
    public String startGame() {
        if (game == null) {
            game = Game.getInstance();
            return "OK";
        } else {
            return "Error, there already is a game in progress!";
        }
    }

    /**
     * Starts the series of events that launches a new game with the custom settings as specified by the user.
     * @param settings The custom settings as specified by the user.
     * @return "OK" if a new game has been started, relevant error messages in case something's wrong
     */
    public String startGame(String settings) {
        if (game == null) {
            game = Game.getInstance(settings);
            if (game == null) {
                return "Error, invalid start settings!";
            }
            return "OK";
        } else {
            return "Error, there already is a game in progress!";
        }
    }

    /**
     * Rolls the dice. The user gives the result of the dice roll instead of having it randomly generated in code.
     * The method then calls another method that calculates and lists all the possible moves that can be made following
     * that dice roll.
     * @param diceRoll what the user says the dice roll yielded
     * @return the list of all possible legal moves, followed by an indicator that shows who's up
     */
    public String rollTheDice(String diceRoll) {
        if (!diceRoll.matches("[1-6]")) {
            return "Error, invalid dice roll!";
        }
        return game.getMoves(Integer.valueOf(diceRoll));
    }

    /**
     * Sets the game instance to null, which effectively ends the game and removes all data related to it.
     */
    public void resetGame() {
        game = null;
    }

    @Override
    public String toString() {
        if (game == null) {
            return "Error, no game is currently in progress!";
        }
        return game.toString();
    }
}
