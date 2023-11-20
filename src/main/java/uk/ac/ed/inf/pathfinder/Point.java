package uk.ac.ed.inf.pathfinder;

import uk.ac.ed.inf.ilp.data.LngLat;

/**
 * Represents a point in a two-dimensional space with associated attributes.
 */
public class Point {
    /**
     * The angle from the previous point to the current point.
     */
    private double angle;

    /**
     * The current coordinates (LngLat) of this point.
     */
    private LngLat currentCoordinates;

    /**
     * The previous point in a path, if any.
     */
    private Point previousPoint;

    /**
     * The G score represents the total cost of traversing all previous points so far on the current path.
     */
    private double gScore;

    /**
     * The H score represents the cost (distance) of the current coordinates to the goal (end).
     */
    private double hScore;

    /**
     * The eight-character hexadecimal string assigned to the order we are finding a path for.
     */
    private String orderNo;

    /**
     * Constructs a Point with specified attributes.
     *
     * @param angle              The angle from the previous point to the current point.
     * @param currentCoordinates The current coordinates (LngLat) of this point.
     * @param previousPoint      The previous point in a path, if any.
     * @param gScore             The G score represents the total cost of traversing all previous points so far on the current path.
     * @param hScore             The H score represents the cost (distance) of the current coordinates to the goal (end).
     * @param orderNo            The eight-character hexadecimal order number assigned to the order that algorithm is finding a path for.
     */
    public Point(double angle, LngLat currentCoordinates, Point previousPoint, double gScore, double hScore, String orderNo) {
        this.angle = angle;
        this.currentCoordinates = currentCoordinates;
        this.previousPoint = previousPoint;
        this.gScore = gScore;
        this.hScore = hScore;
        this.orderNo = orderNo;
    }

    /**
     * Gets the angle from the previous point to the current point.
     *
     * @return The angle from the previous point to the current point.
     */
    public double getAngle(){return angle;}

    /**
     * Sets the angle from the previous point to the current point.
     *
     * @param angle from the previous point to the current point.
     */
    public void setAngle(double angle){this.angle = angle;}

    /**
     * Gets the current coordinates (LngLat) of this point.
     *
     * @return The current coordinates (LngLat) of this point.
     */
    public LngLat getLngLat() {
        return currentCoordinates;
    }

    /**
     * Sets the current coordinates (LngLat) of this point.
     *
     * @param currentCoordinates The new current coordinates (LngLat) for this point.
     */
    public void setCurrentCoordinates(LngLat currentCoordinates) {
        this.currentCoordinates = currentCoordinates;
    }

    /**
     * Gets the previous point in a path, if any.
     *
     * @return The previous point in a path, or null if there is none.
     */
    public Point getPreviousPoint() {
        return previousPoint;
    }

    /**
     * Sets the previous point in a path.
     *
     * @param previousPoint The new previous point in a path.
     */
    public void setPreviousPoint(Point previousPoint) {
        this.previousPoint = previousPoint;
    }

    /**
     * Gets the G score, the total cost of traversing all previous points so far on the current path.
     *
     * @return The G score of this point.
     */
    public double getgScore() {
        return gScore;
    }

    /**
     * Sets the G score, the total cost of traversing all previous points so far on the current path.
     *
     * @param gScore The updated G score for this point.
     */
    public void setgScore(double gScore) {
        this.gScore = gScore;
    }

    /**
     * Gets the H score, the cost (distance) of the current coordinates to the goal (end).
     *
     * @return The H score of this point.
     */
    public double gethScore() {
        return hScore;
    }

    /**
     * Sets the H score, the cost (distance) of the current coordinates to the goal (end).
     *
     * @param hScore The updated H score for this point.
     */
    public void sethScore(double hScore) {
        this.hScore = hScore;
    }

    /**
     * Gets the eight-character hexadecimal order number assigned to the order that the algorithm is finding a path for.
     *
     * @return The order number of the order that the path is being calculated for.
     */
    public String getOrderNo(){return orderNo;}

    /**
     * Sets the eight-character hexadecimal order number assigned to the order that the algorithm is finding a path for.
     *
     * @param orderNo The new order number for a point on a new path.
     */
    public void setOrderNo(String orderNo){
        this.orderNo = orderNo;
    }

}

