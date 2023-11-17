package uk.ac.ed.inf.restClient;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.pathfinder.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WriteFiles {


    private List<Point> flightpaths;
    private String orderDate;
    private List<Order> orders;

    public WriteFiles(List<Order> orders, String orderDate, List<Point> flightpaths){
        this.orders = orders;
        this.orderDate = orderDate;
        this.flightpaths = flightpaths;
        File theDir = new File("resultfiles");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }
    public void writeFlightPath(){

        try{
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("resultfiles/flightpath-" + orderDate + ".json");

            for(Point flightpath: flightpaths){
                if (flightpath.getPreviousPoint() != null){
                    FlightpathJSON flightpathJSON = new FlightpathJSON(
                            flightpath.getOrderNo(),
                            flightpath.getPreviousPoint().getLngLat().lng(),
                            flightpath.getPreviousPoint().getLngLat().lat(),
                            flightpath.getAngle(),
                            flightpath.getLngLat().lng(),
                            flightpath.getLngLat().lat()
                    );
                    mapper.writeValue(new FileWriter(file), flightpathJSON);
                }


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeDeliveries(){

        try{
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("resultfiles/deliveries-" + orderDate + ".json");

            for(Order order: orders) {
                DeliveriesJSON deliveriesJSON = new DeliveriesJSON(
                        order.getOrderNo(),
                        order.getOrderStatus().toString(),
                        order.getOrderValidationCode().toString(),
                        order.getPriceTotalInPence()
                );

                mapper.writeValue(new FileWriter(file), deliveriesJSON);

            }
        } catch (IOException e) {

            throw new RuntimeException(e);
        }


    }

//    public void writeGeoJson(){
//        try{
//            ObjectMapper mapper = new ObjectMapper();
//            File file = new File("resultfiles/drone-" + orderDate + ".geojson");
//
//            for(Point flightpath : flightpaths){
//
//                LngLat lngLat = new LngLat(flightpath.getLngLat().lng(), flightpath.getLngLat().lat());
//                mapper
//
//
//            }




}
