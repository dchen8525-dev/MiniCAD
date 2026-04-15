package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;

/**
 * Use of an edge with an explicit orientation inside a loop.
 *
 * @param edge referenced base edge
 * @param orientation true for forward, false for reversed
 */
public record OrientedEdge(Edge edge, boolean orientation) {

    /**
     * Creates an oriented edge.
     */
    public OrientedEdge {
        if (edge == null) {
            throw new TopologyException("edge must not be null");
        }
    }

    /**
     * Returns the effective start vertex under the orientation.
     *
     * @return oriented start vertex
     */
    public Vertex startVertex() {
        return orientation ? edge.start() : edge.end();
    }

    /**
     * Returns the effective end vertex under the orientation.
     *
     * @return oriented end vertex
     */
    public Vertex endVertex() {
        return orientation ? edge.end() : edge.start();
    }

    /**
     * Returns the bounding box of the underlying edge.
     *
     * @return bounding box enclosing the edge
     */
    public BoundingBox3 boundingBox() {
        return edge.boundingBox();
    }

    /**
     * Returns the length of the underlying edge.
     *
     * @return edge length
     */
    public double length() {
        return edge.length();
    }

    /**
     * Returns the closest point on the edge to a given point.
     *
     * @param point target point
     * @return closest point on the edge
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        return edge.closestPointTo(point);
    }

    /**
     * Returns the distance from a point to the edge.
     *
     * @param point target point
     * @return minimum distance to the edge
     */
    public double distanceTo(CartesianPoint point) {
        return edge.distanceTo(point);
    }
}
