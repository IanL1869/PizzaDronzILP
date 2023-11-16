package uk.ac.ed.inf.restClient;

public class FlightpathJSON {

    private String orderNo;
    private double fromLongitude;
    private double fromLatitude;
    private double angle;
    private double toLongitude;
    private double toLatitude;

    public FlightpathJSON(String orderNo, double fromLongitude, double fromLatitude, double angle, double toLongitude, double toLatitude){
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;

    }
}
