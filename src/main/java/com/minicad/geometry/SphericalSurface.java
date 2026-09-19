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
        Preconditions.requireNonNull(position, "position");
        if (radius <= Epsilon.get()) {
            throw new GeometryException("sphere radius must be greater than epsilon");
        }
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
        CartesianPoint origin = position.getLocation();
        Vector3 axis = position.getAxis().asVector();
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

    /**
     * {@inheritDoc}
     *
     * <p>U is the azimuth over one full turn {@code [0, 2PI]}; V is the latitude over
     * {@code [-PI/2, PI/2]}, which puts {@code v = 0} on the equator, exactly as
     * {@link #pointAt} reads it.</p>
     */
    @Override
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        return SurfaceGridSampling.sampleGrid(
                uSegments, vSegments, 0.0, 2.0 * Math.PI, -Math.PI / 2, Math.PI / 2, this::pointAt);
    }

    /**
     * Finds the closest point on this surface to a given point.
     *
     * @param point the point to find closest point to
     * @return closest point on the surface
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint center = position.getLocation();
        Vector3 toPoint = point.subtract(center);
        double dist = toPoint.norm();
        if (dist < Epsilon.get()) {
            return pointAt(0, 0); // Point at center, return any point on sphere
        }
        Direction3 radial = Direction3.from(toPoint);
        return center.add(radial.asVector().scale(radius));
    }

    /**
     * Computes the distance from a point to this surface.
     *
     * @param point the point to measure distance from
     * @return distance to the surface
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint center = position.getLocation();
        double dist = point.distanceTo(center);
        return Math.abs(dist - radius);
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

    /**
     * {@inheritDoc}
     *
     * <p>{@code v} is a <em>latitude</em> here, matching {@code pointAt} and
     * {@code sampleGrid}: {@code v = 0} is the equator and {@code v = ±PI/2}
     * the poles. (This method previously read {@code v} as a polar angle, which
     * put the normals a quarter turn away from the points they described.)</p>
     */
    @Override
    public Vector3 normalAt(double u, double v) {
        // The outward normal of a sphere is the radial direction, which is
        // exactly ∂P/∂u × ∂P/∂v for the (azimuth, latitude) parameterization.
        return pointAt(u, v).subtract(position.getLocation()).normalize();
    }
}
