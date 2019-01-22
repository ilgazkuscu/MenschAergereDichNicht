package edu.kit.informatik;

import java.util.ArrayList;
import java.util.LinkedList;

public class Player {

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
    private ArrayList<Peg> pegs;
    private Peg[] endArea;
    private String name;


    Player(int startIndex, String[] startPositions, String name) {

        this.name = name;
        pegs = new ArrayList<>();
        this.startIndex = startIndex;
        endArea = new Peg[NUMBER_OF_PEGS];

        for (int i = 0; i < NUMBER_OF_PEGS; i++) {
            Peg peg;

            if (startPositions[i].matches("[0-9]|[1-3][0-9]")) {

                int position = Integer.valueOf(startPositions[i]);
                peg = new Peg(position);
                pegs.add(peg);
                pegMover.launchPeg(position, peg);

            } else if (startPositions[i].matches("[S](R|G|B|Y)")) {

                peg = new Peg(-4);
                pegs.add(peg);

            } else {

                switch (startPositions[i].charAt(0)) {
                    case 'A':
                        peg = new Peg(-1);
                        pegs.add(peg);
                        endArea[0] = peg;
                        break;
                    case 'B':
                        peg = new Peg(-2);
                        pegs.add(peg);
                        endArea[1] = peg;
                        break;
                    case 'C':
                        peg = new Peg(-3);
                        pegs.add(peg);
                        endArea[2] = peg;
                        break;
                    case 'D':
                        peg = new Peg(-4);
                        pegs.add(peg);
                        endArea[3] = peg;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    Player(int startIndex, String name) {
        this.startIndex = startIndex;
        this.name = name;
        pegs = new ArrayList<>();
        endArea = new Peg[NUMBER_OF_PEGS];
        for (int i = 0; i < NUMBER_OF_PEGS; i++) {
            pegs.add(new Peg(-4));
        }
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

    public static int getNumberOfPegs() {
        return NUMBER_OF_PEGS;
    }

    public static void setPegMover(PegMoverInterface pegMover_) {
        pegMover = pegMover_;
    }

    public String toString() {

        String text = "";
        Positions position[] = Positions.values();
        char playerChar = name.toUpperCase().charAt(0);

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

}
