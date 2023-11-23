package uk.ac.ed.inf.pathfinder;
import uk.ac.ed.inf.handler.LongLatHandle;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.restClient.FlightpathJSON;


import java.util.*;
public class PathFinder {

    private final LongLatHandle LLhandle = new LongLatHandle();
    private final String orderNo;
    private final LngLat start;
    private final LngLat end;
    private final NamedRegion[] noFlyZones;
    private final NamedRegion centralArea;
    private final LngLat appletonTower = new LngLat(-3.186874,55.944494);
    private boolean returnedToCentralArea = false;
    private boolean returningToAppleton = false;

    private final double[] angles = {0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};



    public PathFinder(String orderNo, LngLat start, LngLat end, NamedRegion[] noFlyZones, NamedRegion centralArea) {
        this.orderNo = orderNo;
        this.start = start;
        this.end = end;
        this.noFlyZones = noFlyZones;
        this.centralArea = centralArea;
    }


    public List<Point> aStar() {

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

            List<Point> neighbours = getNeighbours(currentPoint, noFlyZones);

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

    private List<Point> getNeighbours(Point currentPoint, NamedRegion[] noFlyZones) {

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

    private List<Point> reconstructPath(Point currentPoint){
        List<Point> total_path = new ArrayList<>();
        total_path.add(new Point(999, currentPoint.getLngLat(), currentPoint, currentPoint.getgScore(), currentPoint.gethScore(), orderNo ));

        while (currentPoint != null && currentPoint.getPreviousPoint() != null){
            total_path.add(currentPoint);
            currentPoint = currentPoint.getPreviousPoint();
        }

        total_path.add(new Point(999, currentPoint.getLngLat(), currentPoint, currentPoint.getgScore(), currentPoint.gethScore(), orderNo ));

        Collections.reverse(total_path);

        return total_path;
    }

    private boolean validateNeighbour(LngLat neighbourLngLat, LngLat previousLngLat){

        for (NamedRegion noFlyZone : noFlyZones){

            if (LLhandle.isInRegion(neighbourLngLat, noFlyZone)){

                return false;
            }
        }

        if (LLhandle.isInRegion(previousLngLat, centralArea)){
            returnedToCentralArea = true;
        }

        if (returnedToCentralArea && returningToAppleton){

            return LLhandle.isInRegion(neighbourLngLat, centralArea);

        }

        return true;

    }


}
