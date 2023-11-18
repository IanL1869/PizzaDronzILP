package uk.ac.ed.inf.restClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URL;

public class RestClient {

    private String baseURL;
    private String orderDate;


    public RestClient(String baseURL, String orderDate){
        this.baseURL = baseURL;
        this.orderDate = orderDate;
    }

    public Restaurant[] getRestaurants() throws IOException {


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Restaurant[] restaurant_list;
        try {

            restaurant_list = mapper.readValue(new URL(baseURL + "restaurants"), Restaurant[].class);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        return restaurant_list;
    }


    public Order[] getOrdersOnDate() throws IOException{

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Order[] ordersOnDate;

        try{
            ordersOnDate = mapper.readValue(new URL (baseURL + "orders" + "/" + orderDate), Order[].class);

        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return ordersOnDate;

    }

    public NamedRegion getCentralArea() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        NamedRegion centralArea;

        try{
            centralArea = mapper.readValue(new URL (baseURL + "centralArea"), NamedRegion.class);

        }catch (IOException e){

            throw new RuntimeException(e);
        }

        return centralArea;
    }

    public NamedRegion[] getNoFlyZones() throws IOException{

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        NamedRegion[] noFlyZones;

        try{
            noFlyZones = mapper.readValue(new URL (baseURL + "noFlyZones"), NamedRegion[].class);

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
