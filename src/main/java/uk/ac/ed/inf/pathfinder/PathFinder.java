package uk.ac.ed.inf.pathfinder;
import uk.ac.ed.inf.handler.LongLatHandle;
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
    private final LongLatHandle LLhandle = new LongLatHandle();

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

        if (end == appletonTower){

            returningToAppleton = true;

        }

        PriorityQueue<Point> frontier = new PriorityQueue<>(Comparator.comparingDouble(point -> point.getgScore() + point.gethScore()));
        Point startPoint = new Point(
                0,
                start ,
                null ,
                0,
                LLhandle.distanceTo(start, end),
                orderNo
        );

        frontier.add(startPoint);

        Set<LngLat> visitedPoints = new HashSet<>();
        Map<LngLat, Point> gScoreValues = new HashMap<>();

        gScoreValues.put(startPoint.getLngLat(), startPoint);

        while (!frontier.isEmpty()) {

            Point currentPoint = frontier.poll();
            visitedPoints.add(currentPoint.getLngLat());

            if (LLhandle.isCloseTo(currentPoint.getLngLat(), end)) {
                returningToAppleton = false;
                returnedToCentralArea = false;
                return reconstructPath(currentPoint);
            }

            List<Point> neighbours = getNeighbours(currentPoint);

            for(Point neighbour : neighbours){

                if (visitedPoints.contains(neighbour.getLngLat())) {
                    continue;
                }

                if (gScoreValues.containsKey(neighbour.getLngLat())) {

                    double new_cost = neighbour.getgScore();
                    Point existingNeighbour = gScoreValues.get(neighbour.getLngLat());
                    if (existingNeighbour.getgScore() > new_cost) {

                        frontier.remove(existingNeighbour);
                        frontier.add(neighbour);
                        gScoreValues.replace(neighbour.getLngLat(), neighbour);

                    }
                } else {
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

        for(double angle: angles){
            LngLat neighbourLngLat = LLhandle.nextPosition(currentLngLat, angle);


            if (validateNeighbour(neighbourLngLat, currentLngLat)){

                Point neighbour = new Point(
                        angle,
                        neighbourLngLat,
                        currentPoint,
                        currentPoint.getgScore() + SystemConstants.DRONE_MOVE_DISTANCE,
                        LLhandle.distanceTo(neighbourLngLat, end),
                        orderNo
                );

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

        total_path.add(new FlightpathJSON(orderNo, endPoint.getLngLat().lng(), endPoint.getLngLat().lat(), 999,endPoint.getLngLat().lng(), endPoint.getLngLat().lat()  ));

        while (endPoint != null && endPoint.getPreviousPoint() != null){
            total_path.add(new FlightpathJSON(orderNo, endPoint.getPreviousPoint().getLngLat().lng(),endPoint.getPreviousPoint().getLngLat().lat(), endPoint.getAngle(), endPoint.getLngLat().lng(), endPoint.getLngLat().lat() ));
            endPoint = endPoint.getPreviousPoint();
        }

        total_path.add(new FlightpathJSON(orderNo, endPoint.getLngLat().lng(), endPoint.getLngLat().lat(), 999,endPoint.getLngLat().lng(), endPoint.getLngLat().lat()));

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
    private boolean validateNeighbour(LngLat neighbourLngLat, LngLat currentLngLat){

        for (NamedRegion noFlyZone : noFlyZones){

            if (LLhandle.isInRegion(neighbourLngLat, noFlyZone)){

                return false;
            }
        }

        if (LLhandle.isInRegion(currentLngLat, centralArea)){
            returnedToCentralArea = true;
        }

        if (returnedToCentralArea && returningToAppleton){

            return LLhandle.isInRegion(neighbourLngLat, centralArea);

        }

        return true;

    }


}
