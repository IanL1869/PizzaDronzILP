package uk.ac.ed.inf.pathfinder;

import uk.ac.ed.inf.handler.LngLatHandle;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.restClient.FlightpathJSON;
import java.util.*;

/**
 * A class for finding a flight path using the A* algorithm.
 */
public class PathFinder {


    /**
     * The handle for longitude and latitude calculations.
     */
    private final LngLatHandle lngLatHandle = new LngLatHandle();

    /**
     * The order number associated with the order that the path is being calculated for.
     */
    private final String orderNo;

    /**
     * The starting point of the flight path.
     */
    private final LngLat start;

    /**
     * The ending point of the flight path.
     */
    private final LngLat end;

    /**
     * An array of named regions representing no-fly zones.
     */
    private final NamedRegion[] noFlyZones;

    /**
     * The central area where the drone needs to return.
     */
    private final NamedRegion centralArea;

    /**
     * The predefined coordinates of Appleton Tower.
     */
    private final LngLat appletonTower = new LngLat(-3.186874, 55.944494);

    /**
     * Flag indicating whether the drone has returned to the central area.
     */
    private boolean returnedToCentralArea = false;

    /**
     * Flag indicating whether the drone is returning to Appleton Tower.
     */
    private boolean returningToAppleton = false;

    /**
     * The predefined angles for finding neighbouring points.
     */
    private final double[] angles = {0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};


    /**
     * Constructs a new PathFinder object with the specified parameters.
     *
     * @param orderNo The order number associated with the order that the path is being calculated for.
     * @param start The starting point of the flight path.
     * @param end The ending point of the flight path.
     * @param noFlyZones An array of named regions representing no-fly zones.
     * @param centralArea The central area where the drone needs to return.
     */
    public PathFinder(String orderNo, LngLat start, LngLat end, NamedRegion[] noFlyZones, NamedRegion centralArea) {
        this.orderNo = orderNo;
        this.start = start;
        this.end = end;
        this.noFlyZones = noFlyZones;
        this.centralArea = centralArea;
    }

    /**
     * Finds a flight path from start point to end point using the A* algorithm.
     *
     * @return A list of FlightpathJSON objects representing the flight path.
     */
    public List<FlightpathJSON> aStar() {

        // update condition if the end coordinate is Appleton Tower.
        if (end == appletonTower){
            returningToAppleton = true;
        }

        // setup priority queue so that it is arranged with respect to lowest f score (g score + h score).
        PriorityQueue<Point> frontier = new PriorityQueue<>(Comparator.comparingDouble(point -> point.getgScore() + point.gethScore()));

        // initialise start Point and add it to the frontier.
        Point startPoint = new Point(0, start , null , 0, lngLatHandle.distanceTo(start, end), orderNo);
        frontier.add(startPoint);

        // initialise hashset to track points already visited and hashmap to store g scores.
        Set<LngLat> visitedPoints = new HashSet<>();
        Map<LngLat, Point> gScoreValues = new HashMap<>();

        gScoreValues.put(startPoint.getLngLat(), startPoint);

        // loop when frontier is not empty.
        while (!frontier.isEmpty()) {

            // get and remove the Point with lowest f score and add it to visited points.
            Point currentPoint = frontier.poll();
            visitedPoints.add(currentPoint.getLngLat());

            // condition when destination is reached
            if (lngLatHandle.isCloseTo(currentPoint.getLngLat(), end)) {
                return reconstructPath(currentPoint);
            }

            // initialise a list of the neighbours
            List<Point> neighbours = getNeighbours(currentPoint);

            // loop through neighbours
            for(Point neighbour : neighbours){

                if (visitedPoints.contains(neighbour.getLngLat())) {
                    continue;
                }

                // condition when coordinate has already been searched but there's another path that costs less.
                if (gScoreValues.containsKey(neighbour.getLngLat())) {

                    double new_cost = neighbour.getgScore();
                    Point existingNeighbour = gScoreValues.get(neighbour.getLngLat());

                    if (existingNeighbour.getgScore() > new_cost) {
                        // remove old neighbour and replace it with less cost neighbour of same coordinate.
                        frontier.remove(existingNeighbour);
                        frontier.add(neighbour);
                        gScoreValues.replace(neighbour.getLngLat(), neighbour);

                    }
                } else {
                    // add neighbour to the frontier and update g score values.
                    frontier.add(neighbour);
                    gScoreValues.put(neighbour.getLngLat(), neighbour);
                }

            }

        }
        return null;
    }

