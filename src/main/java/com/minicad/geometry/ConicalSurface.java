package com.minicad.geometry;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal infinite conical surface representation.
 *
 * @param position cone placement
 * @param radius radius at placement origin
 * @param semiAngle cone semi-angle in radians
 */
/**
 * Minimal infinite conical surface representation.
 *
 * @param position cone placement
 * @param radius radius at placement origin
 * @param semiAngle cone semi-angle in radians
 */
public final class ConicalSurface implements SurfaceGeometry {
    private final Axis2Placement3D position;
    private final double radius;
    private final double semiAngle;

    public ConicalSurface(Axis2Placement3D position, double radius, double semiAngle) {
        this.position = position;
        this.radius = radius;
        this.semiAngle = semiAngle;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public double getSemiAngle() {
        return semiAngle;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return position; }
    public double radius() { return radius; }
    public double semiAngle() { return semiAngle; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConicalSurface that = (ConicalSurface) o;
        return Objects.equals(position, that.position) && radius == that.radius && semiAngle == that.semiAngle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, radius, semiAngle);
    }

    @Override
    public String toString() {
        return "ConicalSurface{" + "position=" + position + "radius=" + radius + "semiAngle=" + semiAngle + "}";
    }
}
