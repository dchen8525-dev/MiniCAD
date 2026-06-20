package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal surface of linear extrusion representation.
 *
 * @param sweptCurve directrix curve
 * @param extrusionVector extrusion vector
 */
/**
 * Minimal surface of linear extrusion representation.
 *
 * @param sweptCurve directrix curve
 * @param extrusionVector extrusion vector
 */
public final class SurfaceOfLinearExtrusion3 implements SurfaceGeometry {
    private final Curve3 sweptCurve;
    private final Vector3 extrusionVector;

    public SurfaceOfLinearExtrusion3(Curve3 sweptCurve, Vector3 extrusionVector) {
        this.sweptCurve = sweptCurve;
        this.extrusionVector = extrusionVector;
    }

    public Curve3 getSweptCurve() {
        return sweptCurve;
    }

    public Vector3 getExtrusionVector() {
        return extrusionVector;
    }

    // Record-style accessors
    public Curve3 sweptCurve() { return getSweptCurve(); }
    public Vector3 extrusionVector() { return getExtrusionVector(); }

    /**
     * Returns a point on the surface of linear extrusion at the given parametric coordinates.
     *
     * @param u parameter along the swept curve
     * @param v extrusion parameter (0 = base, 1 = full extrusion)
     * @return point on the surface
     */
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        CartesianPoint curvePoint = sweptCurve.pointAt(u);
        return curvePoint.add(extrusionVector.scale(v));
    }

    /**
     * Samples the surface on a grid.
     *
     * @param uSegments number of segments along the swept curve
     * @param vSegments number of segments along extrusion
     * @return list of sampled point rows
     */
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        java.util.List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>();
        for (int i = 0; i <= uSegments; i++) {
            java.util.List<CartesianPoint> row = new java.util.ArrayList<>();
            double u = (double) i / uSegments;
            for (int j = 0; j <= vSegments; j++) {
                double v = (double) j / vSegments;
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
        SurfaceOfLinearExtrusion3 that = (SurfaceOfLinearExtrusion3) o;
        return Objects.equals(sweptCurve, that.sweptCurve) && Objects.equals(extrusionVector, that.extrusionVector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sweptCurve, extrusionVector);
    }

    @Override
    public String toString() {
        return "SurfaceOfLinearExtrusion3{" + "sweptCurve=" + sweptCurve + "extrusionVector=" + extrusionVector + "}";
    }

    @Override
    public Vector3 normalAt(double u, double v) {
        Vector3 tangent = sweptCurve.tangentAt(u);
        return tangent.cross(extrusionVector).normalize();
    }
}
