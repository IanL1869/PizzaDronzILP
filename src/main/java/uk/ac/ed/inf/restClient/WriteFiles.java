package uk.ac.ed.inf.restClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * The WriteFiles class provides methods to write delivery and flight path information
 * to JSON and GeoJSON files for a given order date.
 */
public class WriteFiles {

    /**
     * List of flight paths objects to be written.
     */
    private List<FlightpathJSON> flightpathsJSON;

    /**
     * The order date for which files are being written.
     */
    private String orderDate;

    /**
     * List of delivery information to be written.
     */
    private List<DeliveriesJSON> deliveriesJSON;

    /**
     * ObjectMapper for converting Java objects to JSON.
     */
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructs a WriteFiles object with delivery, order date, and flight path information.
     *
     * @param deliveriesJSON List of delivery information for order date.
     * @param orderDate      The order date for which files are being written.
     * @param flightpathsJSON    List of flight paths for order date.
     */
    public WriteFiles(List<DeliveriesJSON> deliveriesJSON, String orderDate, List<FlightpathJSON> flightpathsJSON){
        this.deliveriesJSON = deliveriesJSON;
        this.orderDate = orderDate;
        this.flightpathsJSON = flightpathsJSON;
        File theDir = new File("resultfiles");

        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    /**
     * Writes flight path information to a JSON fil.
     */
    public void writeFlightPath(){

        try{

            File file = new File("resultfiles/flightpath-" + orderDate + ".json");
            mapper.writeValue(new FileWriter(file), flightpathsJSON);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes delivery information to a JSON file.
     */
    public void writeDeliveries(){

        try{

            File file = new File("resultfiles/deliveries-" + orderDate + ".json");
            mapper.writeValue(new FileWriter(file), deliveriesJSON);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }

    /**
     * Writes drone flight path information to a GeoJSON file.
     */
    public void writeDrone() {

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

        for(FlightpathJSON flightpath: flightpathsJSON){
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
