package uk.ac.ed.inf.handler;

import uk.ac.ed.inf.ilp.data.LngLat;

/**
 * Represents a pair of longitude and latitude coordinates.
 * To be used to help cache the paths of a pair of LngLat coordinates.
 */
public class LngLatPair {

    /**
     * The starting LngLat coordinates of the pair.
     */
    private LngLat start;

    /**
     * The ending LngLat coordinates of the pair.
     */
    private LngLat end;

    /**
     * Constructs a new LngLatPair with the given starting and ending LngLat coordinates.
     *
     * @param start The starting LngLat coordinates.
     * @param end   The ending LngLat coordinates.
     */
    public LngLatPair(LngLat start, LngLat end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Computes the hash code for this LngLatPair by combining the hash codes
     * for the start and the end LngLat coordinates.
     *
     * @return The hash code value for the start and end of a path.
     */
    @Override
    public int hashCode() {
        return start.hashCode() ^ end.hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one within hash.
     *
     * @param obj The reference object with which to compare.
     * @return {@code true} if this object is equal to the obj argument;
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        LngLatPair other = (LngLatPair) obj;
        return start.equals(other.start) && end.equals(other.end);
    }
}
