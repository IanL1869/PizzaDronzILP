package uk.ac.ed.inf;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.ac.ed.inf.ilp.data.LngLat;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Make sure Files are correct names.
 */
public class FileTests extends TestCase {

    public static String date;
    public static String url = "https://ilp-rest.azurewebsites.net/";

    // generates random order days.
    protected void setUp() throws Exception {

        super.setUp();
        Random random = new Random();

        int day = random.nextInt(1, 31);

        if (day < 10){
            date = "2023-09-" + "0" + day;
        }else{
            date = "2023-09-"  + day;
        }

    }

    /**
     * Drone file name test.
     * @throws IOException for running entire app.
     */
    public void testDroneFileName() throws IOException {
        App.main(new String[] { date, url });
        File expectedFile = new File("resultfiles/drone-" + date + ".geojson");
        assertTrue(expectedFile.exists());
    }

    /**
     * Deliveries file name test.
     * @throws IOException for running entire app.
     */
    public void testDeliveriesFileName() throws IOException {
        App.main(new String[] { date, url });
        File expectedFile = new File("resultfiles/deliveries-" + date + ".json");
        assertTrue(expectedFile.exists());
    }

    /**
     * FlightPath file name test.
     * @throws IOException for running entire app.
     */
    public void testFlightPathFileName() throws IOException {
        App.main(new String[] { date, url });
        File expectedFile = new File("resultfiles/flightpath-" + date + ".json");
        assertTrue(expectedFile.exists());
    }





}