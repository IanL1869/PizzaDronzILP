package uk.ac.ed.inf.pathfinder;

import uk.ac.ed.inf.ilp.data.LngLat;

public class Point {
    private double angle;
    private LngLat currentPoint;
    private Point previousPoint;
    private double gScore;
    private double hScore;
    private double costSoFar;

    public Point(double angle, LngLat cc, Point pc, double gscore, double hscore) {
        this.angle = angle;
        this.currentPoint = cc;
        this.previousPoint = pc;
        this.gScore = gscore;
        this.hScore = hscore;
    }



    public LngLat getLngLat() {
        return currentPoint;
    }

    public void setCurrentPoint(LngLat currentPoint) {
        this.currentPoint = currentPoint;
    }

    public Point getPreviousPoint() {
        return previousPoint;
    }

    public void setPreviousPoint(Point previousPoint) {
        this.previousPoint = previousPoint;
    }

    public double getgScore() {
        return gScore;
    }

    public void setgScore(double gScore) {
        this.gScore = gScore;
    }

    public double gethScore() {
        return hScore;
    }

    public void sethScore(double hScore) {
        this.hScore = hScore;
    }

    public double getCostSoFar() {
        return costSoFar;
    }

    public void setCostSoFar(double costSoFar) {
        this.costSoFar = costSoFar;
    }
}

