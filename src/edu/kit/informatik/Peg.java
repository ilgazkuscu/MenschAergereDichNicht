package edu.kit.informatik;

/**
 * Implements the player pieces in the Mensch Ärgere Dich Nicht game
 */
public class Peg implements Comparable<Peg> {

    private int position;
    private boolean isHome = false;
    private boolean hasArrived = false;
    private int owner;

    /**
     * Constructor that sets the peg's owner and its position. A negative position means the peg is not on the gameBoard
     * 0>position>-4 means the peg is in the end area, and position = -4 means the peg is at home. The boolean vars
     * that describe where the peg is supposed to be are set based on the position value.
     * @param position where the peg is
     * @param owner who owns the peg
     */
    Peg(int position, int owner) {
        this.owner = owner;
        if (position >= 0) {
            this.position = position;
        } else if (position == -5) {
            isHome = true;
            this.position = position * -1;
        } else {
            hasArrived = true;
            this.position = position * -1;
        }
    }

    /**
     * Who owns the peg?
     * @return the owner
     */
    int getOwner() {
        return owner;
    }

    /**
     * Is the peg at home waiting to be put in the game?
     * @return true if it is, false if not.
     */
    boolean isHome() {
        return isHome;
    }

    /**
     * Sets the isHome variable
     * @param home true if the peg is now home, false if it's now out in the game
     */
    void setHome(boolean home) {
        isHome = home;
    }

    /**
     * Where is the peg?
     * @return its position
     */
    int getPosition() {
        return position;
    }

    /**
     * Has the peg made it into the player's end area?
     * @return true if it has, false if not
     */
    boolean hasArrived() {
        return hasArrived;
    }

    /**
     * Sets the peg's new position
     * @param position the new position
     */
    void setPosition(int position) {
        this.position = position;
    }

    /**
     * Flips the hasArrived variable to true, which means the peg has entered the end area
     */
    void setHasArrived() {
        hasArrived = true;
    }

    @Override
    public int compareTo(Peg peg) {

        int thispos;
        if (isHome) {
            thispos = position * -1;
        } else if (hasArrived) {
            thispos = position + 40;
        } else {
            thispos = position;
        }
        int thatpos;
        if (peg.isHome) {
            thatpos = peg.getPosition() * -1;
        } else if (peg.hasArrived) {
            thatpos = peg.getPosition() + 40;
        } else {
            thatpos = peg.getPosition();
        }

        return thispos - thatpos;
    }
}
