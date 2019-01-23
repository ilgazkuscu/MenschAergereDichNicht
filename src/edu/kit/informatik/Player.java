package edu.kit.informatik;

import java.util.ArrayList;

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
    private int id;


    Player(int startIndex, String[] startPositions, int id) {

        this.id = id;
        pegs = new ArrayList<>();
        this.startIndex = startIndex;
        endArea = new Peg[NUMBER_OF_PEGS];

        for (int i = 0; i < NUMBER_OF_PEGS; i++) {
            Peg peg;

            if (startPositions[i].matches("[0-9]|[1-3][0-9]")) {

                int position = Integer.valueOf(startPositions[i]);
                peg = new Peg(position, id);
                pegs.add(peg);
                pegMover.launchPeg(position, peg);

            } else if (startPositions[i].matches("[S][RGBY]")) {

                peg = new Peg(-4, id);
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

    Player(int startIndex, int id) {
        this.startIndex = startIndex;
        this.id = id;
        pegs = new ArrayList<>();
        endArea = new Peg[NUMBER_OF_PEGS];
        for (int i = 0; i < NUMBER_OF_PEGS; i++) {
            pegs.add(new Peg(-4, id));
        }
    }

    static String getRedStartSpots() {
        return RED_START_SPOTS;
    }

    static String getGreenStartSpots() {
        return GREEN_START_SPOTS;
    }

    static String getBlueStartSpots() {
        return BLUE_START_SPOTS;
    }

    static String getYellowStartSpots() {
        return YELLOW_START_SPOTS;
    }

    static int getNumberOfPegs() {
        return NUMBER_OF_PEGS;
    }

    static void setPegMover(PegMoverInterface pegMover_) {
        pegMover = pegMover_;
    }

    public String toString() {

        String text = "";
        Positions[] position = Positions.values();
        char playerChar = Game.getPlayerName(id).toUpperCase().charAt(0);

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

    Peg hasLaunchReady() {
        int i = 0;
        for (Peg peg : pegs) {
            if (peg.isHome()) {
                return peg;
            }
        }
        Terminal.printLine("evde yok");
        return null;
    }

    int getStartIndex() {
        return startIndex;
    }

    Peg[] getEndArea() {
        return endArea;
    }

    void launchAPeg() {
        pegMover.launchPeg(startIndex, hasLaunchReady());
    }

    void moveInsideTheEndArea(int position, int target) {
        endArea[target] = endArea[position];
        endArea[position] = null;
        endArea[target].setPosition(target + 1);
    }

    boolean hasWon() {
        boolean victory = true;
        for (int i = 0; i < pegs.size() && victory; i++) {
            victory = pegs.get(i).hasArrived();
        }
        return victory;
    }
}
