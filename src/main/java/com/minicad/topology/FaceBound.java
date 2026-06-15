package com.minicad.topology;

import com.minicad.common.TopologyException;
import java.util.Objects;

/**
 * Face boundary based on a loop subtype.
 *
 * @param loop boundary loop
 * @param orientation orientation relative to the face
 * @param outer whether this is the outer boundary
 */
/**
 * Face boundary based on a loop subtype.
 *
 * @param loop boundary loop
 * @param orientation orientation relative to the face
 * @param outer whether this is the outer boundary
 */
public final class FaceBound {
    private final Loop loop;
    private final boolean orientation;
    private final boolean outer;

    public FaceBound(Loop loop, boolean orientation, boolean outer) {
        this.loop = loop;
        this.orientation = orientation;
        this.outer = outer;
    }

    public Loop getLoop() {
        return loop;
    }

    public boolean isOrientation() {
        return orientation;
    }

    public boolean isOuter() {
        return outer;
    }

    // Record-style accessors
    public Loop loop() { return getLoop(); }
    public boolean orientation() { return isOrientation(); }
    public boolean outer() { return isOuter(); }

    /**
     * Creates a FaceBound with a given loop and outer flag, with default orientation true.
     *
     * @param loop the loop
     * @param outer whether this is an outer boundary
     * @return new FaceBound
     */
    public static FaceBound outer(Loop loop, boolean outer) {
        return new FaceBound(loop, true, outer);
    }

    /**
     * Creates an inner FaceBound with a given loop and orientation.
     *
     * @param loop the loop
     * @param orientation orientation relative to the face
     * @return new FaceBound marked as inner
     */
    public static FaceBound inner(Loop loop, boolean orientation) {
        return new FaceBound(loop, orientation, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FaceBound that = (FaceBound) o;
        return Objects.equals(loop, that.loop) && orientation == that.orientation && outer == that.outer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(loop, orientation, outer);
    }

    @Override
    public String toString() {
        return "FaceBound{" + "loop=" + loop + "orientation=" + orientation + "outer=" + outer + "}";
    }
}
