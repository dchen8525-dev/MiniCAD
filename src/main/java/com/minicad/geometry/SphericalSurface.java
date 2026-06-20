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
     * Samples the spherical surface on a grid.
     *
     * @param uSegments number of segments around the axis
     * @param vSegments number of segments from pole to pole
     * @return list of sampled point rows
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>();
        for (int i = 0; i <= uSegments; i++) {
            java.util.List<CartesianPoint> row = new java.util.ArrayList<>();
            double u = 2 * Math.PI * i / uSegments;
            for (int j = 0; j <= vSegments; j++) {
                double v = Math.PI * j / vSegments - Math.PI / 2;
                row.add(pointAt(u, v));
            }
            grid.add(java.util.List.copyOf(row));
        }
        return java.util.List.copyOf(grid);
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

    @Override
    public Vector3 normalAt(double u, double v) {
        Vector3 x = position.xDirection().asVector();
        Vector3 y = position.yDirection().asVector();
        Vector3 z = position.getAxis().asVector();
        return x.scale(Math.sin(v) * Math.cos(u))
            .add(y.scale(Math.sin(v) * Math.sin(u)))
            .add(z.scale(Math.cos(v))).normalize();
    }
}
