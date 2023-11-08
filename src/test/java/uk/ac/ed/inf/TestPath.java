package uk.ac.ed.inf;

import uk.ac.ed.inf.handler.LongLatHandle;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.pathfinder.*;
import uk.ac.ed.inf.handler.LongLatHandle;
import java.io.IOException;
import java.util.List;

public class TestPath {

    public static void main(String[] args) throws IOException {

        LongLatHandle LLhandler = new LongLatHandle();

        NamedRegion[] noFly = RestClient.getNoFlyZones();

        Order[] ordercheck = RestClient.getOrders();

        LngLat destination = new LngLat( -3.1912869215011597,55.945535152517735);
        LngLat start = new LngLat(-3.186874, 55.944494);

        PathFinding2 astar = new PathFinding2("1", start, destination);

        LongLatHandle handle = new LongLatHandle();

        NamedRegion[] noFlyZones = new RestClient("https://ilp-rest.azurewebsites.net").getNoFlyZones();
        astar.aStar(noFlyZones);


        PathFinding2 pathfinder = new PathFinding2(ordercheck[1].getOrderNo(), start, destination);

//        List<Point> path = PathFinding2.aStar(new Point(0, start, null, 0.0, LLhandler.distanceTo(start, destination)), destination, noFly);
//
//
//        for (Point paths: path){
//            System.out.println(paths);
//        }

    }
}
