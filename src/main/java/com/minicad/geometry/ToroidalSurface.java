package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal toroidal surface representation.
 *
 * @param position torus placement
 * @param majorRadius distance from axis to tube center
 * @param minorRadius tube radius
 */
/**
 * Minimal toroidal surface representation.
 *
 * @param position torus placement
 * @param majorRadius distance from axis to tube center
 * @param minorRadius tube radius
 */
public final class ToroidalSurface implements SurfaceGeometry {
    private final Axis2Placement3D position;
    private final double majorRadius;
    private final double minorRadius;

    public ToroidalSurface(Axis2Placement3D position, double majorRadius, double minorRadius) {
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getMajorRadius() {
        return majorRadius;
    }

    public double getMinorRadius() {
        return minorRadius;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return getPosition(); }
    public double majorRadius() { return getMajorRadius(); }
    public double minorRadius() { return getMinorRadius(); }

    /**
     * Returns a point on the toroidal surface at the given parametric coordinates.
     *
     * @param u revolution angle around the torus axis (radians)
     * @param v angle around the tube cross-section (radians)
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
        double tubeCenterR = majorRadius + minorRadius * cosV;
        Vector3 radial = xDir.scale(tubeCenterR * cosU).add(yDir.scale(tubeCenterR * sinU));
        Vector3 tubeOffset = axis.scale(minorRadius * sinV);
        return origin.add(radial).add(tubeOffset);
    }

    /**
     * Samples the toroidal surface on a grid.
     *
     * @param uSegments number of segments around the torus axis
     * @param vSegments number of segments around the tube cross-section
     * @return list of sampled point rows
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>();
        for (int i = 0; i <= uSegments; i++) {
            java.util.List<CartesianPoint> row = new java.util.ArrayList<>();
            double u = 2 * Math.PI * i / uSegments;
            for (int j = 0; j <= vSegments; j++) {
                double v = 2 * Math.PI * j / vSegments;
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
        ToroidalSurface that = (ToroidalSurface) o;
        return Objects.equals(position, that.position) && majorRadius == that.majorRadius && minorRadius == that.minorRadius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, majorRadius, minorRadius);
    }

    @Override
    public String toString() {
        return "ToroidalSurface{" + "position=" + position + "majorRadius=" + majorRadius + "minorRadius=" + minorRadius + "}";
    }

    @Override
    public Vector3 normalAt(double u, double v) {
        Vector3 x = position.xDirection().asVector();
        Vector3 y = position.yDirection().asVector();
        Vector3 z = position.getAxis().asVector();
        return x.scale(Math.cos(u) * Math.cos(v))
            .add(y.scale(Math.sin(u) * Math.cos(v)))
            .add(z.scale(Math.sin(v))).normalize();
    }
}
