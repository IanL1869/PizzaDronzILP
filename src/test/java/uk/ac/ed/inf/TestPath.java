package uk.ac.ed.inf;

import uk.ac.ed.inf.handler.LongLatHandle;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.pathfinder.*;
import uk.ac.ed.inf.restClient.RestClient;

import java.io.IOException;

public class TestPath {

    public static void main(String[] args) throws IOException {

        LongLatHandle LLhandler = new LongLatHandle();

//        NamedRegion[] noFly = RestClient.getNoFlyZones();
//

        LngLat destination = new LngLat( -3.1912869215011597,55.945535152517735);
        LngLat start = new LngLat(-3.186874, 55.944494);

       // PathFinding2 astar = new PathFinding2("1", start, destination);

//        LongLatHandle handle = new LongLatHandle();
//
        RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net/");
        NamedRegion centralArea = restClient.getCentralArea();
        Order[] orders = restClient.getOrders();
        NamedRegion[] noFlyZones = restClient.getNoFlyZones();



//        astar.aStar(noFlyZones);


        PathFinder pathfinder = new PathFinder(orders[1].getOrderNo(), start, destination, noFlyZones, centralArea);
        pathfinder.aStar();

//        List<Point> path = PathFinding2.aStar(new Point(0, start, null, 0.0, LLhandler.distanceTo(start, destination)), destination, noFly);
//
//
//        for (Point paths: path){
//            System.out.println(paths);
//        }

    }
}
