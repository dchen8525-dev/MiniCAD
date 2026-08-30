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

    /**
     * Returns a point on the conical surface at the given parametric coordinates.
     *
     * @param u angle around the cone axis (radians)
     * @param v height along the cone axis
     * @return point on the surface
     */
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        CartesianPoint origin = position.getLocation();
        Vector3 axis = position.getAxis().asVector();
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();
        double r = radius + v * Math.tan(semiAngle);
        double cosU = Math.cos(u);
        double sinU = Math.sin(u);
        Vector3 radial = xDir.scale(r * cosU).add(yDir.scale(r * sinU));
        return origin.add(radial).add(axis.scale(v));
    }

    /**
     * {@inheritDoc}
     *
     * <p>A cone is developable, so its normal is constant along a generator and
     * depends only on the azimuth — hence the delegation to
     * {@link #normalAt(double)}. Without this override the interface's
     * finite-difference default would win and the closed form below would never
     * run.</p>
     */
    @Override
    public Vector3 normalAt(double u, double v) {
        return normalAt(u);
    }

    /**
     * Returns the surface normal at the given parametric coordinates.
     *
     * @param u angle around the cone axis (radians)
     * @return unit normal vector
     */
    public Vector3 normalAt(double u) {
        Preconditions.requireFinite(u, "u");
        Vector3 axis = position.getAxis().asVector();
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();
        double cosU = Math.cos(u);
        double sinU = Math.sin(u);
        Vector3 radial = xDir.scale(cosU).add(yDir.scale(sinU));
        Vector3 axial = axis.scale(-Math.tan(semiAngle));
        return radial.add(axial).normalize().asVector();
    }

    /**
     * Samples the conical surface on a grid.
     *
     * @param uSegments number of segments around the axis
     * @param vSegments number of segments along the axis
     * @return list of sampled point rows
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>();
        for (int i = 0; i <= uSegments; i++) {
            java.util.List<CartesianPoint> row = new java.util.ArrayList<>();
            double u = 2 * Math.PI * i / uSegments;
            for (int j = 0; j <= vSegments; j++) {
                double v = -10.0 + 20.0 * j / vSegments; // Sample a range
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
        // Sample grid and find closest point
        java.util.List<java.util.List<CartesianPoint>> grid = sampleGrid(32, 32);
        CartesianPoint closest = grid.get(0).get(0);
        double minDist = point.distanceTo(closest);
        for (java.util.List<CartesianPoint> row : grid) {
            for (CartesianPoint p : row) {
                double dist = point.distanceTo(p);
                if (dist < minDist) {
                    minDist = dist;
                    closest = p;
                }
            }
        }
        return closest;
    }

    /**
     * Computes the distance from a point to this surface.
     *
     * @param point the point to measure distance from
     * @return distance to the surface
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = closestPointTo(point);
        return point.distanceTo(closest);
    }

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
