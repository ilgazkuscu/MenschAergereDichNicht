package edu.kit.informatik;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    private static final String DICE_ROLL_PATTERN = "[1-6]{1}";

    private Game game;
    private int diceRoll;

    public Controller() {

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
            return "OK";
        } else {
            return "Error, there already is a game in progress!";
        }
    }

    String rollTheDice(String diceRoll) {
        Pattern pattern = Pattern.compile(DICE_ROLL_PATTERN);
        Matcher matcher = pattern.matcher(diceRoll);
        if (!matcher.matches()) {
            return "Error, invalid dice roll!";
        }
        this.diceRoll = Integer.valueOf(diceRoll);
        return "OK";
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
