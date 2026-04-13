package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.List;

/**
 * Minimal 3D polyline curve.
 *
 * @param points ordered polyline vertices
 */
public record Polyline3(List<CartesianPoint> points) implements Curve3 {

    public Polyline3 {
        points = List.copyOf(points);
        if (points.size() < 2) {
            throw new IllegalArgumentException("polyline must contain at least two points");
        }
        for (CartesianPoint point : points) {
            Preconditions.requireNonNull(point, "points");
        }
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        for (int index = 0; index < points.size() - 1; index++) {
            if (pointOnSegment(points.get(index), points.get(index + 1), point)) {
                return true;
            }
        }
        return false;
    }

    private static boolean pointOnSegment(CartesianPoint start, CartesianPoint end, CartesianPoint point) {
        Vector3 segment = end.subtract(start);
        Vector3 offset = point.subtract(start);
        if (segment.isZero()) {
            return point.distanceTo(start) <= Epsilon.EPS;
        }
        if (segment.cross(offset).norm() > 1.0e-6) {
            return false;
        }
        double projection = offset.dot(segment);
        return projection >= -Epsilon.EPS && projection <= segment.dot(segment) + Epsilon.EPS;
    }
}
