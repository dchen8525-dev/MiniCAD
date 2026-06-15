package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;

import java.util.List;
import java.util.Objects;

/**
 * Loop represented directly by polygon vertices.
 *
 * @param points polygon vertices in order
 */
/**
 * Loop represented directly by polygon vertices.
 *
 * @param points polygon vertices in order
 */
public final class PolyLoop implements Loop {
    private final List<CartesianPoint> points;

    public PolyLoop(List<CartesianPoint> points) {
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
        PolyLoop that = (PolyLoop) o;
        return Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }

    @Override
    public String toString() {
        return "PolyLoop{" + "points=" + points + "}";
    }

    @Override
    public BoundingBox3 boundingBox() {
        if (points == null || points.isEmpty()) {
            return BoundingBox3.empty();
        }
        BoundingBox3 box = BoundingBox3.empty();
        for (CartesianPoint p : points) {
            box = box.union(p);
        }
        return box;
    }
}
