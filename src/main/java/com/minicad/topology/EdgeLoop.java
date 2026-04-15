package com.minicad.topology;

import com.minicad.common.Epsilon;
import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;

import java.util.List;

/**
 * Ordered closed loop of oriented edges.
 *
 * @param edges oriented edges in traversal order
 */
public record EdgeLoop(List<OrientedEdge> edges) implements Loop {

    /**
     * Creates a loop and validates closure/connectivity.
     */
    public EdgeLoop {
        if (edges == null) {
            throw new TopologyException("edges must not be null");
        }
        if (edges.isEmpty()) {
            throw new TopologyException("edges must not be empty");
        }
        edges = List.copyOf(edges);

        for (int i = 0; i < edges.size(); i++) {
            OrientedEdge current = edges.get(i);
            if (current == null) {
                throw new TopologyException("edges must not contain null");
            }

            OrientedEdge next = edges.get((i + 1) % edges.size());
            double gap = current.endVertex().point().distanceTo(next.startVertex().point());
            if (gap > Epsilon.EPS) {
                throw new TopologyException("edge loop must be connected and closed");
            }
        }
    }

    /**
     * Returns the bounding box enclosing all edges in the loop.
     *
     * @return bounding box enclosing the loop
     */
    public BoundingBox3 boundingBox() {
        BoundingBox3 box = BoundingBox3.empty();
        for (OrientedEdge edge : edges) {
            box = box.union(edge.boundingBox());
        }
        return box;
    }

    /**
     * Returns the total length of all edges in the loop.
     *
     * @return total perimeter length
     */
    public double perimeter() {
        double totalLength = 0.0;
        for (OrientedEdge edge : edges) {
            totalLength += edge.length();
        }
        return totalLength;
    }

    /**
     * Returns the count of edges in the loop.
     *
     * @return edge count
     */
    public int edgeCount() {
        return edges.size();
    }

    /**
     * Returns all unique vertices in the loop.
     *
     * @return list of unique vertices
     */
    public List<Vertex> vertices() {
        java.util.Set<Vertex> vertexSet = new java.util.LinkedHashSet<>();
        for (OrientedEdge edge : edges) {
            vertexSet.add(edge.startVertex());
            vertexSet.add(edge.endVertex());
        }
        return List.copyOf(vertexSet);
    }

    /**
     * Returns the centroid of the loop (average of vertices).
     *
     * @return centroid point
     */
    public CartesianPoint centroid() {
        List<Vertex> uniqueVertices = vertices();
        double cx = 0.0;
        double cy = 0.0;
        double cz = 0.0;
        for (Vertex v : uniqueVertices) {
            cx += v.point().x();
            cy += v.point().y();
            cz += v.point().z();
        }
        int n = uniqueVertices.size();
        return new CartesianPoint(cx / n, cy / n, cz / n);
    }

    /**
     * Returns the closest point on the loop to a given point.
     *
     * @param point target point
     * @return closest point on the loop edges
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        CartesianPoint closest = edges.get(0).closestPointTo(point);
        double minDistance = point.distanceTo(closest);
        for (int i = 1; i < edges.size(); i++) {
            CartesianPoint edgeClosest = edges.get(i).closestPointTo(point);
            double distance = point.distanceTo(edgeClosest);
            if (distance < minDistance) {
                minDistance = distance;
                closest = edgeClosest;
            }
        }
        return closest;
    }

    /**
     * Returns the distance from a point to the loop.
     *
     * @param point target point
     * @return minimum distance to the loop
     */
    public double distanceTo(CartesianPoint point) {
        return point.distanceTo(closestPointTo(point));
    }
}
