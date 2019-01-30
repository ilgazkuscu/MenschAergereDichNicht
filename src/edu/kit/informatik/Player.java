package edu.kit.informatik;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Implements Players that take part in the Mensch Ärgere Dich Nicht game
 * @author Emre Senliyim
 * @version 1.0
 */
public class Player {

    /**
     * The enum that holds the 'names' of all the points in the end area
     */
    private enum Positions {
        A, B, C, D
    }

    private static final int NUMBER_OF_PEGS = 4;
    private static final String RED_START_SPOTS = "(SR|AR|BR|CR|DR|[0-9]|[1-3][0-9])";
    private static final String GREEN_START_SPOTS = "(SG|AG|BG|CG|DG|[0-9]|[1-3][0-9])";
    private static final String BLUE_START_SPOTS = "(SB|AB|BB|CB|DB|[0-9]|[1-3][0-9])";
    private static final String YELLOW_START_SPOTS = "(SY|AY|BY|CY|DY|[0-9]|[1-3][0-9])";

    private static PegMoverInterface pegMover;
    private int startIndex;
    private int endIndex;
    private ArrayList<Peg> pegs;
    private Peg[] endArea;
    private int id;


    /**
     * The "loaded" constructor that runs when the game is being launched with custom start settings.
     * @param startIndex the spot on the gameBoard where this player's pieces will land when they are launched
     * @param startPositions an array that specifies where each peg will start the game
     * @param id the player's ID, which is passed onto its pegs to signify ownership
     */
    Player(int startIndex, String[] startPositions, int id) {

        this.id = id;
        pegs = new ArrayList<>();
        this.startIndex = startIndex;
        endIndex = (startIndex + Game.getPathLength() - 1) % Game.getPathLength();
        endArea = new Peg[NUMBER_OF_PEGS];

        for (int i = 0; i < NUMBER_OF_PEGS; i++) {
            Peg peg;

            if (startPositions[i].matches("[0-9]|[1-3][0-9]")) {

                int position = Integer.valueOf(startPositions[i]);
                peg = new Peg(position, id);
                pegs.add(peg);
                pegMover.launchPeg(position, peg);

            } else if (startPositions[i].matches("[S][RGBY]")) {

                peg = new Peg(-5, id);
                pegs.add(peg);

            } else {

                switch (startPositions[i].charAt(0)) {
                    case 'A':
                        peg = new Peg(-1, id);
                        pegs.add(peg);
                        endArea[0] = peg;
                        break;
                    case 'B':
                        peg = new Peg(-2, id);
                        pegs.add(peg);
                        endArea[1] = peg;
                        break;
                    case 'C':
                        peg = new Peg(-3, id);
                        pegs.add(peg);
                        endArea[2] = peg;
                        break;
                    case 'D':
                        peg = new Peg(-4, id);
                        pegs.add(peg);
                        endArea[3] = peg;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * The "empty" constructor that runs when the game is being launched with the default start settings where every
     * player starts with all their pieces at home
     * @param startIndex the spot on the gameBoard where this player's pieces will land when they are launched
     * @param id the player's ID, which is passed onto its pegs to signify ownership
     */
    Player(int startIndex, int id) {
        this.startIndex = startIndex;
        this.id = id;
        pegs = new ArrayList<>();
        endArea = new Peg[NUMBER_OF_PEGS];
        for (int i = 0; i < NUMBER_OF_PEGS; i++) {
            pegs.add(new Peg(-5, id));
        }
    }

    /**
     * Returns a regular expression that specifies where a red piece can start a game
     * @return a regular expression that specifies where a red piece can start a game
     */
    static String getRedStartSpots() {
        return RED_START_SPOTS;
    }

    /**
     * Returns a regular expression that specifies where a green piece can start a game
     * @return a regular expression that specifies where a green piece can start a game
     */
    static String getGreenStartSpots() {
        return GREEN_START_SPOTS;
    }

    /**
     * Returns a regular expression that specifies where a blue piece can start a game
     * @return a regular expression that specifies where a blue piece can start a game
     */
    static String getBlueStartSpots() {
        return BLUE_START_SPOTS;
    }

    /**
     * Returns a regular expression that specifies where a yellow piece can start a game
     * @return a regular expression that specifies where a yellow piece can start a game
     */
    static String getYellowStartSpots() {
        return YELLOW_START_SPOTS;
    }

    /**
     * Sets the PegMoverInterface instance that provides communication between this class and another class.
     * @param newPegMover the interface instance that was passed from another class
     */
    static void setPegMover(PegMoverInterface newPegMover) {
        pegMover = newPegMover;
    }

    /**
     * Generates a String that lists all the pieces' locations
     * @return a String that lists all the pieces' locations
     */
    public String toString() {

        String text = "";
        Positions[] position = Positions.values();
        char playerChar = Game.getPlayerName(id).toUpperCase().charAt(0);
        Collections.sort(pegs);

        for (Peg peg : pegs) {

            if (peg.isHome()) {
                text = text.concat("S" + playerChar + ",");
            } else if (peg.hasArrived()) {
                text = text.concat(position[peg.getPosition() - 1].toString() + playerChar + ",");
            } else {
                text = text.concat(peg.getPosition() + ",");
            }
        }
        return text.substring(0, text.length() - 1);
    }

    /**
     * Checks if the player has any pieces that are not in the game
     * @return a peg object if there are any, null if not
     */
    Peg hasLaunchReady() {
        for (Peg peg : pegs) {
            if (peg.isHome()) {
                return peg;
            }
        }
        Terminal.printLine("evde yok");
        return null;
    }

    /**
     * Returns the spot where the player's pieces start the game
     * @return the spot where the player's pieces start the game
     */
    int getStartIndex() {
        return startIndex;
    }

    /**
     * Returns the 4-long array that is the player's final 4 spots that they need to fill with pieces
     * @return the 4-long array that is the player's final 4 spots
     */
    Peg[] getEndArea() {
        return endArea;
    }

    /**
     * Launches a new piece into the game. The piece is picked from the home area and put in the player's specific
     * starting position
     */
    void launchAPeg() {
        pegMover.launchPeg(startIndex, hasLaunchReady());
    }

    /**
     * Moves the pieces inside the end area
     * @param position From where
     * @param target to where
     */
    void moveInsideTheEndArea(int position, int target) {
        endArea[target] = endArea[position];
        endArea[position] = null;
        endArea[target].setPosition(target + 1);
    }

    /**
     * Called when a piece has finished going around the board and entered the end area
     * @param position the position within the end area, on which the piece has landed
     * @param newcomer the piece that has just finished a lap
     */
    void aNewPegHasArrived(int position, Peg newcomer) {
        endArea[position] = newcomer;
        newcomer.setHasArrived();
        newcomer.setPosition(position + 1);
    }

    /**
     * Checks if the player has all their pieces in the end area, meaning they have won
     * @return true if they have all their pieces in the end area, false if not
     */
    boolean hasWon() {
        boolean victory = true;
        for (int i = 0; i < pegs.size() && victory; i++) {
            victory = pegs.get(i).hasArrived();
        }
        return victory;
    }

    /**
     * @return the spot where the gameBoard ends and the end area begins for this player
     */
    int getEndIndex() {
        return endIndex;
    }

}
