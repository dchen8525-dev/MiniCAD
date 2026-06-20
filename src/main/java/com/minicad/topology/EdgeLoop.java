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
        if (edges == null || edges.isEmpty()) {
            throw new TopologyException("edge loop requires at least one edge");
        }
        for (int i = 0; i < edges.size(); i++) {
            OrientedEdge current = edges.get(i);
            OrientedEdge next = edges.get((i + 1) % edges.size());
            double gap = current.endVertex().point().distanceTo(next.startVertex().point());
            if (gap > Epsilon.IMPORT_TOPOLOGY_TOLERANCE) {
                throw new TopologyException("edge loop must be connected and closed between edge " + i
                    + " and edge " + ((i + 1) % edges.size()) + "; gap " + gap
                    + " exceeds tolerance " + Epsilon.IMPORT_TOPOLOGY_TOLERANCE);
            }
        }
        this.edges = java.util.List.copyOf(edges);
    }

    public List<OrientedEdge> getEdges() {
        return edges;
    }

    // Record-style accessor
    public List<OrientedEdge> edges() { return getEdges(); }

    /**
     * Returns the number of edges in this loop.
     *
     * @return edge count
     */
    public int edgeCount() {
        return edges == null ? 0 : edges.size();
    }

    /**
     * Returns the vertices of this loop in traversal order.
     *
     * @return list of vertices
     */
    public List<Vertex> vertices() {
        if (edges == null || edges.isEmpty()) {
            return java.util.List.of();
        }
        java.util.List<Vertex> result = new java.util.ArrayList<>();
        for (OrientedEdge oe : edges) {
            Vertex start = oe.startVertex();
            if (start != null) {
                result.add(start);
            }
        }
        return java.util.List.copyOf(result);
    }

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

    @Override
    public BoundingBox3 boundingBox() {
        if (edges == null || edges.isEmpty()) {
            return BoundingBox3.empty();
        }
        BoundingBox3 box = BoundingBox3.empty();
        for (OrientedEdge oe : edges) {
            Edge edge = oe.getEdge();
            if (edge != null) {
                // Include start and end vertices
                if (edge.getStart() != null) {
                    box = box.expand(edge.getStart().point());
                }
                if (edge.getEnd() != null) {
                    box = box.expand(edge.getEnd().point());
                }
                box = box.union(edge.boundingBox());
            }
        }
        return box;
    }
}
