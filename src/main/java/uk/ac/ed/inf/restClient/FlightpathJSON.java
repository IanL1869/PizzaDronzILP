package uk.ac.ed.inf.restClient;

public class FlightpathJSON {

    public String orderNo;
    public double fromLongitude;
    public double fromLatitude;
    public double angle;
    public double toLongitude;
    public double toLatitude;

    public FlightpathJSON(String orderNo, double fromLongitude, double fromLatitude, double angle, double toLongitude, double toLatitude){
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;

    }

    public double getFromLatitude() {
        return fromLatitude;
    }

    public double getFromLongitude() {
        return fromLongitude;
    }

    public double getToLatitude() {
        return toLatitude;
    }

    public double getToLongitude() {
        return toLongitude;
    }
}
