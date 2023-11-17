package uk.ac.ed.inf.handler;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.pathfinder.PathFinder;
import uk.ac.ed.inf.pathfinder.Point;
import uk.ac.ed.inf.restClient.RestClient;
import uk.ac.ed.inf.restClient.WriteFiles;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DeliveryHandler {

    private String baseURL;
    private String orderDate;
    private RestClient restClient;



    private final LngLat appletonTower = new LngLat(-3.186874,55.944494);

    public DeliveryHandler(String baseURL, String orderDate) throws IOException {
        this.baseURL = baseURL;
        this.orderDate = orderDate;
        this.restClient = new RestClient(baseURL, orderDate);
    }

    public List<Order> getValidDeliveries() throws IOException {
        RestClient restClient = new RestClient(baseURL, orderDate);
        Restaurant[] restaurants = restClient.getRestaurants();
        Order[] orders = restClient.getOrdersOnDate();

        List<Order> validatedDeliveries = new ArrayList<>();

        for (Order order : orders) {

            OrderVal orderToValidate = new OrderVal();
            orderToValidate.validateOrder(order, restaurants);

            if (order.getOrderValidationCode().equals(OrderValidationCode.NO_ERROR)) {

                validatedDeliveries.add(order);

            }
        }

        return validatedDeliveries;
    }


    public List<Point> getFlightPaths() throws IOException {

        Restaurant[] restaurants = restClient.getRestaurants();
        NamedRegion[] noFlyZones = restClient.getNoFlyZones();
        NamedRegion centralArea = restClient.getCentralArea();

        List<Order> validOrders = getValidDeliveries();
        List<Point> flightPaths = new ArrayList<>();


        for (Order order: validOrders){


            Restaurant restaurant = getRestaurant(order, restaurants);

            if(restaurant != null && order.getOrderNo() != null){

                PathFinder pathFinderToRest = new PathFinder(
                        order.getOrderNo(),
                        appletonTower,
                        restaurant.location(),
                        noFlyZones,
                        centralArea
                );

                flightPaths.addAll(pathFinderToRest.aStar());

                order.setOrderStatus(OrderStatus.DELIVERED);

                PathFinder pathFinderToAppleton = new PathFinder(
                        order.getOrderNo(),
                        restaurant.location(),
                        appletonTower,
                        noFlyZones,
                        centralArea
                );

                flightPaths.addAll(pathFinderToAppleton.aStar());

            }


        }

        return flightPaths;

    }


    public Restaurant getRestaurant(Order validatedOrder, Restaurant[] restaurants){

        if (validatedOrder.getOrderStatus().equals(OrderStatus.VALID_BUT_NOT_DELIVERED)){
            for(Restaurant restaurant: restaurants){

                if (Arrays.asList(restaurant.menu()).contains(validatedOrder.getPizzasInOrder()[0])){
                    return restaurant;
                }
            }
        }
        return null;


    }
}
