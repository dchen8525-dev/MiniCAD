package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal 3D degenerate curve representation.
 * A degenerate curve collapses to a single point.
 *
 * @param point the single point where the curve degenerates
 */
/**
 * Minimal 3D degenerate curve representation.
 * A degenerate curve collapses to a single point.
 *
 * @param point the single point where the curve degenerates
 */
public final class DegenerateCurve3 implements Curve3 {
    private final CartesianPoint point;

    public DegenerateCurve3(CartesianPoint point) {
        this.point = point;
    }

    public CartesianPoint getPoint() {
        return point;
    }

    // Record-style accessor
    public CartesianPoint point() { return point; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DegenerateCurve3 that = (DegenerateCurve3) o;
        return Objects.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }

    @Override
    public String toString() {
        return "DegenerateCurve3{" + "point=" + point + "}";
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        // A degenerate curve is always the same point
        return point;
    }

    @Override
    public boolean contains(CartesianPoint testPoint) {
        Preconditions.requireNonNull(testPoint, "testPoint");
        if (point == null) {
            return false;
        }
        return testPoint.distanceTo(point) < Epsilon.get();
    }

    @Override
    public CartesianPoint closestPointTo(CartesianPoint testPoint) {
        Preconditions.requireNonNull(testPoint, "testPoint");
        // A degenerate curve always returns its single point
        return point;
    }

    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        if (point == null) {
            return java.util.List.of();
        }
        return java.util.List.of(point);
    }
}