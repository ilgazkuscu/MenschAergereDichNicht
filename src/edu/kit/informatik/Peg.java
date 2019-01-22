package edu.kit.informatik;


public class Peg {

    private static final int NUMBER_OF_PEGS_PER_PLAYER = 4;

    private int position;
    private boolean isHome = false;
    private boolean hasArrived = false;

    public Peg(int position) {
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

    public boolean isHome() {
        return isHome;
    }

    public void setHome(boolean home) {
        isHome = home;
    }

    public int getPosition() {
        return position;
    }

    public boolean hasArrived() {
        return hasArrived;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setHasArrived(boolean hasArrived) {
        this.hasArrived = hasArrived;
    }
}
