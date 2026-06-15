package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal spherical surface representation.
 *
 * @param position sphere placement
 * @param radius sphere radius
 */
/**
 * Minimal spherical surface representation.
 *
 * @param position sphere placement
 * @param radius sphere radius
 */
public final class SphericalSurface implements SurfaceGeometry {
    private final Axis2Placement3D position;
    private final double radius;

    public SphericalSurface(Axis2Placement3D position, double radius) {
        this.position = position;
        this.radius = radius;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    // Record-style accessor
    public Axis2Placement3D position() { return getPosition(); }
    public double radius() { return getRadius(); }

    /**
     * Returns a point on the spherical surface at the given parametric coordinates.
     *
     * @param u longitude angle around the sphere axis (radians)
     * @param v latitude angle from equator (radians)
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
        double cosV = Math.cos(v);
        double sinV = Math.sin(v);
        Vector3 radial = xDir.scale(radius * cosV * cosU).add(yDir.scale(radius * cosV * sinU));
        Vector3 axial = axis.scale(radius * sinV);
        return origin.add(radial).add(axial);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SphericalSurface that = (SphericalSurface) o;
        return Objects.equals(position, that.position) && radius == that.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, radius);
    }

    @Override
    public String toString() {
        return "SphericalSurface{" + "position=" + position + "radius=" + radius + "}";
    }
}
