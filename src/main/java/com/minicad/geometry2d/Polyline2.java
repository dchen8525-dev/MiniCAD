package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal 2D polyline curve.
 *
 * @param points ordered polyline vertices
 */
/**
 * Minimal 2D polyline curve.
 *
 * @param points ordered polyline vertices
 */
public final class Polyline2 implements Curve2 {
    private final List<Point2> points;

    public Polyline2(List<Point2> points) {
        this.points = points == null ? null : java.util.List.copyOf(points);
    }

    public List<Point2> getPoints() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polyline2 that = (Polyline2) o;
        return Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }

    @Override
    public String toString() {
        return "Polyline2{" + "points=" + points + "}";
    }
}