    /**
     * Gets all neighbours of a point.
     *
     * @param currentPoint The point to retrieve neighbours for.
     * @return A list Point objects representing all neighbouring points of the current point parameter.
     */
    private List<Point> getNeighbours(Point currentPoint) {

        LngLat currentLngLat = currentPoint.getLngLat();
        List<Point> neighbours = new ArrayList<>();

        // loop through all possible angles and calculate next coordinate.
        for(double angle : angles){

            LngLat neighbourLngLat = lngLatHandle.nextPosition(currentLngLat, angle);

            // if the neighbour is valid add it to the list.
            if (checkNeighbour(neighbourLngLat, currentLngLat)){

                Point neighbour = new Point(angle, neighbourLngLat, currentPoint, currentPoint.getgScore() + SystemConstants.DRONE_MOVE_DISTANCE, lngLatHandle.distanceTo(neighbourLngLat, end), orderNo);
                neighbours.add(neighbour);
            }
        }

        return neighbours;

    }

    /**
     * Reconstructs the flight path from the given end point to the start point.
     *
     * @param endPoint The end point of the path to reconstruct.
     * @return A list FlightpathJSON objects that represent the reconstructed flight path.
     */
    private List<FlightpathJSON> reconstructPath(Point endPoint){
        List<FlightpathJSON> total_path = new ArrayList<>();

        // adds the hovering of the drone at the end of a path.
        total_path.add(new FlightpathJSON(orderNo, endPoint.getLngLat().lng(), endPoint.getLngLat().lat(), 999,endPoint.getLngLat().lng(), endPoint.getLngLat().lat()  ));

        // builds the path from the end point backwards.
        while (endPoint != null && endPoint.getPreviousPoint() != null){
            total_path.add(new FlightpathJSON(orderNo, endPoint.getPreviousPoint().getLngLat().lng(),endPoint.getPreviousPoint().getLngLat().lat(), endPoint.getAngle(), endPoint.getLngLat().lng(), endPoint.getLngLat().lat() ));
            endPoint = endPoint.getPreviousPoint();
        }

        // as it is backwards reverse the list to get path.
        Collections.reverse(total_path);

        return total_path;
    }

    /**
     * Checks to see if neighbour is a valid point or not.
     *
     * @param neighbourLngLat The neighbour's longitude and latitude.
     * @param currentLngLat The current point's longitude and latitude.
     * @return True if neighbour is valid, false otherwise.
     */
    private boolean checkNeighbour(LngLat neighbourLngLat, LngLat currentLngLat){

        // loop through all no-fly zones and check if the neighbour is in one.
        for (NamedRegion noFlyZone : noFlyZones){
            if (lngLatHandle.isInRegion(neighbourLngLat, noFlyZone)){
                return false;
            }
        }

        // if the current coordinate is in the central area update the boolean that tracks this.
        if(centralArea != null) {
            if (lngLatHandle.isInRegion(currentLngLat, centralArea)) {
                returnedToCentralArea = true;
            }
        }

        // if the path is on the return to Appleton and has returned to the central area then the
        // neighbour cannot leave central area again.
        if (returnedToCentralArea && returningToAppleton){

            return lngLatHandle.isInRegion(neighbourLngLat, centralArea);

        }

        return true;

    }


}
