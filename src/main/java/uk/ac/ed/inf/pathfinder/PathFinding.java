package uk.ac.ed.inf.pathfinder;

import uk.ac.ed.inf.handler.LongLatHandle;
import uk.ac.ed.inf.handler.OrderVal;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;

import java.util.*;

public class PathFinding {
    private static LongLatHandle LLhandle = new LongLatHandle();
    private static Order order;
    private static LngLat endPoint;
    public PathFinding(LngLat start, LngLat end, Order order) {
        this.order = order;
        endPoint = end;
    }
    public static List<Move> aStar(Node startNode, LngLat endNode, NamedRegion[] noFlyZones) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(
                node -> node.getGscore() + node.getHscore()));

            //            Comparator.comparingDouble(
//            node -> node.getGscore() + node.getHscore()));
//            @Override
//            public int compare(Node o1, Node o2) {
//                return Double.compare(o1.getGscore() + o1.getHscore(), o2.getGscore() + o2.getHscore());
//            }
//        });
        List<LngLat> closedSet = new ArrayList<>();
        Map<LngLat, Node> gScores = new HashMap<>();

        openSet.add(startNode);
        gScores.put(startNode.getCC(), startNode);

        while (!openSet.isEmpty()) {

            Node currentNode = openSet.poll();
            System.out.println("[" + currentNode.getCC().lng() + "," + currentNode.getCC().lat() + "],");
            closedSet.add(currentNode.getCC());

            if (LLhandle.isCloseTo(currentNode.getCC(), endNode)) {
                return reconstructPath(currentNode);
            }

            List<Node> neighbours = getNeighbours(currentNode, noFlyZones);

            for (Node node : neighbours) {
                if (closedSet.contains(node.getCC())) {
                    continue;
                }
                double new_cost = currentNode.getGscore() + LLhandle.distanceTo(currentNode.getCC(), node.getCC());


                if (gScores.containsKey(node.getCC()) && gScores.get(node.getCC()).getGscore() < new_cost) {
                    openSet.remove(gScores.get(node.getCC()));
                    openSet.add(node);
                    gScores.remove(node.getCC());
                    gScores.put(node.getCC(), node);

                } else if (!openSet.contains(node)) {
                    openSet.add(node);
                    gScores.put(node.getCC(), node);
                }

            }
        }
        return null;
    }


    public static List<Move> reconstructPath(Node currentNode){
        List<Move> total_path = new ArrayList<>();

        while (currentNode.getPC() != null){
            Move currentMove = new Move(order.getOrderNo(), currentNode.getPC().getCC().lng(), currentNode.getPC().getCC().lat(), 0 , currentNode.getCC().lng(), currentNode.getCC().lat());
            total_path.add(currentMove);
            currentNode = currentNode.getPC();
        }

        Collections.reverse(total_path);

        return total_path;
    }

    public static List<Node> getNeighbours(Node currentNode, NamedRegion[] noFlyZones){


        LngLat currentLL = currentNode.getCC();
        double[] angles = {0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};
        List<Node> neighbours = new ArrayList<>();
        for (double angle : angles){
            LngLat neighbour = LLhandle.nextPosition(currentLL, angle);
            boolean flag = true;


            for(NamedRegion noFlyZone: noFlyZones){
                if (LLhandle.isInRegion(neighbour, noFlyZone)){
                    flag = false;
                }
            }

            if (flag){

                neighbours.add(new Node(neighbour, currentNode, currentNode.getGscore() + SystemConstants.DRONE_MOVE_DISTANCE, LLhandle.distanceTo(neighbour, endPoint)));

            }
        }
        return neighbours;
    }
}
