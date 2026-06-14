package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal single-sheet hyperboloid surface (rotationally symmetric).
 * Parametrized as x^2/a^2 + y^2/a^2 - z^2/b^2 = 1 in local coordinates.
 *
 * @param position placement (axis is symmetry axis)
 * @param radius radius at z=0 (waist)
 * @param semiAxis b parameter controlling the z-spread rate
 */
/**
 * Minimal single-sheet hyperboloid surface (rotationally symmetric).
 * Parametrized as x^2/a^2 + y^2/a^2 - z^2/b^2 = 1 in local coordinates.
 *
 * @param position placement (axis is symmetry axis)
 * @param radius radius at z=0 (waist)
 * @param semiAxis b parameter controlling the z-spread rate
 */
public final class HyperboloidSurface implements SurfaceGeometry {
    private final Axis2Placement3D position;
    private final double radius;
    private final double semiAxis;

    public HyperboloidSurface(Axis2Placement3D position, double radius, double semiAxis) {
        this.position = position;
        this.radius = radius;
        this.semiAxis = semiAxis;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public double getSemiAxis() {
        return semiAxis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HyperboloidSurface that = (HyperboloidSurface) o;
        return Objects.equals(position, that.position) && radius == that.radius && semiAxis == that.semiAxis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, radius, semiAxis);
    }

    @Override
    public String toString() {
        return "HyperboloidSurface{" + "position=" + position + "radius=" + radius + "semiAxis=" + semiAxis + "}";
    }
}
