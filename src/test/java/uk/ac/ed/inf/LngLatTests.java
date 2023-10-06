package uk.ac.ed.inf;

import junit.framework.TestCase;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

import static junit.framework.Assert.assertTrue;

public class LngLatTests extends TestCase {
    public static LngLat[] sampleIntCoordinates;

    protected void setUp() throws Exception{
        super.setUp();
        LngLat p1 = new LngLat(0,0);
        LngLat p2 = new LngLat(3,2);
        LngLat p3 = new LngLat(4,-1);
        LngLat p4 = new LngLat(0,-4);
        LngLat p5 = new LngLat(-5,-2);
        LngLat p6 = new LngLat(-2,-1);
        LngLat p7 = new LngLat(-2,2);
        sampleIntCoordinates = new LngLat[]{p1,p2,p3,p4,p5,p6,p7};

    }

    public void testIsInRegion(){
        LngLat position = new LngLat(-3.192473,  55.942617);
        LngLat[] regionCoordinates = {new LngLat(-3.192473,  55.946233),
                new LngLat(-3.192473,  55.942617),
                new LngLat(-3.184319,  55.942617),
                new LngLat(-3.184319,  55.946233)};
        NamedRegion region = new NamedRegion("test", regionCoordinates);
        LngLatHandling lngLatHandler = new LongLatHandle();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    // Tests with diagram
    public void testIsInRegionWithPointAsVertex1(){
        LngLat position = new LngLat(0,-4);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandling lngLatHandler = new LongLatHandle();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsInRegionWithPointAsVertex2(){
        LngLat position = new LngLat(-2,-1);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandling lngLatHandler = new LongLatHandle();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsInRegionWithPointOnEdge1(){
        LngLat position = new LngLat(0,-2);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandling lngLatHandler = new LongLatHandle();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsInRegionWithPointOnEdge2(){
        LngLat position = new LngLat(-1,1);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandling lngLatHandler = new LongLatHandle();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsInRegionWithPointInside1(){
        LngLat position = new LngLat(-2,0);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandling lngLatHandler = new LongLatHandle();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsInRegionWithPointInside2(){
        LngLat position = new LngLat(3,-1);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandling lngLatHandler = new LongLatHandle();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertTrue(result);
    }

    public void testIsInRegionWithPointOutside1(){
        LngLat position = new LngLat(2,4);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandling lngLatHandler = new LongLatHandle();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertFalse(result);
    }

    public void testIsInRegionWithPointOutside2(){
        LngLat position = new LngLat(-5,-4);
        NamedRegion region = new NamedRegion("test", sampleIntCoordinates);
        LngLatHandling lngLatHandler = new LongLatHandle();
        boolean result = lngLatHandler.isInRegion(position, region);
        assertFalse(result);
    }

}
