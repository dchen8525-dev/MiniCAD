package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.List;

/**
 * Minimal 2D polyline curve.
 *
 * @param points ordered polyline vertices
 */
public record Polyline2(List<Point2> points) implements Curve2 {

    public Polyline2 {
        points = List.copyOf(points);
        if (points.size() < 2) {
            throw new IllegalArgumentException("polyline must contain at least two points");
        }
        for (Point2 point : points) {
            Preconditions.requireNonNull(point, "points");
        }
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        for (int index = 0; index < points.size() - 1; index++) {
            if (pointOnSegment(points.get(index), points.get(index + 1), point)) {
                return true;
            }
        }
        return false;
    }

    private static boolean pointOnSegment(Point2 start, Point2 end, Point2 point) {
        Vector2 segment = end.subtract(start);
        Vector2 offset = point.subtract(start);
        if (segment.isZero()) {
            return offset.norm() <= Epsilon.EPS;
        }
        double cross = segment.x() * offset.y() - segment.y() * offset.x();
        if (Math.abs(cross) > 1.0e-6) {
            return false;
        }
        double projection = offset.dot(segment);
        return projection >= -Epsilon.EPS && projection <= segment.dot(segment) + Epsilon.EPS;
    }
}
