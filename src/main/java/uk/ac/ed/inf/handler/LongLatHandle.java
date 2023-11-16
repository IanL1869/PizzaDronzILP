package uk.ac.ed.inf.handler;

import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

import java.util.Arrays;

public class LongLatHandle implements LngLatHandling {
    @Override
    public double distanceTo(LngLat startPosition, LngLat endPosition) {

        // calculate each difference separately
        double longDist = Math.pow(startPosition.lng() - endPosition.lng(), 2 );
        double latDist = Math.pow(startPosition.lat() - endPosition.lat(), 2 );

        // return distance between two points
        return Math.sqrt(longDist + latDist);
    }

    @Override
    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {

        // check if the distance between two points is less that close distance constant
        return distanceTo(startPosition, otherPosition) < SystemConstants.DRONE_IS_CLOSE_DISTANCE;

    }

    @Override
    public boolean isInRegion(LngLat position, NamedRegion region) {
        int count = 0;
        double xp = position.lng();
        double yp = position.lat();
        LngLat[] vertices = region.vertices();

//      Checks each set of edges against the point for intersections
        for (int i = 0; i < region.vertices().length; i++) {

//          Gets pair of vertices joined by edge
            double x1 = vertices[i].lng();
            double y1 = vertices[i].lat();

            double x2 = vertices[(i + 1) % vertices.length].lng();
            double y2 = vertices[(i + 1) % vertices.length].lat();

//          Checks if point is vertex
            if (Arrays.asList(vertices).contains(position)) {
                return true;
            }

//          Check if point is between equal y values
            if ((yp == y1 && yp == y2 && ((xp < x1) != (xp < x2)))) {
                return true;
            }

//          Raycast
            if ((yp < y1) != (yp < y2)) {
                double v = x1 + (((yp - y1) / (y2 - y1)) * (x2 - x1));
//              If x values are equal
                if (xp == v) {
                    return true;
                } else if (xp < v) {
                    count++;
                }
            }
        }
        return count%2==1;
    }

    public boolean isInRegionT(LngLat position, NamedRegion region) {

        // initialsie array of coordinates as the regions vertices
        LngLat[] vertices = region.vertices();

        // set the passed in position coordinates to variables
        double positionLon = position.lng();
        double positionLat = position.lat();

        // set count to 0 and loop through all region vertices
        int count = 0;

        for (int i = 0; i < vertices.length; i++){

            // set variables that check consecutive vertex
            LngLat currentVertex = vertices[i];
            LngLat nextVertex = vertices[(i + 1) % vertices.length];    // Use modular arithmetic so that last vertex connects with first


            // case for when position is on a vertex of a region
            if ((positionLat == currentVertex.lat() && positionLon == currentVertex.lng()) || (positionLat == nextVertex.lat() && positionLon == nextVertex.lng())){
                return true;

                // case for rectangle/square top or bottom line
            } else if ((positionLat == currentVertex.lat() && positionLat == nextVertex.lat()) || (positionLon < currentVertex.lng() && positionLon < nextVertex.lng())){
                return true;

                // ray casting algorithm to determine if position is within any polygon
            } else if ((positionLat < currentVertex.lat()) != (positionLat < nextVertex.lat())){

                double ip = currentVertex.lng() + ((positionLat - currentVertex.lat())/ (nextVertex.lat() - currentVertex.lat()) * (nextVertex.lng() - currentVertex.lng()));

                // ray intersects with edge so increase the count
                if (positionLat <= ip){

                    count = count + 1;

                }
            }

        }
        // if the odd then it is in the region. if even it is outwith the region
        return count % 2 == 1;

    }

    @Override
    public LngLat nextPosition(LngLat startPosition, double angle) {

        // check if angle is a defined angle given
        if (angle % 22.5 == 0){

            // define new coordinates and return them. Use radians as its more accurate
            double newLong = SystemConstants.DRONE_MOVE_DISTANCE * Math.cos(Math.toRadians(angle)) + startPosition.lng();
            double newLat = SystemConstants.DRONE_MOVE_DISTANCE * Math.sin(Math.toRadians(angle)) + startPosition.lat();
            return new LngLat(newLong, newLat);

            // condition when drone is hovering
        }else if (angle == 999){

            return startPosition;

        }

        return startPosition;

    }
}
