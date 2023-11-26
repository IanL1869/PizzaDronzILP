package uk.ac.ed.inf.restClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URL;

/**
 * RestClient class to get essential data from Rest Service.
 */
public class RestClient {

    /**
     * String representing base URL for the Rest Service.
     */
    private final String baseURL;

    /**
     * String representing the order date.
     */
    private final String orderDate;

    /**
     * ObjectMapper for converting JSON to Java objects.
     */
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructor for RestClient
     *
     * @param baseURL   String representing base URL for the Rest Service.
     * @param orderDate String representing the order date.
     */
    public RestClient(String baseURL, String orderDate) {
        this.baseURL = baseURL;
        this.orderDate = orderDate;
    }

    /**
     * Retrieves a list of Restaurants from the given base URL.
     * Sends a request to the given base URL to get a list of Restaurants.
     *
     * @return An array of Restaurant records representing the retrieved Restaurants.
     * @throws RuntimeException if an IOException occurs during the process.
     */
    public Restaurant[] getRestaurants(){

        Restaurant[] restaurant_list;

        // read in restaurants.
        try {

            restaurant_list = mapper.readValue(new URL(baseURL + "/restaurants"), Restaurant[].class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return restaurant_list;
    }

    /**
     * Retrieves a list of Orders from the given base URL and orderDate.
     * Sends a request to the given base URL to get a list of Orders on a specific date.
     *
     * @return An array of Order classes representing the retrieved Orders for that date.
     * @throws RuntimeException if an IOException occurs during the process.
     */
    public Order[] getOrdersOnDate(){

        // needed for reading in orders on a given date.
        mapper.registerModule(new JavaTimeModule());
        Order[] ordersOnDate;

        // read in orders on the given date.
        try {
            ordersOnDate = mapper.readValue(new URL(baseURL + "/orders" + "/" + orderDate), Order[].class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ordersOnDate;

    }

    /**
     * Retrieves the NamedRegion central area from the given base URL.
     * Sends a request to the given base URL to get a NamedRegion record for central area.
     * The NamedRegion record holds the LngLat coordinates of the centralArea.
     *
     * @return A NamedRegion record that includes the LngLat coordinates of the central area.
     * @throws RuntimeException if an IOException occurs during the process.
     */
    public NamedRegion getCentralArea(){

        NamedRegion centralArea;

        // read in centralArea on the given date.
        try {
            centralArea = mapper.readValue(new URL(baseURL + "/centralArea"), NamedRegion.class);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        return centralArea;
    }

    /**
     * Retrieves NamedRegion no-fly-zones from the given base URL.
     * Sends a request to the given base URL to get an array of NamedRegion records for the zones the drone is not
     * allowed to fly in. The NamedRegion records hold the LngLat coordinates of each of these areas.
     *
     * @return An array of NamedRegion records that includes LngLat coordinates of each no-fly-zone.
     * @throws RuntimeException if an IOException occurs during the process.
     */
    public NamedRegion[] getNoFlyZones(){

        NamedRegion[] noFlyZones;

        // read in no-fly zones.
        try {
            noFlyZones = mapper.readValue(new URL(baseURL + "/noFlyZones"), NamedRegion[].class);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        return noFlyZones;
    }
}
