package uk.ac.ed.inf.restClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class WriteFiles {


    private List<FlightpathJSON> flightpaths;
    private String orderDate;
    private List<DeliveriesJSON> deliveriesJSON;
    private ObjectMapper mapper = new ObjectMapper();


    public WriteFiles(List<DeliveriesJSON> deliveriesJSON, String orderDate, List<FlightpathJSON> flightpaths){
        this.deliveriesJSON = deliveriesJSON;
        this.orderDate = orderDate;
        this.flightpaths = flightpaths;
        File theDir = new File("resultfiles");

        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }
    public void writeFlightPath(){

        try{

            File file = new File("resultfiles/flightpath-" + orderDate + ".json");
            mapper.writeValue(new FileWriter(file), flightpaths);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeDeliveries(){

        try{

            File file = new File("resultfiles/deliveries-" + orderDate + ".json");
            mapper.writeValue(new FileWriter(file), deliveriesJSON);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }

    public void writeGeoJson() {

        ObjectNode featureCollection = mapper.createObjectNode();
        featureCollection.put("type", "FeatureCollection");
        ArrayNode features = mapper.createArrayNode();
        ObjectNode feature = mapper.createObjectNode();
        feature.put("type", "Feature");

        ObjectNode properties = mapper.createObjectNode();
        feature.set("properties", properties);

        ObjectNode geometry = mapper.createObjectNode();
        geometry.put("type", "LineString");

        ArrayNode coordinates = mapper.createArrayNode();

        for(FlightpathJSON flightpath: flightpaths){
            ArrayNode coordinate = mapper.createArrayNode();
            coordinate.add(flightpath.getFromLongitude());
            coordinate.add(flightpath.getFromLatitude());
            coordinates.add(coordinate);
        }

        geometry.set("coordinates", coordinates);
        feature.set("geometry", geometry);
        features.add(feature);

        featureCollection.set("features", features);
        File file = new File("resultfiles/drone-" + orderDate + ".geojson");

        try {
            mapper.writeValue(new FileWriter(file), featureCollection);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }

}
