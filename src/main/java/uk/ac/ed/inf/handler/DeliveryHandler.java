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

    private final String baseURL;
    private final String orderDate;
    private final RestClient restClient;
    private List<Order> validOrders = new ArrayList<>();
    private Map<LngLatPair, List<FlightpathJSON>> cachedPaths = new HashMap<>();
    private final LngLat appletonTower = new LngLat(-3.186874,55.944494);



    public DeliveryHandler(String baseURL, String orderDate) throws IOException {
        this.baseURL = baseURL;
        this.orderDate = orderDate;
        this.restClient = new RestClient(baseURL, orderDate);
    }



    public List<DeliveriesJSON> getValidDeliveries() throws IOException {
        Restaurant[] restaurants = restClient.getRestaurants();
        Order[] orders = restClient.getOrdersOnDate();


        List<DeliveriesJSON> validatedDeliveries = new ArrayList<>();

        for (Order order : orders) {

            OrderVal orderToValidate = new OrderVal();
            orderToValidate.validateOrder(order, restaurants);

            if (order.getOrderValidationCode().equals(OrderValidationCode.NO_ERROR)) {
                order.setOrderStatus(OrderStatus.DELIVERED);


                validOrders.add(order);

                DeliveriesJSON orderJSON = new DeliveriesJSON(
                        order.getOrderNo(),
                        order.getOrderStatus().toString(),
                        order.getOrderValidationCode().toString(),
                        order.getPriceTotalInPence()
                );
                validatedDeliveries.add(orderJSON);
            }
        }

        return validatedDeliveries;
    }


    public List<FlightpathJSON> getFlightPaths() throws IOException {

        Restaurant[] restaurants = restClient.getRestaurants();
        NamedRegion[] noFlyZones = restClient.getNoFlyZones();
        NamedRegion centralArea = restClient.getCentralArea();


        List<FlightpathJSON> flightPaths = new ArrayList<>();

        for (Order order: validOrders){

            Restaurant restaurant = getRestaurant(order, restaurants);

            if(restaurant != null && order.getOrderNo() != null){

                LngLatPair toRestaurantKey = new LngLatPair(appletonTower, restaurant.location());

                if (cachedPaths.containsKey(toRestaurantKey)){
                    flightPaths.addAll(cachedPaths.get(toRestaurantKey));
                } else {
                    PathFinder pathFinderToRest = new PathFinder(
                        order.getOrderNo(),
                        appletonTower,
                        restaurant.location(),
                        noFlyZones,
                        centralArea
                    );

                    cachedPaths.put(toRestaurantKey, pathFinderToRest.aStar());
                    flightPaths.addAll(pathFinderToRest.aStar());
                }

                LngLatPair toAppletonKey = new LngLatPair(restaurant.location(), appletonTower);

                if (cachedPaths.containsKey(toAppletonKey)){
                    flightPaths.addAll(cachedPaths.get(toAppletonKey));
                }
                else {
                    PathFinder pathFinderToAppleton = new PathFinder(
                            order.getOrderNo(),
                            restaurant.location(),
                            appletonTower,
                            noFlyZones,
                            centralArea
                    );
                    cachedPaths.put(toAppletonKey, pathFinderToAppleton.aStar());
                    flightPaths.addAll(pathFinderToAppleton.aStar());
                }

            }


        }

        validOrders = new ArrayList<>();
        return flightPaths;

    }




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
        writeFiles.writeFlightPath();
        writeFiles.writeDeliveries();
        writeFiles.writeGeoJson();
    }
}
