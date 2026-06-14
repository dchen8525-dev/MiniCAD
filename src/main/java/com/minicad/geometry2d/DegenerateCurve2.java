package com.minicad.geometry2d;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal 2D degenerate curve representation.
 * A degenerate curve collapses to a single point.
 *
 * @param point the single point where the curve degenerates
 */
/**
 * Minimal 2D degenerate curve representation.
 * A degenerate curve collapses to a single point.
 *
 * @param point the single point where the curve degenerates
 */
public final class DegenerateCurve2 implements Curve2 {
    private final Point2 point;

    public DegenerateCurve2(Point2 point) {
        this.point = point;
    }

    public Point2 getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DegenerateCurve2 that = (DegenerateCurve2) o;
        return Objects.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }

    @Override
    public String toString() {
        return "DegenerateCurve2{" + "point=" + point + "}";
    }
}
