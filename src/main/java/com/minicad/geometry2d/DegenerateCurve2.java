package com.minicad.geometry2d;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.List;

/**
 * Minimal 2D degenerate curve representation.
 * A degenerate curve collapses to a single point.
 *
 * @param point the single point where the curve degenerates
 */
public record DegenerateCurve2(Point2 point) implements Curve2 {

    public DegenerateCurve2 {
        Preconditions.requireNonNull(point, "point");
    }

    @Override
    public boolean contains(Point2 other) {
        Preconditions.requireNonNull(other, "other");
        return point.equals(other);
    }

    @Override
    public List<Point2> sample(int segments) {
        return List.of(point, point);
    }

    @Override
    public BoundingBox2 boundingBox() {
        return BoundingBox2.of(point, point);
    }

    @Override
    public Vector2 tangentAt(double parameter) {
        throw new GeometryException("degenerate curve has no meaningful tangent");
    }

    @Override
    public double length() {
        return 0.0;
    }

    @Override
    public Point2 closestPointTo(Point2 query) {
        Preconditions.requireNonNull(query, "query");
        return point;
    }

    /**
     * Returns the parameter value for the degenerate curve.
     * Always returns 0.0 as the curve collapses to a single point.
     *
     * @param query point to project (ignored)
     * @return parameter value (always 0.0)
     */
    public double parameterOf(Point2 query) {
        return 0.0;
    }
}
