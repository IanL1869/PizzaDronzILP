package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RestClient {

    public static String baseURL = "https://ilp-rest.azurewebsites.net/";




    public static Restaurant[] getRestaurant() throws IOException {


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


    public static Order[] getOrders() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Order[] orders_list;

        try {
            orders_list = mapper.readValue(new URL(baseURL + "orders"), Order[].class);


        } catch (IOException e){

            throw new RuntimeException(e);

        }

        return orders_list;
    }

    public static NamedRegion getCentralArea() throws IOException{
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

    public static NamedRegion[] getNoFlyZones() throws IOException{

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
    public static void main(String[] args) throws IOException {
        Restaurant[] restaurant = getRestaurant();

        System.out.println(restaurant);
    }

}
