package uk.ac.ed.inf.handler;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.pathfinder.PathFinder;
import uk.ac.ed.inf.pathfinder.Point;
import uk.ac.ed.inf.restClient.DeliveriesJSON;
import uk.ac.ed.inf.restClient.FlightpathJSON;
import uk.ac.ed.inf.restClient.RestClient;
import uk.ac.ed.inf.restClient.WriteFiles;

import java.io.IOException;

import java.util.*;


public class DeliveryHandler {

    private  String orderDate;
    private  RestClient restClient;
    private Map<LngLatPair, List<FlightpathJSON>> cachedPaths = new HashMap<>();
    private final LngLat appletonTower = new LngLat(-3.186874,55.944494);



    public DeliveryHandler(String baseURL, String orderDate) throws IOException {
        this.orderDate = orderDate;
        this.restClient = new RestClient(baseURL, orderDate);
    }

    private List<Order> getValidOrders() throws IOException {
        Restaurant[] restaurants = restClient.getRestaurants();
        Order[] orders = restClient.getOrdersOnDate();
        List<Order> validOrders = new ArrayList<>();
        OrderVal orderToValidate = new OrderVal();

        for (Order order : orders) {

            orderToValidate.validateOrder(order, restaurants);

            if (order.getOrderValidationCode().equals(OrderValidationCode.NO_ERROR)) {
                order.setOrderStatus(OrderStatus.DELIVERED);

                validOrders.add(order);
            }

        }
        return validOrders;
    }

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


    public List<FlightpathJSON> getFlightPaths() throws IOException {

        Restaurant[] restaurants = restClient.getRestaurants();
        NamedRegion[] noFlyZones = restClient.getNoFlyZones();
        NamedRegion centralArea = restClient.getCentralArea();
        List<FlightpathJSON> flightPaths = new ArrayList<>();

        List<Order> validOrders = getValidOrders();

        for (Order validOrder: validOrders){

            Restaurant restaurant = getRestaurant(validOrder, restaurants);

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
//      Need to make sure orderNO is updated
    private void updatePathOrderNo(List<FlightpathJSON> flightPaths, Order order, LngLatPair toAppletonKey) {
        List<FlightpathJSON> pathToAppleton = cachedPaths.get(toAppletonKey);
        List<FlightpathJSON> updatePathToAppleton = pathToAppleton.stream().map(flightpathJSON -> new FlightpathJSON(order.getOrderNo(), flightpathJSON.getFromLongitude(), flightpathJSON.getFromLatitude(), flightpathJSON.getAngle(), flightpathJSON.getToLongitude(), flightpathJSON.getToLatitude())).toList();


        flightPaths.addAll(updatePathToAppleton);
    }


    //     method only to be called on validated orders
    private Restaurant getRestaurant(Order validatedOrder, Restaurant[] restaurants){

        if (validatedOrder.getOrderValidationCode().equals(OrderValidationCode.NO_ERROR)){
            for(Restaurant restaurant: restaurants){

                if (Arrays.asList(restaurant.menu()).contains(validatedOrder.getPizzasInOrder()[0])){
                    return restaurant;
                }
            }
        }
        return null;

    }

    public void filesWriter() throws IOException {
        WriteFiles writeFiles = new WriteFiles(getValidDeliveries(), orderDate, getFlightPaths());
        writeFiles.writeDeliveries();
        writeFiles.writeFlightPath();
        writeFiles.writeGeoJson();
    }
}
