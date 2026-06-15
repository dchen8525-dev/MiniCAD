package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal infinite cylindrical surface representation.
 *
 * @param position cylinder placement
 * @param radius positive radius
 */
/**
 * Minimal infinite cylindrical surface representation.
 *
 * @param position cylinder placement
 * @param radius positive radius
 */
public final class CylindricalSurface implements SurfaceGeometry {
    private final Axis2Placement3D position;
    private final double radius;

    public CylindricalSurface(Axis2Placement3D position, double radius) {
        this.position = position;
        this.radius = radius;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return getPosition(); }
    public double radius() { return getRadius(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CylindricalSurface that = (CylindricalSurface) o;
        return Objects.equals(position, that.position) && radius == that.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, radius);
    }

    @Override
    public String toString() {
        return "CylindricalSurface{" + "position=" + position + "radius=" + radius + "}";
    }
}
