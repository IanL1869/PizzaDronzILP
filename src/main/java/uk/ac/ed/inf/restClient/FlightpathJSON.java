package uk.ac.ed.inf.restClient;

/**
 * Represents a drone flight path described in JSON format.
 */
public class FlightpathJSON {

    /**
     * The order number associated with the flight path.
     */
    public String orderNo;

    /**
     * The longitude of the coordinate in the flight path that the drone has come from.
     */
    public double fromLongitude;

    /**
     * The latitude of the coordinate in the flight path that the drone has come from.
     */
    public double fromLatitude;

    /**
     * The angle taken.
     */
    public double angle;

    /**
     * The longitude of the coordinate in the flight path that the drone is going to.
     */
    public double toLongitude;

    /**
     * The latitude of the coordinate in the flight path that the drone is going to.
     */
    public double toLatitude;

    /**
     * Constructs a new FlightpathJSON object with the specified parameters.
     *
     * @param orderNo The order number associated with the flight path.
     * @param fromLongitude The longitude of the coordinate in the flight path that the drone has come from.
     * @param fromLatitude The latitude of the coordinate in the flight path that the drone has come from.
     * @param angle The angle taken.
     * @param toLongitude The longitude of the coordinate in the flight path that the drone is going to.
     * @param toLatitude The latitude of the coordinate in the flight path that the drone is going to.
     */
    public FlightpathJSON(String orderNo, double fromLongitude, double fromLatitude, double angle, double toLongitude, double toLatitude) {
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
    }

    /**
     * Gets the latitude of the starting point of the flight path.
     *
     * @return The latitude of the starting point.
     */
    public double getFromLatitude() {
        return fromLatitude;
    }

    /**
     * Gets the longitude of the coordinate in the flight path that the drone has come from.
     *
     * @return The longitude of the coordinate in the flight path that the drone has come from.
     */
    public double getFromLongitude() {
        return fromLongitude;
    }

    /**
     * Gets the latitude of the coordinate in the flight path that the drone is going to.
     *
     * @return The latitude of the coordinate in the flight path that the drone is going to.
     */
    public double getToLatitude() {
        return toLatitude;
    }

    /**
     * Gets the longitude of the coordinate in the flight path that the drone is going to.
     *
     * @return The longitude of the coordinate in the flight path that the drone is going to.
     */
    public double getToLongitude() {
        return toLongitude;
    }
}
