package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal 3D polyline curve.
 *
 * @param points ordered polyline vertices
 */
/**
 * Minimal 3D polyline curve.
 *
 * @param points ordered polyline vertices
 */
public final class Polyline3 implements Curve3 {
    private final List<CartesianPoint> points;

    public Polyline3(List<CartesianPoint> points) {
        this.points = points == null ? null : java.util.List.copyOf(points);
    }

    public List<CartesianPoint> getPoints() {
        return points;
    }

    // Record-style accessor
    public List<CartesianPoint> points() { return getPoints(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polyline3 that = (Polyline3) o;
        return Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }

    @Override
    public String toString() {
        return "Polyline3{" + "points=" + points + "}";
    }
}
