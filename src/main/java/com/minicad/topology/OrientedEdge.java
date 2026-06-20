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

    // Record-style accessors
    public Edge edge() { return getEdge(); }
    public boolean orientation() { return isOrientation(); }

    /**
     * Returns the start vertex considering orientation.
     * If orientation is true, returns edge.start(); otherwise returns edge.end().
     *
     * @return start vertex
     */
    public Vertex startVertex() {
        return orientation ? edge.getStart() : edge.getEnd();
    }

    /**
     * Returns the end vertex considering orientation.
     * If orientation is true, returns edge.end(); otherwise returns edge.start().
     *
     * @return end vertex
     */
    public Vertex endVertex() {
        return orientation ? edge.getEnd() : edge.getStart();
    }

    /**
     * Returns both vertices as a list: [start, end].
     *
     * @return list of vertices
     */
    public java.util.List<Vertex> vertices() {
        return java.util.List.of(startVertex(), endVertex());
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

    /**
     * Returns the length of this oriented edge.
     *
     * @return length of the underlying edge
     */
    public double length() {
        return edge != null ? edge.length() : 0.0;
    }

    /**
     * Returns the closest point on this oriented edge to a given point.
     *
     * @param point the target point
     * @return closest point on the edge
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        return edge != null ? edge.closestPointTo(point) : null;
    }

    /**
     * Returns the distance from this oriented edge to a given point.
     *
     * @param point the target point
     * @return distance to the closest point
     */
    public double distanceTo(CartesianPoint point) {
        return edge != null ? edge.distanceTo(point) : Double.MAX_VALUE;
    }
}
