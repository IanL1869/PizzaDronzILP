package uk.ac.ed.inf.handler;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.pathfinder.PathFinder;
import uk.ac.ed.inf.pathfinder.Point;
import uk.ac.ed.inf.restClient.RestClient;
import uk.ac.ed.inf.restClient.WriteFiles;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DeliveryHandler {

    private String baseURL;
    private String orderDate;


    private final LngLat appletonTower = new LngLat(-3.186874,55.944494);

    public DeliveryHandler(String baseURL, String orderDate) throws IOException {
        this.baseURL = baseURL;
        this.orderDate = orderDate;
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

    public void getDelivery() throws IOException {

        RestClient restClient = new RestClient(baseURL, orderDate);
        Restaurant[] restaurants = restClient.getRestaurant();
        NamedRegion[] noFlyZones = restClient.getNoFlyZones();
        NamedRegion centralArea = restClient.getCentralArea();
        Order[] orders = restClient.getOrdersOnDate();

        List<Order> validatedOrders = new ArrayList<>();

        for (Order order : orders) {

            OrderVal orderToValidate = new OrderVal();
            orderToValidate.validateOrder(order, restaurants);

            if (order.getOrderValidationCode().equals(OrderValidationCode.NO_ERROR)){

                validatedOrders.add(order);

                Restaurant restaurant = getRestaurant(order, restaurants);

                PathFinder pathToRestaurant = new PathFinder(order.getOrderNo(),appletonTower, restaurant.location(), noFlyZones, centralArea);
                List<Point> reconPathToRest = pathToRestaurant.aStar();
                order.setOrderStatus(OrderStatus.DELIVERED);

                PathFinder pathToAppleton = new PathFinder(order.getOrderNo(), restaurant.location(),appletonTower,noFlyZones,centralArea);

            }


        }


    }
}
