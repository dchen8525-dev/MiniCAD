package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;

import java.util.List;

/**
 * Loop represented directly by polygon vertices.
 *
 * @param points polygon vertices in order
 */
public record PolyLoop(List<CartesianPoint> points) implements Loop {

    public PolyLoop {
        if (points == null) {
            throw new TopologyException("poly loop points must not be null");
        }
        points = List.copyOf(points);
        if (points.size() < 3) {
            throw new TopologyException("poly loop must contain at least three points");
        }
        for (CartesianPoint point : points) {
            if (point == null) {
                throw new TopologyException("poly loop points must not contain null");
            }
        }
    }

    /**
     * Returns the bounding box enclosing all polygon vertices.
     *
     * @return bounding box enclosing the polygon
     */
    public BoundingBox3 boundingBox() {
        return BoundingBox3.of(points);
    }

    /**
     * Returns the number of vertices in the polygon.
     *
     * @return vertex count
     */
    public int vertexCount() {
        return points.size();
    }

    /**
     * Returns the total perimeter length of the polygon.
     *
     * @return perimeter length
     */
    public double perimeter() {
        double totalLength = 0.0;
        int n = points.size();
        for (int i = 0; i < n; i++) {
            totalLength += points.get(i).distanceTo(points.get((i + 1) % n));
        }
        return totalLength;
    }

    /**
     * Returns the centroid of the polygon.
     *
     * @return centroid point
     */
    public CartesianPoint centroid() {
        double cx = 0.0;
        double cy = 0.0;
        double cz = 0.0;
        int n = points.size();
        for (CartesianPoint p : points) {
            cx += p.x();
            cy += p.y();
            cz += p.z();
        }
        return new CartesianPoint(cx / n, cy / n, cz / n);
    }

    /**
     * Returns the closest point on the polygon to a given point.
     *
     * @param point target point
     * @return closest point on the polygon edges
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        CartesianPoint closest = points.get(0);
        double minDistance = point.distanceTo(closest);
        int n = points.size();
        for (int i = 0; i < n; i++) {
            CartesianPoint p1 = points.get(i);
            CartesianPoint p2 = points.get((i + 1) % n);
            CartesianPoint segmentClosest = closestPointOnSegment(p1, p2, point);
            double distance = point.distanceTo(segmentClosest);
            if (distance < minDistance) {
                minDistance = distance;
                closest = segmentClosest;
            }
        }
        return closest;
    }

    /**
     * Returns the distance from a point to the polygon.
     *
     * @param point target point
     * @return minimum distance to the polygon
     */
    public double distanceTo(CartesianPoint point) {
        return point.distanceTo(closestPointTo(point));
    }

    private static CartesianPoint closestPointOnSegment(CartesianPoint start, CartesianPoint end, CartesianPoint point) {
        com.minicad.geometry.Vector3 segment = end.subtract(start);
        if (segment.norm() <= com.minicad.common.Epsilon.EPS) {
            return start;
        }
        double t = point.subtract(start).dot(segment) / segment.dot(segment);
        t = Math.max(0.0, Math.min(1.0, t));
        return start.add(segment.scale(t));
    }
}
