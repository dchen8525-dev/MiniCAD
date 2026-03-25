package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.CartesianPoint;

/**
 * Topological vertex backed by a 3D point.
 *
 * @param point vertex geometry
 */
public record Vertex(CartesianPoint point) {

    /**
     * Creates a vertex.
     */
    public Vertex {
        if (point == null) {
            throw new TopologyException("point must not be null");
        }
    }
}
