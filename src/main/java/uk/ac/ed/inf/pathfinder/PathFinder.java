package uk.ac.ed.inf.pathfinder;
import uk.ac.ed.inf.handler.LongLatHandle;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;


import java.util.*;
public class PathFinder {

    private  LongLatHandle LLhandle = new LongLatHandle();
    private String orderNo;
    private LngLat start;
    private LngLat end;
    private NamedRegion[] noFlyZones;
    private NamedRegion centralArea;
    private final LngLat appletonTower = new LngLat(-3.186874,55.944494);


//    private boolean leftCentralArea = false;
    private boolean returnedToCentralArea = false;
    private boolean returningToAppleton = false;



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
        Point startPoint = new Point(0, start , null , 0, LLhandle.distanceTo(start, end), orderNo);
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


                //gScoreValues.put(neighbour.getCurrentPoint(), neighbour);

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

    public List<Point> getNeighbours(Point currentPoint, NamedRegion[] noFlyZones) {

        LngLat currentLngLat = currentPoint.getLngLat();
        double[] angles = {0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};
        List<Point> neighbours = new ArrayList<>();

        for(double angle: angles){
            LngLat neighbourLngLat = LLhandle.nextPosition(currentLngLat, angle);

//            Point neighbour = new Point(null, currentPoint, 0, 0);
//            neighbour.setCurrentPoint(LLhandle.nextPosition(currentLngLat, angle));


            if (validateNeighbour(neighbourLngLat, currentLngLat)){

                Point neighbour = new Point(angle, neighbourLngLat, currentPoint, currentPoint.getgScore() + SystemConstants.DRONE_MOVE_DISTANCE, LLhandle.distanceTo(neighbourLngLat, end), orderNo );
                neighbours.add(neighbour);
            }
        }

        return neighbours;

    }

    public List<Point> reconstructPath(Point currentPoint){
        List<Point> total_path = new ArrayList<>();

        while (currentPoint != null){
            total_path.add(currentPoint);
            currentPoint = currentPoint.getPreviousPoint();
        }

        Collections.reverse(total_path);

        return total_path;
    }

    public boolean validateNeighbour(LngLat neighbourLngLat, LngLat previousLngLat){

        for (NamedRegion noFlyZone : noFlyZones){

            if (LLhandle.isInRegion(neighbourLngLat, noFlyZone)){

                return false;
            }
        }

        if (LLhandle.isInRegion(neighbourLngLat, centralArea)){
            returnedToCentralArea = true;
        }

        if (returnedToCentralArea && returningToAppleton){

            return LLhandle.isInRegion(neighbourLngLat, centralArea);

        }

        return true;

    }


}
