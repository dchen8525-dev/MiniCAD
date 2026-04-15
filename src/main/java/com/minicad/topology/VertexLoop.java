package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;

/**
 * Degenerate loop represented by a single vertex.
 *
 * @param vertex loop vertex
 */
public record VertexLoop(Vertex vertex) implements Loop {

    /**
     * Creates a vertex loop.
     */
    public VertexLoop {
        if (vertex == null) {
            throw new TopologyException("vertex must not be null");
        }
    }

    /**
     * Returns the bounding box of this vertex loop.
     *
     * @return bounding box containing the vertex
     */
    public BoundingBox3 boundingBox() {
        return BoundingBox3.of(vertex.point());
    }

    /**
     * Returns the centroid (same as the vertex point).
     *
     * @return vertex point
     */
    public CartesianPoint centroid() {
        return vertex.point();
    }

    /**
     * Returns the closest point (same as the vertex point).
     *
     * @param point target point
     * @return vertex point
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        return vertex.point();
    }

    /**
     * Returns the distance from a point to this vertex.
     *
     * @param point target point
     * @return distance to the vertex
     */
    public double distanceTo(CartesianPoint point) {
        return point.distanceTo(vertex.point());
    }
}
