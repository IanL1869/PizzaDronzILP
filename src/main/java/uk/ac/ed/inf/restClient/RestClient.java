package uk.ac.ed.inf.restClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URL;

public class RestClient {

    /**
     * Two variables of type string. One represents the URL used for the RestClient
     * and the other is the date of orders to retrieve.
     */

    private String baseURL;
    private String orderDate;

    /**
     * Constructor for RestClient
     * @param baseURL URL used for REST Server
     * @param orderDate Date of orders to get
     */
    public RestClient(String baseURL, String orderDate){
        this.baseURL = baseURL;
        this.orderDate = orderDate;
    }

    /**
     * Retrieves a list of Restaurants from the given base URL.
     * Sends a request to the given base URL to get a list of Restaurants.
     *
     *
     * @return An array of Restaurant records representing the retrieved Restaurants.
     * @throws RuntimeException if an IOException occurs during the process.
     */
    public Restaurant[] getRestaurants() throws IOException {


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Restaurant[] restaurant_list;
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
     *
     * @return An array of Order classes representing the retrieved Orders for that date.
     * @throws RuntimeException if an IOException occurs during the process.
     */
    public Order[] getOrdersOnDate() throws IOException{

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Order[] ordersOnDate;

        try{
            ordersOnDate = mapper.readValue(new URL (baseURL + "/orders" + "/" + orderDate), Order[].class);

        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return ordersOnDate;

    }

    /**
     * Retrieves the LngLat coordinates of the central area from the given base URL.
     * Sends a request to the given base URL to get a NamedRegion record for central area.
     * The NamedRegion record holds the LngLat coordinates of the centralArea.
     *
     *
     * @return A NamedRegion record that includes the LngLat coordinates of the central area.
     * @throws RuntimeException if an IOException occurs during the process.
     */
    public NamedRegion getCentralArea() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        NamedRegion centralArea;

        try{
            centralArea = mapper.readValue(new URL (baseURL + "/centralArea"), NamedRegion.class);

        }catch (IOException e){

            throw new RuntimeException(e);
        }

        return centralArea;
    }

    /**
     * Retrieves the LngLat coordinates of the no-fly-zones from the given base URL.
     * Sends a request to the given base URL to get an array of NamedRegion records for the zones the drone is not
     * allowed to fly in. The NamedRegion records hold the LngLat coordinates of each of these areas.
     *
     *
     * @return An array of NamedRegion records that includes LngLat coordinates of each no-fly-zone.
     * @throws RuntimeException if an IOException occurs during the process.
     */
    public NamedRegion[] getNoFlyZones() throws IOException{

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        NamedRegion[] noFlyZones;

        try{
            noFlyZones = mapper.readValue(new URL (baseURL + "/noFlyZones"), NamedRegion[].class);

        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return noFlyZones;
    }


//    public void main(String[] args) throws IOException {
//        Restaurant[] restaurant = getRestaurant();
//
//        for(Restaurant value: restaurant){
//            System.out.println(value);
//        }
//        System.out.println(restaurant);
//    }

}
