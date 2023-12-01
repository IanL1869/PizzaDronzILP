package uk.ac.ed.inf;

import junit.framework.TestCase;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.pathfinder.PathFinder;
import uk.ac.ed.inf.restClient.FlightpathJSON;
import uk.ac.ed.inf.restClient.RestClient;

import java.util.List;

/**
 * Test Paths.
 */
public class PathTests extends TestCase {

    RestClient restClient = new RestClient("https://ilp-rest.azurewebsites.net", "2023-09-01");
    NamedRegion[] noFlyZones = restClient.getNoFlyZones();
    NamedRegion centralArea = restClient.getCentralArea();
    LngLat start = new LngLat(-3.186874, 55.944494);

    /**
     * Make sure path is not empty.
     */
    public void testPath() {

        LngLat end = new LngLat(-3.1838572025299072, 55.94449876875712);
        PathFinder pathFinder = new PathFinder(null, start , end, noFlyZones, centralArea );

        List<FlightpathJSON> path = pathFinder.aStar();

        assertNotNull(path);
    }

    /**
     * Make Sure goes around no fly zone.
     */
    public void testNoFlyZone() {

        LngLat end = new LngLat(-3.187500001288157, 55.94522256505843);
        PathFinder pathFinder = new PathFinder(null, start , end, noFlyZones, centralArea );

        List<FlightpathJSON> path = pathFinder.aStar();
        assertNotNull(path);
    }


}
