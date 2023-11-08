package uk.ac.ed.inf.pathfinder;
import uk.ac.ed.inf.handler.LongLatHandle;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;

import java.util.*;
public class PathFinding2 {

    private static LongLatHandle LLhandle = new LongLatHandle();
    private String orderNo;
    private static LngLat start;
    private static LngLat end;




    public PathFinding2(String orderNo, LngLat start, LngLat end) {
        this.orderNo = orderNo;
        this.start = start;
        this.end = end;
    }

    public static List<Point> aStar(NamedRegion[] noFlyZones) {

        PriorityQueue<Point> frontier = new PriorityQueue<>(Comparator.comparingDouble(point -> point.getgScore() + point.gethScore()));
        Point startPoint = new Point(0,start , null , 0, LLhandle.distanceTo(start, end));
        frontier.add(startPoint);

        List<LngLat> visitedPoints = new ArrayList<>();
        Map<LngLat, Point> gScoreValues = new HashMap<>();

        gScoreValues.put(startPoint.getLngLat(), startPoint);

        while (!frontier.isEmpty()) {

            Point currentPoint = frontier.poll();
            System.out.println("[" + currentPoint.getLngLat().lng() + "," + currentPoint.getLngLat().lat() + "],");
            visitedPoints.add(currentPoint.getLngLat());

            if (LLhandle.isCloseTo(currentPoint.getLngLat(), end)) {
                return reconstructPath(currentPoint);
            }

            List<Point> neighbours = getNeighbours(currentPoint, noFlyZones);

            for(Point neighbour : neighbours){

                if (visitedPoints.contains(neighbour.getLngLat())) {
                    continue;
                }


                //gScoreValues.put(neighbour.getCurrentPoint(), neighbour);

                if (gScoreValues.containsKey(neighbour.getLngLat())) {

                    double new_cost = currentPoint.getgScore() + LLhandle.distanceTo(currentPoint.getLngLat(), neighbour.getLngLat());
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

    public static List<Point> getNeighbours(Point currentPoint, NamedRegion[] noFlyZones) {
        LngLat currentCC = currentPoint.getLngLat();
        double[] angles = {0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};
        List<Point> neighbours = new ArrayList<>();

        for(double angle: angles){
            boolean flag = true;
            LngLat neighbourLngLat = LLhandle.nextPosition(currentCC, angle);

//            Point neighbour = new Point(null, currentPoint, 0, 0);
//            neighbour.setCurrentPoint(LLhandle.nextPosition(currentCC, angle));

            for(NamedRegion noFlyZone : noFlyZones){
                if(LLhandle.isInRegion(neighbourLngLat, noFlyZone)){
                    flag = false;
                }
            }

            if (flag){

                Point neighbour = new Point(angle, currentPoint.getLngLat(), currentPoint, currentPoint.getgScore() + SystemConstants.DRONE_MOVE_DISTANCE, LLhandle.distanceTo(neighbourLngLat, end) );
                neighbours.add(neighbour);

            }
        }

        return neighbours;

    }

    public static List<Point> reconstructPath(Point currentPoint){
        List<Point> total_path = new ArrayList<>();

        while (currentPoint != null){
            total_path.add(currentPoint);
            currentPoint = currentPoint.getPreviousPoint();
        }

        Collections.reverse(total_path);

        return total_path;
    }


}
