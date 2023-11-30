package uk.ac.ed.inf;

import junit.framework.TestCase;


import org.junit.Test;
import uk.ac.ed.inf.handler.LngLatHandle;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;



public class LngLatTests extends TestCase {

    public static LngLat[] sampleRectangle;


    protected void setUp() throws Exception {
        super.setUp();
        LngLat p1 = new LngLat(55.943067559235686, -3.1893342007830086);
        LngLat p2 = new LngLat(55.94219537835775, -3.1893342007830086);
        LngLat p3 = new LngLat(55.94219537835775, -3.1853658189387204);
        LngLat p4 = new LngLat(55.943067559235686, -3.1853658189387204);

        sampleRectangle = new LngLat[]{p1,p2,p3,p4};

    }

    public void testDistanceTo(){
        LngLat startPosition = new LngLat(1.68687,1.5777474);
        LngLat endPosition = new LngLat(4.848484,5.92992);
        LngLatHandling lngLatHandler = new LngLatHandle();
        double result = lngLatHandler.distanceTo(startPosition, endPosition);
        assertEquals(5.379331689456113, result);
    }

    public void testIsCloseToVeryClose() {
        LngLatHandle lngLatHandling = new LngLatHandle();

        LngLat lngLat1 = new LngLat(55.94279900000000, -3.188739883592973);
        LngLat lngLat2 =  new LngLat(55.94279906310982, -3.188739883592973);

        assertTrue(lngLatHandling.isCloseTo(lngLat1, lngLat2));
    }

    public void testIsCloseToSame() {
        LngLatHandle lngLatHandling = new LngLatHandle();

        LngLat lngLat1 = new LngLat(55.94279906310982, -3.188739883592973);
        LngLat lngLat2 =  new LngLat(55.94279906310982, -3.188739883592973);

        assertTrue(lngLatHandling.isCloseTo(lngLat1, lngLat2));
    }

    public void testNextPosition(){

        LngLatHandle lngLatHandling = new LngLatHandle();

        LngLat lngLat1 = new LngLat(55.94279906310982, -3.188739883592973);
        LngLat nextLngLat = lngLatHandling.nextPosition(lngLat1, 90);

        assertEquals(55.94279906310982, nextLngLat.lng());
        assertEquals(-3.188589883592973, nextLngLat.lat());

    }

    public void testIsInRegion() {
        LngLatHandling lngLatHandler = new LngLatHandle();
        NamedRegion region = new NamedRegion("test", sampleRectangle);
        LngLat position = new LngLat(55.94262561135076, -3.187409601839363);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsInRegionR() {
        LngLatHandling lngLatHandler = new LngLatHandle();
        NamedRegion region = new NamedRegion("test", sampleRectangle);
        LngLat position = new LngLat(55.94262561135076, -3.1893342007830086);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsInRegionL() {
        LngLatHandling lngLatHandler = new LngLatHandle();
        NamedRegion region = new NamedRegion("test", sampleRectangle);
        LngLat position = new LngLat(55.94262561135076, -3.1853658189387204);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsInRegionU() {
        LngLatHandling lngLatHandler = new LngLatHandle();
        NamedRegion region = new NamedRegion("test", sampleRectangle);
        LngLat position = new LngLat(55.943067559235686, -3.187409601839363);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsInRegionD() {
        LngLatHandling lngLatHandler = new LngLatHandle();
        NamedRegion region = new NamedRegion("test", sampleRectangle);
        LngLat position = new LngLat(55.94219537835775, -3.187409601839363);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsInRegionVertex() {
        LngLatHandling lngLatHandler = new LngLatHandle();
        NamedRegion region = new NamedRegion("test", sampleRectangle);
        LngLat position = new LngLat(55.943067559235686, -3.1893342007830086);

        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }








}
