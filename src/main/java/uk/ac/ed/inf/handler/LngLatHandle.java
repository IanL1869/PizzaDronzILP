package uk.ac.ed.inf.handler;

import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

import java.util.Arrays;

/**
 * The LngLatHandle class implements the LngLatHandling interface and provides
 * methods for handling LngLat coordinates.
 */
public class LngLatHandle implements LngLatHandling {

    /**
     * Calculates the distance between two LngLat coordinates.
     *
     * @param startLngLat The starting geographical position.
     * @param endLngLat   The ending geographical position.
     * @return The distance between the two positions.
     */
    @Override
    public double distanceTo(LngLat startLngLat, LngLat endLngLat) {

        // calculate each difference separately
        double longDist = Math.pow(startLngLat.lng() - endLngLat.lng(), 2 );
        double latDist = Math.pow(startLngLat.lat() - endLngLat.lat(), 2 );

        // return distance between two points
        return Math.sqrt(longDist + latDist);
    }

    /**
     * Checks if a LngLat coordinate is close to another.
     *
     * @param currentLngLat The current LngLat coordinate to check.
     * @param givenLngLat The given LngLat coordinate to compare.
     * @return True if the LngLat coordinates are close, false otherwise.
     */
    @Override
    public boolean isCloseTo(LngLat currentLngLat, LngLat givenLngLat) {

        // check if the distance between two coordinates is less that close distance constant.
        return distanceTo(currentLngLat, givenLngLat) < SystemConstants.DRONE_IS_CLOSE_DISTANCE;

    }

    /**
     * Checks if a LngLat coordinate is within a named region.
     *
     * @param currentLngLat The LngLat coordinate to check.
     * @param region   The named region to check against.
     * @return True if the LngLat coordinate is in the region, false otherwise.
     */
    @Override
    public boolean isInRegion(LngLat currentLngLat, NamedRegion region) {
        // gets vertices of region to check
        LngLat[] vertices = region.vertices();

        // defines the longitude and latitude to check
        double currentLng = currentLngLat.lng();
        double currentLat = currentLngLat.lat();

        // initialises count for raycast algorithm
        int count = 0;
        // checks each set of edges against the point for intersections
        for (int i = 0; i < region.vertices().length; i++) {

            // gets consecutive vertices joined by edge
            double currentVertexLng = vertices[i].lng();
            double currentVertexLat = vertices[i].lat();

            double nextVertexLng = vertices[(i + 1) % vertices.length].lng();
            double nextVertexLat = vertices[(i + 1) % vertices.length].lat();

            // checks if coordinate is on the vertex of region
            if (Arrays.asList(vertices).contains(currentLngLat)) {
                return true;
            }

            // check if coordinate is between equal longitude values
            if ((currentLat == currentVertexLat && currentLat == nextVertexLat && ((currentLng < currentVertexLng) != (currentLng < nextVertexLng)))) {
                return true;
            }

            // raycast algorithm
            if ((currentLat < currentVertexLat) != (currentLat < nextVertexLat)) {

                double v = currentVertexLng + (((currentLat - currentVertexLat) / (nextVertexLat - currentVertexLat)) * (nextVertexLng - currentVertexLng));

                if (currentLng == v) {
                    return true;
                } else if (currentLng < v) {
                    count++;
                }
            }
        }
        return count%2==1;
    }

    /**
     * Calculates the next LngLat coordinate based on the current LngLat coordinate and angle.
     *
     * @param currentLngLat The starting current LngLat coordinate.
     * @param angle         The angle at which to calculate the next LngLat coordinate.
     * @return The next LngLat coordinate.
     */
    @Override
    public LngLat nextPosition(LngLat currentLngLat, double angle) {

        // check if angle is a defined angle given
        if (angle % 22.5 == 0){

            // define new coordinates and return them. Use radians as it's more accurate
            double newLong = SystemConstants.DRONE_MOVE_DISTANCE * Math.cos(Math.toRadians(angle)) + currentLngLat.lng();
            double newLat = SystemConstants.DRONE_MOVE_DISTANCE * Math.sin(Math.toRadians(angle)) + currentLngLat.lat();
            return new LngLat(newLong, newLat);

            // condition when drone is hovering
        }else if (angle == 999){

            return currentLngLat;

        }

        return currentLngLat;

    }
}
