package edu.kit.informatik;

/**
 * A basic interface that is used to let other classes perform actions on the gameBoard without having them directly
 * reference the gameBoard itself or the Game class.
 */
public interface PegMoverInterface {
    /**
     * used to put a new piece in the game
     * @param position where the piece will be placed on the board
     * @param peg the peg that will be placed
     */
    void launchPeg(int position, Peg peg);
}
