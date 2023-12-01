package uk.ac.ed.inf;

import junit.framework.TestCase;


import org.junit.Test;
import uk.ac.ed.inf.handler.LngLatHandle;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;


/**
 * The LngLatTests class contains JUnit tests for validating various scenarios in the LngLatHandling class.
 */
public class LngLatTests extends TestCase {

    /** An array of LngLat objects representing a sample rectangle. */

    public static LngLat[] sampleRectangle;

    /**
     * Initialises test data before each test method execution.
     *
     * @throws Exception If there is an issue setting up the test.
     */
    protected void setUp() throws Exception {
        super.setUp();
        LngLat vertex1 = new LngLat(55.943067559235686, -3.1893342007830086);
        LngLat vertex2 = new LngLat(55.94219537835775, -3.1893342007830086);
        LngLat vertex3 = new LngLat(55.94219537835775, -3.1853658189387204);
        LngLat vertex4 = new LngLat(55.943067559235686, -3.1853658189387204);

        sampleRectangle = new LngLat[]{vertex1,vertex2,vertex3,vertex4};

    }

    /**
     * Tests the calculation of distance between two LngLat positions.
     */
    public void testDistanceTo(){
        LngLat startPosition = new LngLat(1.68687,1.5777474);
        LngLat endPosition = new LngLat(4.848484,5.92992);
        LngLatHandling lngLatHandler = new LngLatHandle();
        double result = lngLatHandler.distanceTo(startPosition, endPosition);
        assertEquals(5.379331689456113, result);
    }
    /**
     * Tests very close positions.
     */
    public void testIsCloseToVeryClose() {
        LngLatHandle lngLatHandling = new LngLatHandle();

        LngLat lngLat1 = new LngLat(55.94279900000000, -3.188739883592973);
        LngLat lngLat2 =  new LngLat(55.94279906310982, -3.188739883592973);

        assertTrue(lngLatHandling.isCloseTo(lngLat1, lngLat2));
    }

    /**
     * Tests is close to same point.
     */
    public void testIsCloseToSame() {
        LngLatHandle lngLatHandling = new LngLatHandle();

        LngLat lngLat1 = new LngLat(55.94279906310982, -3.188739883592973);
        LngLat lngLat2 =  new LngLat(55.94279906310982, -3.188739883592973);

        assertTrue(lngLatHandling.isCloseTo(lngLat1, lngLat2));
    }

    /**
     * Tests the calculation of the next LngLat position based on a given angle.
     */
    public void testNextPosition(){

        LngLatHandle lngLatHandling = new LngLatHandle();

        LngLat lngLat1 = new LngLat(55.94279906310982, -3.188739883592973);
        LngLat nextLngLat = lngLatHandling.nextPosition(lngLat1, 90);

        assertEquals(55.94279906310982, nextLngLat.lng());
        assertEquals(-3.188589883592973, nextLngLat.lat());

    }

    /**
     * Tests LngLat position is within a region.
     */
    public void testIsInRegion() {
        LngLatHandling lngLatHandler = new LngLatHandle();
        NamedRegion region = new NamedRegion("test", sampleRectangle);
        LngLat position = new LngLat(55.94262561135076, -3.187409601839363);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    /**
     * Tests LngLat position on right edge is within a region.
     */
    public void testIsInRegionR() {
        LngLatHandling lngLatHandler = new LngLatHandle();
        NamedRegion region = new NamedRegion("test", sampleRectangle);
        LngLat position = new LngLat(55.94262561135076, -3.1893342007830086);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    /**
     * Tests LngLat position on left edge is within a region.
     */
    public void testIsInRegionL() {
        LngLatHandling lngLatHandler = new LngLatHandle();
        NamedRegion region = new NamedRegion("test", sampleRectangle);
        LngLat position = new LngLat(55.94262561135076, -3.1853658189387204);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    /**
     * Tests LngLat position on upper edge is within a region.
     */
    public void testIsInRegionU() {
        LngLatHandling lngLatHandler = new LngLatHandle();
        NamedRegion region = new NamedRegion("test", sampleRectangle);
        LngLat position = new LngLat(55.943067559235686, -3.187409601839363);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    /**
     * Tests LngLat position on bottom edge is within a region.
     */
    public void testIsInRegionD() {
        LngLatHandling lngLatHandler = new LngLatHandle();
        NamedRegion region = new NamedRegion("test", sampleRectangle);
        LngLat position = new LngLat(55.94219537835775, -3.187409601839363);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    /**
     * Tests LngLat position on vertex is within a region.
     */
    public void testIsInRegionVertex() {
        LngLatHandling lngLatHandler = new LngLatHandle();
        NamedRegion region = new NamedRegion("test", sampleRectangle);
        LngLat position = new LngLat(55.943067559235686, -3.1893342007830086);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

}
