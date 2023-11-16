package uk.ac.ed.inf.pathfinder;

import uk.ac.ed.inf.ilp.data.LngLat;

public class Point {
    private double angle;
    private LngLat currentCoordinates;
    private Point previousPoint;
    private double gScore;
    private double hScore;
    private String orderNo;

    public Point(double angle, LngLat currentCoordinates, Point pc, double gScore, double hScore, String orderNo) {
        this.angle = angle;
        this.currentCoordinates = currentCoordinates;
        this.previousPoint = pc;
        this.gScore = gScore;
        this.hScore = hScore;
        this.orderNo = orderNo;


    }


    public double getAngle(){return angle;}
    public void setAngle(double angle){this.angle = angle;}
    public LngLat getLngLat() {
        return currentCoordinates;
    }

    public void setCurrentCoordinates(LngLat currentCoordinates) {
        this.currentCoordinates = currentCoordinates;
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

    public String getOrderNo(){return orderNo;}
    public void setOrderNo(String orderNo){
        this.orderNo = orderNo;
    }

}

