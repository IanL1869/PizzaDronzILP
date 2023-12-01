package uk.ac.ed.inf.handler;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.pathfinder.PathFinder;
import uk.ac.ed.inf.restClient.DeliveriesJSON;
import uk.ac.ed.inf.restClient.FlightpathJSON;
import uk.ac.ed.inf.restClient.RestClient;
import uk.ac.ed.inf.restClient.WriteFiles;

import java.util.*;

/**
 * The DeliveryHandler class manages the delivery process, including order validation, pathfinding,
 * and writing delivery and flight path information to files.
 */
public class DeliveryHandler {


    /**
     * The date for which orders are being processed.
     */
    private final String orderDate;

    /**
     * The RestClient responsible for making API requests.
     */
    private RestClient restClient;

    /**
     * Cached flight paths for paths that are repeated on an order date.
     */
    private Map<LngLatPair, List<FlightpathJSON>> cachedPaths = new HashMap<>();

    /**
     * Coordinates for Appleton Tower.
     */
    private final LngLat appletonTower = new LngLat(-3.186874, 55.944494);

    /**
     * Order validator for validating orders.
     */
    private OrderVal orderVal = new OrderVal();

    /**
     * Constructs a DeliveryHandler object.
     *
     * @param restClient   The rest client to interact with Rest Server.
     * @param orderDate The date for which day orders are being processed.
     */
    public DeliveryHandler(RestClient restClient, String orderDate){
        this.orderDate = orderDate;
        this.restClient = restClient;
    }

    /**
     * Retrieves a list of valid orders by validating orders.
     *
     * @return A list of valid orders for the order date.
     */
    private List<Order> getValidOrders(){
        List<Order> validOrders = new ArrayList<>();

        // get restaurants and orders for the order date.
        Restaurant[] restaurants = restClient.getRestaurants();
        Order[] orders = restClient.getOrdersOnDate();

        // loop through all orders in that day and validate them.
        for (Order order : orders) {

            orderVal.validateOrder(order, restaurants);

            // If orders are valid then add the valid orders to a list.
            if (order.getOrderValidationCode().equals(OrderValidationCode.NO_ERROR)) {
                validOrders.add(order);
            }
        }

        return validOrders;
    }

    /**
     * Retrieves a list of deliveries based on orders. Updates deliveries to delivered if order is valid.
     *
     * @return A list of deliveries in JSON format.
     */
    public List<DeliveriesJSON> getDeliveries(){

        // get a list of all orders.
        Order[] orders = restClient.getOrdersOnDate();
        List<DeliveriesJSON> deliveries = new ArrayList<>();
        Restaurant[] restaurants = restClient.getRestaurants();

        // loop through all the orders.
        for (Order order : orders) {
            // validate
            orderVal.validateOrder(order, restaurants);

            // set delivered if no error.
            if (order.getOrderValidationCode().equals(OrderValidationCode.NO_ERROR) && order.getOrderStatus().equals(OrderStatus.VALID_BUT_NOT_DELIVERED)){
                order.setOrderStatus(OrderStatus.DELIVERED);
            }
            // add all order to deliveries in correct format.
            DeliveriesJSON orderJSON = new DeliveriesJSON(order.getOrderNo(), order.getOrderStatus().toString(), order.getOrderValidationCode().toString(), order.getPriceTotalInPence());
            deliveries.add(orderJSON);


        }

        return deliveries;
    }


    /**
     * Retrieves flight paths for valid orders by calculating paths to the restaurant and for the path when returning to Appleton Tower.
     *
     * @return A list of flight paths in JSON format.
     */
    public List<FlightpathJSON> getFlightPaths() {

        // get the list of valid orders and get the data needed for writing flight paths.

        List<Order> validOrders = getValidOrders();
        Restaurant[] restaurants = restClient.getRestaurants();
        NamedRegion[] noFlyZones = restClient.getNoFlyZones();
        NamedRegion centralArea = restClient.getCentralArea();
        List<FlightpathJSON> flightPaths = new ArrayList<>();

        // loop through valid orders.
        for (Order validOrder: validOrders){

            // get the restaurant related to the order.
            Restaurant restaurant = orderVal.getRestaurant(validOrder, restaurants);

            // picking up the pizzas.
            // define a LngLatPair of the way there.
            LngLatPair toRestaurantKey = new LngLatPair(appletonTower, restaurant.location());

            // if this path to the restaurant has already been calculated then get it from cache.
            if (cachedPaths.containsKey(toRestaurantKey)) {
                updatePathOrderNo(flightPaths, validOrder, toRestaurantKey);

                // otherwise calculate the path to the restaurant, cache it and add it to the flight paths.
            } else {
                PathFinder pathFinderToRest = new PathFinder(validOrder.getOrderNo(), appletonTower, restaurant.location(), noFlyZones, centralArea);
                cachedPaths.put(toRestaurantKey, pathFinderToRest.aStar());
                flightPaths.addAll(pathFinderToRest.aStar());
            }

            // delivering the pizzas.
            // define a LngLatPair of the way back to Appleton.
            LngLatPair toAppletonKey = new LngLatPair(restaurant.location(), appletonTower);

            // if this path back has already been calculated then get it from cache.
            if (cachedPaths.containsKey(toAppletonKey)) {
                updatePathOrderNo(flightPaths, validOrder, toAppletonKey);

                // otherwise calculate the path back to Appleton, cache it and add it to the flight paths.
            } else {
                PathFinder pathFinderToAppleton = new PathFinder(validOrder.getOrderNo(), restaurant.location(), appletonTower, noFlyZones, centralArea);
                cachedPaths.put(toAppletonKey, pathFinderToAppleton.aStar());
                flightPaths.addAll(pathFinderToAppleton.aStar());
            }

        }

        return flightPaths;

    }

    /**
     * Updates the order number in the cached flight paths for a given order.
     *
     * @param flightPaths The list of flight paths to update.
     * @param order       The order for which to update the order number.
     * @param key       The key representing the start and end coordinates in the cached paths.
     */
    private void updatePathOrderNo(List<FlightpathJSON> flightPaths, Order order, LngLatPair key) {

        // find the path from cache, change orderNo to match the correct order and add it to flightpaths.
        List<FlightpathJSON> pathToGet = cachedPaths.get(key);
        List<FlightpathJSON> updatePath = pathToGet.stream().map(flightpathJSON -> new FlightpathJSON(order.getOrderNo(), flightpathJSON.getFromLongitude(), flightpathJSON.getFromLatitude(), flightpathJSON.getAngle(), flightpathJSON.getToLongitude(), flightpathJSON.getToLatitude())).toList();

        flightPaths.addAll(updatePath);
    }


    /**
     * Writes deliveries to json, flight paths to json and drone flight coordinates to geojson files using WriteFiles class.
     */
    public void filesWriter() {
        WriteFiles writeFiles = new WriteFiles(getDeliveries(), orderDate, getFlightPaths());
        writeFiles.writeDeliveries();
        writeFiles.writeFlightPath();
        writeFiles.writeDrone();
    }
}
