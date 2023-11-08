package uk.ac.ed.inf.pathfinder;

import uk.ac.ed.inf.ilp.data.LngLat;

public class Move {

    private String orderNo;
    private double comeFromLon;
    private double comeFromLat;
    private double goToLon;
    private double goToLat;
    private double angle;
    public Move(String orderNo, double comeFromLon, double comeFromLat, double angle, double goToLon, double goToLat){
        this.orderNo = "";
        this.comeFromLon = comeFromLon;
        this.comeFromLat = comeFromLat;
        this.angle = angle;
        this.goToLon = goToLon;
        this.goToLat = goToLat;
    }


}
