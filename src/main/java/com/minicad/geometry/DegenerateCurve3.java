package com.minicad.geometry;

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
}