package edu.kit.informatik;

public class Controller {

    private static final String DICE_ROLL_PATTERN = "[1-6]";

    private Game game;
    private int diceRoll;

    Controller() {

    }

    String startGame() {
        if (game == null) {
            game = Game.getInstance();
            return "OK";
        } else {
            return "Error, there already is a game in progress!";
        }
    }

    String startGame(String settings) {
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

    String rollTheDice(String diceRoll) {
        if (!diceRoll.matches("[1-6]")) {
            return "Error, invalid dice roll!";
        }
        this.diceRoll = Integer.valueOf(diceRoll);
        return game.getMoves(this.diceRoll);
    }

    void resetGame() {
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
