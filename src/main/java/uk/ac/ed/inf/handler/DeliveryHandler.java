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

import java.io.IOException;

import java.util.*;

/**
 * The DeliveryHandler class manages the delivery process, including order validation, pathfinding,
 * and writing delivery and flight path information to files.
 */
public class DeliveryHandler {


    /**
     * The date for which orders are being processed.
     */
    private String orderDate;

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
     * @param baseURL   The base URL for making API requests.
     * @param orderDate The date for which day orders are being processed.
     * @throws IOException If there is an issue with the RestClient.
     */
    public DeliveryHandler(String baseURL, String orderDate) throws IOException {
        this.orderDate = orderDate;
        this.restClient = new RestClient(baseURL, orderDate);
    }

    /**
     * Retrieves a list of valid orders by validating orders and setting their status to DELIVERED.
     *
     * @return A list of valid orders for the order date.
     * @throws IOException If there is an issue with the RestClient.
     */
    private List<Order> getValidOrders() throws IOException {
        List<Order> validOrders = new ArrayList<>();
        Restaurant[] restaurants = restClient.getRestaurants();
        Order[] orders = restClient.getOrdersOnDate();

        for (Order order : orders) {

            orderVal.validateOrder(order, restaurants);

            if (order.getOrderValidationCode().equals(OrderValidationCode.NO_ERROR)) {
                order.setOrderStatus(OrderStatus.DELIVERED);
                validOrders.add(order);
            }
        }
        return validOrders;
    }

    /**
     * Retrieves a list of validated deliveries based on valid orders.
     *
     * @return A list of validated deliveries in JSON format.
     * @throws IOException If there is an issue with the RestClient.
     */
    public List<DeliveriesJSON> getValidDeliveries() throws IOException {

        List<Order> validOrders = getValidOrders();
        List<DeliveriesJSON> validatedDeliveries = new ArrayList<>();

        for (Order order : validOrders) {
            order.setOrderStatus(OrderStatus.DELIVERED);
            DeliveriesJSON orderJSON = new DeliveriesJSON(order.getOrderNo(), order.getOrderStatus().toString(), order.getOrderValidationCode().toString(), order.getPriceTotalInPence());
            validatedDeliveries.add(orderJSON);
        }

        return validatedDeliveries;
    }


    /**
     * Retrieves flight paths for valid orders by calculating paths to the restaurant and for the path when returning to Appleton Tower.
     *
     * @return A list of flight paths in JSON format.
     * @throws IOException If there is an issue with the RestClient.
     */
    public List<FlightpathJSON> getFlightPaths() throws IOException {

        List<Order> validOrders = getValidOrders();
        Restaurant[] restaurants = restClient.getRestaurants();
        NamedRegion[] noFlyZones = restClient.getNoFlyZones();
        NamedRegion centralArea = restClient.getCentralArea();
        List<FlightpathJSON> flightPaths = new ArrayList<>();

        for (Order validOrder: validOrders){

            Restaurant restaurant = orderVal.getRestaurant(validOrder, restaurants);

            LngLatPair toRestaurantKey = new LngLatPair(appletonTower, restaurant.location());

            if (cachedPaths.containsKey(toRestaurantKey)) {
                updatePathOrderNo(flightPaths, validOrder, toRestaurantKey);


            } else {
                PathFinder pathFinderToRest = new PathFinder(validOrder.getOrderNo(), appletonTower, restaurant.location(), noFlyZones, centralArea);
                cachedPaths.put(toRestaurantKey, pathFinderToRest.aStar());
                flightPaths.addAll(pathFinderToRest.aStar());
            }

            LngLatPair toAppletonKey = new LngLatPair(restaurant.location(), appletonTower);

            if (cachedPaths.containsKey(toAppletonKey)) {
                updatePathOrderNo(flightPaths, validOrder, toAppletonKey);

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
        List<FlightpathJSON> pathToGet = cachedPaths.get(key);
        List<FlightpathJSON> updatePath = pathToGet.stream().map(flightpathJSON -> new FlightpathJSON(order.getOrderNo(), flightpathJSON.getFromLongitude(), flightpathJSON.getFromLatitude(), flightpathJSON.getAngle(), flightpathJSON.getToLongitude(), flightpathJSON.getToLatitude())).toList();


        flightPaths.addAll(updatePath);
    }


    /**
     * Writes deliveries to json, flight paths to json and drone flight coordinates to geojson files using WriteFiles class.
     *
     * @throws IOException If there is an issue writing files.
     */
    public void filesWriter() throws IOException {
        WriteFiles writeFiles = new WriteFiles(getValidDeliveries(), orderDate, getFlightPaths());
        writeFiles.writeDeliveries();
        writeFiles.writeFlightPath();
        writeFiles.writeDrone();
    }
}
