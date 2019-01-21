package edu.kit.informatik;

import java.util.ListIterator;

public class Peg {

    private static final int NUMBER_OF_PEGS_PER_PLAYER = 4;

    private ListIterator position;

    public Peg(ListIterator position) {
        this.position = position;
    }
}
