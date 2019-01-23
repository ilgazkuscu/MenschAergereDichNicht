package edu.kit.informatik;


public class Peg {

    private static final int NUMBER_OF_PEGS_PER_PLAYER = 4;

    private int position;
    private boolean isHome = false;
    private boolean hasArrived = false;
    private int owner;

    Peg(int position, int owner) {
        this.owner = owner;
        if (position >= 0) {
            this.position = position;
        } else if (position == -4) {
            isHome = true;
            this.position = position * -1;
        } else {
            hasArrived = true;
            this.position = position * -1;
        }
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    boolean isHome() {
        return isHome;
    }

    public void setHome(boolean home) {
        isHome = home;
    }

    int getPosition() {
        return position;
    }

    boolean hasArrived() {
        return hasArrived;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setHasArrived(boolean hasArrived) {
        this.hasArrived = hasArrived;
    }
}
