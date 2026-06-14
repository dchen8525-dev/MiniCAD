package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;
import java.util.Objects;

/**
 * Use of an edge with an explicit orientation inside a loop.
 *
 * @param edge referenced base edge
 * @param orientation true for forward, false for reversed
 */
/**
 * Use of an edge with an explicit orientation inside a loop.
 *
 * @param edge referenced base edge
 * @param orientation true for forward, false for reversed
 */
public final class OrientedEdge {
    private final Edge edge;
    private final boolean orientation;

    public OrientedEdge(Edge edge, boolean orientation) {
        this.edge = edge;
        this.orientation = orientation;
    }

    public Edge getEdge() {
        return edge;
    }

    public boolean isOrientation() {
        return orientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrientedEdge that = (OrientedEdge) o;
        return Objects.equals(edge, that.edge) && orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(edge, orientation);
    }

    @Override
    public String toString() {
        return "OrientedEdge{" + "edge=" + edge + "orientation=" + orientation + "}";
    }
}
