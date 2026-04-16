package com.minicad.geometry2d;

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
}
