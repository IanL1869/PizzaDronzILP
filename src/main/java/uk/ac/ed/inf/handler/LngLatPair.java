package uk.ac.ed.inf.handler;

import uk.ac.ed.inf.ilp.data.LngLat;

public class LngLatPair {

    private LngLat start;

    private LngLat end;

    public LngLatPair(LngLat start, LngLat end){
        this.start = start;
        this.end = end;


    }

    @Override
    public int hashCode(){
        return start.hashCode() ^ end.hashCode();

    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        LngLatPair other = (LngLatPair) obj;
        return start.equals(other.start) && end.equals(other.end);
    }

    public LngLat getStart() {
        return start;
    }

    public LngLat getEnd() {
        return end;
    }

}
