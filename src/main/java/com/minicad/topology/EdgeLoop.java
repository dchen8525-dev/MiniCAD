package com.minicad.topology;

import com.minicad.common.Epsilon;
import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;

import java.util.List;
import java.util.Objects;

/**
 * Ordered closed loop of oriented edges.
 *
 * @param edges oriented edges in traversal order
 */
/**
 * Ordered closed loop of oriented edges.
 *
 * @param edges oriented edges in traversal order
 */
public final class EdgeLoop implements Loop {
    private final List<OrientedEdge> edges;

    public EdgeLoop(List<OrientedEdge> edges) {
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
    }

    public List<OrientedEdge> getEdges() {
        return edges;
    }

    // Record-style accessor
    public List<OrientedEdge> edges() { return getEdges(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeLoop that = (EdgeLoop) o;
        return Objects.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edges);
    }

    @Override
    public String toString() {
        return "EdgeLoop{" + "edges=" + edges + "}";
    }
}
