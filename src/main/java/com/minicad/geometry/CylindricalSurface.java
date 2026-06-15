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

    /**
     * Returns a point on the cylindrical surface at the given parametric coordinates.
     *
     * @param u angle around the cylinder axis (radians)
     * @param v height along the cylinder axis
     * @return point on the surface
     */
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        CartesianPoint origin = position.location();
        Vector3 axis = position.axis().asVector();
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();
        double cosU = Math.cos(u);
        double sinU = Math.sin(u);
        Vector3 radial = xDir.scale(radius * cosU).add(yDir.scale(radius * sinU));
        return origin.add(radial).add(axis.scale(v));
    }

    /**
     * Returns the surface normal at the given angle.
     *
     * @param u angle around the cylinder axis (radians)
     * @return unit normal vector pointing outward
     */
    public Vector3 normalAt(double u) {
        Preconditions.requireFinite(u, "u");
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();
        double cosU = Math.cos(u);
        double sinU = Math.sin(u);
        return xDir.scale(cosU).add(yDir.scale(sinU)).normalize().asVector();
    }

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
