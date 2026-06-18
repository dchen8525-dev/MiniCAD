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

    /**
     * Returns the bounding box for a cylindrical segment with limited height range.
     *
     * @param vMin minimum height along axis
     * @param vMax maximum height along axis
     * @return bounding box
     */
    public BoundingBox3 boundingBox(double vMin, double vMax) {
        Preconditions.requireFinite(vMin, "vMin");
        Preconditions.requireFinite(vMax, "vMax");
        CartesianPoint origin = position.location();
        Vector3 axis = position.axis().asVector();
        Vector3 xDir = position.xDirection().asVector();
        Vector3 yDir = position.yDirection().asVector();

        // Sample points at the extremes (0, PI/2, PI, 3PI/2)
        BoundingBox3 box = BoundingBox3.empty();
        double[] angles = {0, Math.PI / 2, Math.PI, 3 * Math.PI / 2};
        for (double u : angles) {
            CartesianPoint pMin = pointAt(u, vMin);
            CartesianPoint pMax = pointAt(u, vMax);
            box = box.expand(pMin).expand(pMax);
        }
        return box;
    }

    @Override
    public BoundingBox3 boundingBox() {
        // For infinite cylinder, return a reasonable approximation
        // Sample points at multiple heights
        return boundingBox(-10, 10);
    }

    @Override
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>();
        for (int i = 0; i <= uSegments; i++) {
            java.util.List<CartesianPoint> row = new java.util.ArrayList<>();
            double u = 2 * Math.PI * i / uSegments;
            for (int j = 0; j <= vSegments; j++) {
                double v = j / (double) vSegments * 20 - 10; // Sample from -10 to 10
                row.add(pointAt(u, v));
            }
            grid.add(java.util.List.copyOf(row));
        }
        return java.util.List.copyOf(grid);
    }

    /**
     * Returns the closest point on the cylinder surface to a given point.
     *
     * @param point target point
     * @return closest point on cylinder
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint origin = position.location();
        Vector3 axis = position.axis().asVector();

        // Project point onto axis
        Vector3 toPoint = point.subtract(origin);
        double v = toPoint.dot(axis);

        // Find radial direction
        Vector3 radial = toPoint.subtract(axis.scale(v));
        double radialDist = radial.norm();
        if (radialDist < Epsilon.get()) {
            // Point is on axis - any point on cylinder at that height is valid
            return origin.add(axis.scale(v)).add(position.xDirection().asVector().scale(radius));
        }

        // Normalize radial direction and scale to radius
        Direction3 radialDir = new Direction3(radial.x(), radial.y(), radial.z()).normalize();
        return origin.add(axis.scale(v)).add(radialDir.asVector().scale(radius));
    }

    /**
     * Returns the distance from a point to the cylinder surface.
     *
     * @param point target point
     * @return distance to surface
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = closestPointTo(point);
        return point.distanceTo(closest);
    }

    @Override
    public Vector3 normalAt(double u, double v) {
        return normalAt(u);
    }
}
