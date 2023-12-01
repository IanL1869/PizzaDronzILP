package uk.ac.ed.inf;

import static org.junit.Assert.assertTrue;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.File;
import java.io.IOException;


import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static TestSuite suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    /**
     * Check its under 60 seconds.
     * @throws IOException Incase.
     */
    public void testTime() throws IOException {

        long startTime = System.nanoTime();
        App.main(new String[] { "2023-11-01", "https://ilp-rest.azurewebsites.net/" });

        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;

        assertTrue(duration < 60000);
    }






}
