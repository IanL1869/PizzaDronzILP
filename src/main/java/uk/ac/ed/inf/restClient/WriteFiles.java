package uk.ac.ed.inf.restClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import uk.ac.ed.inf.pathfinder.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WriteFiles {


    private List<Point> flightpaths;
    private String orderDate;
    private List<DeliveriesJSON> deliveriesJSON;

    public WriteFiles(List<DeliveriesJSON> deliveriesJSON, String orderDate, List<Point> flightpaths){
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
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("resultfiles/flightpath-" + orderDate + ".json");

            List<FlightpathJSON> flightpathJSONS = new ArrayList<>();

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

                    flightpathJSONS.add(flightpathJSON);

                }


            }
            mapper.writeValue(new FileWriter(file), flightpathJSONS);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeDeliveries(){

        try{
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("resultfiles/deliveries-" + orderDate + ".json");

            mapper.writeValue(new FileWriter(file), deliveriesJSON);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }

    public void writeGeoJson() {

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode featureCollection = mapper.createObjectNode();
        featureCollection.put("type", "FeatureCollection");
        ArrayNode features = mapper.createArrayNode();
        ObjectNode feature = mapper.createObjectNode();
        feature.put("type", "Feature");

        ObjectNode geometry = mapper.createObjectNode();
        geometry.put("type", "LineString");

        ArrayNode coordinates = mapper.createArrayNode();

        for(Point flightpath: flightpaths){
            ArrayNode coordinate = mapper.createArrayNode();
            coordinate.add(flightpath.getLngLat().lng());
            coordinate.add(flightpath.getLngLat().lat());
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

//        List<LngLatAlt> coordinates = new ArrayList<>();
//
//
//        for (Point flightPath: flightpaths){
//            coordinates.add(new LngLatAlt(flightPath.getLngLat().lng(), flightPath.getLngLat().lat()));
//
//        }
//
//        LineString lineString = new LineString();
//        lineString.setCoordinates(coordinates);
//
//        Feature feature = new Feature();
//        feature.setGeometry(lineString);
//
//        FeatureCollection featureCollection = new FeatureCollection();
//        featureCollection.add(feature);


//        JSONObject featureCollection = new JSONObject();
//        JSONArray features = new JSONArray();
//        JSONObject feature = new JSONObject();
//        JSONObject geometry = new JSONObject();
//        JSONArray coordinates = new JSONArray();

//        var featuresList = new ArrayList<>();
//        var flightPath = new ArrayList<LngLat>(flightpaths.size());
//
//        for(Point move : flightpaths){
//            flightPath.add(move.getLngLat());


        //}


//        try{ FileWriter filewriter = new FileWriter("resultfiles/drone-" + date + ".geojson");
//            filewriter.write(FeatureCollection.fromFeatures(Feature.fromGeometry(LineString.fromLngLats(coordinates))).toJson();
//
//            filewriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        JSONObject featureCollection = new JSONObject();
//
//
//        JSONArray features = new JSONArray();  // Correct key is "features"
//
//        JSONObject feature = new JSONObject();
//
//        features.put(feature);  // Add the Feature to the "features" array
//
//        JSONObject geometry = new JSONObject();
//
//
//        JSONArray coordinates = new JSONArray();
//
//        for (Point flightpath : flightpaths) {
//            coordinates.put(new JSONArray().put(flightpath.getLngLat().lng()).put(flightpath.getLngLat().lat()));
//        }
//
//        geometry.put("coordinates", coordinates);
//        feature.put("geometry", geometry);
//
//        featureCollection.put("features", features);  // Correct key is "features"
//        geometry.put("type", "LineString");
//
//        feature.put("type", "Feature");
//        featureCollection.put("type", "FeatureCollection");
//
//
//
//        try (FileWriter fileWriter = new FileWriter("resultfiles/drone-" + orderDate + ".geojson")) {
//            fileWriter.write(featureCollection.toString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
