package uk.ac.ed.inf.pathfinder;

import uk.ac.ed.inf.ilp.data.LngLat;

public class Node {

    private LngLat currentCoords;
    private Node previousCoords;

    private double gscore;

    private double hscore;


    public Node(LngLat cc, Node pc, double gscore, double hscore) {
        this.currentCoords = cc;
        this.previousCoords = pc;
        this.gscore = gscore;
        this.hscore = hscore;
    }

    public LngLat getCC() {
        return currentCoords;
    }

    public Node getPC() {
        return previousCoords;
    }

    public double getGscore(){
        return gscore;
    }
    public double getHscore(){
        return hscore;
    }

    public void setGscore(double gscore) {
        this.gscore = gscore;
    }

    public void setHscore(double hscore) {
        this.hscore = hscore;
    }
}


