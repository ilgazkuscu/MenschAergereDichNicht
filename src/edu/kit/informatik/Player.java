package edu.kit.informatik;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player {

    public static final int NUMBER_OF_PEGS = 4;
    private static final String RED_START_SPOTS = "SR|AR|BR|CR|DR|[0-39]";
    private static final String GREEN_START_SPOTS = "SG|AG|BG|CG|DG|[0-39]";
    private static final String BLUE_START_SPOTS = "SB|AB|BB|CB|DB|[0-39]";
    private static final String YELLOW_START_SPOTS = "SY|AY|BY|CY|DY|[0-39]";


    private int startIndex;
    private ArrayList<Peg> pegs;

    Player(int startIndex, ListIterator[] iterators) {
        this.startIndex = startIndex;
    }

    Player(int startIndex) {
        this.startIndex = startIndex;
    }

    void createPegs(ListIterator position) {
        pegs.add(new Peg(position));
    }

    public static String getRedStartSpots() {
        return RED_START_SPOTS;
    }

    public static String getGreenStartSpots() {
        return GREEN_START_SPOTS;
    }

    public static String getBlueStartSpots() {
        return BLUE_START_SPOTS;
    }

    public static String getYellowStartSpots() {
        return YELLOW_START_SPOTS;
    }
}
