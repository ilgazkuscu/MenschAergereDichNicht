package edu.kit.informatik;

class Spot {

    private Peg occupier;

    Spot() {
        occupier = null;
    }

    String occupy(Peg peg) {
        occupier = peg;
        return "OK";
    }

    boolean isOccupied() {
        return (occupier == null);
    }

    Peg getOccupier() {
        return occupier;
    }

    void setOccupier(Peg peg) {
        occupier = peg;
    }

}
