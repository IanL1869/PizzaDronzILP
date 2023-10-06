package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

public class LongLatHandle implements LngLatHandling {
    @Override
    public double distanceTo(LngLat startPosition, LngLat endPosition) {

        double longDist = Math.pow(startPosition.lng() - endPosition.lng(), 2 );
        double latDist = Math.pow(startPosition.lat() - endPosition.lat(), 2 );


        return Math.sqrt(longDist + latDist);
    }

    @Override
    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {

        return distanceTo(startPosition, otherPosition) < 0.00015;
    }



    @Override
    public boolean isInRegion(LngLat position, NamedRegion region) {

        LngLat[] vertices = region.vertices();
        int count = 0;
        double positionLon = position.lng();
        double positionLat = position.lat();

        for (int i = 0; i < vertices.length; i++){

            LngLat currentVertex = vertices[i];
            LngLat nextVertex = vertices[(i + 1) % vertices.length];

            if ((positionLat < currentVertex.lat()) != (positionLat < nextVertex.lat())){
                double ip = currentVertex.lng() + ((positionLat - currentVertex.lat())/ (nextVertex.lat() - currentVertex.lat()) * (nextVertex.lng() - currentVertex.lng()));
                    if (ip == positionLon){
                        return true;
                    }else if (positionLat < ip){
                        count = count + 1;

                    }


                }


        }

        return count % 2 == 1;

    }

    @Override
    public LngLat nextPosition(LngLat startPosition, double angle) {

        double newLong = 0.00015 * Math.cos(angle) + startPosition.lng();
        double newLat = 0.00015 * Math.sin(angle) + startPosition.lat();

        LngLat newPosition = new LngLat(newLong, newLat);

        if (angle == 999){
            return startPosition;
        }else {
            return newPosition;
        }

    }
}
