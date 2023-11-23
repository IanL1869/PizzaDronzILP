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

        LngLat[] vertices = region.vertices();
        int count = 0;
        double positionLon = position.lng();
        double positionLat = position.lat();

//      Checks each set of edges against the point for intersections
        for (int i = 0; i < region.vertices().length; i++) {

//          Gets pair of vertices joined by edge
            double currentVertexLong = vertices[i].lng();
            double currentVertexLat = vertices[i].lat();

            double nextVertexLong = vertices[(i + 1) % vertices.length].lng();
            double nextVertexLat = vertices[(i + 1) % vertices.length].lat();

//          Checks if point is vertex
            if (Arrays.asList(vertices).contains(position)) {
                return true;
            }

//          Check if point is between equal y values
            if ((positionLat == currentVertexLat && positionLat == nextVertexLat && ((positionLon < currentVertexLong) != (positionLon < nextVertexLong)))) {
                return true;
            }

//          Raycast
            if ((positionLat < currentVertexLat) != (positionLat < nextVertexLat)) {
                double v = currentVertexLong + (((positionLat - currentVertexLat) / (nextVertexLat - currentVertexLat)) * (nextVertexLong - currentVertexLong));
//              If x values are equal
                if (positionLon == v) {
                    return true;
                } else if (positionLon < v) {
                    count++;
                }
            }
        }
        return count%2==1;
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
