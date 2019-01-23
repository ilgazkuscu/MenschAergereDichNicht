package edu.kit.informatik;

/**
 * A basic interface that is used to let other classes perform actions on the gameBoard without having them directly
 * reference the gameBoard itself or the Game class.
 */
public interface PegMoverInterface {
    void launchPeg(int position, Peg peg);
}
