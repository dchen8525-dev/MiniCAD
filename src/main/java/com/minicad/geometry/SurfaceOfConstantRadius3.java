package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal surface of constant radius representation.
 * A surface swept by maintaining a constant radius along a path.
 * This can represent pipe-like surfaces with constant cross-section radius.
 *
 * @param sweptSurface the base surface being swept
 * @param radius the constant radius
 */
/**
 * Minimal surface of constant radius representation.
 * A surface swept by maintaining a constant radius along a path.
 * This can represent pipe-like surfaces with constant cross-section radius.
 *
 * @param sweptSurface the base surface being swept
 * @param radius the constant radius
 */
public final class SurfaceOfConstantRadius3 implements SurfaceGeometry {
    private final SurfaceGeometry sweptSurface;
    private final double radius;

    public SurfaceOfConstantRadius3(SurfaceGeometry sweptSurface, double radius) {
        Preconditions.requireNonNull(sweptSurface, "sweptSurface");
        Preconditions.requireFinite(radius, "radius");
        if (radius <= Epsilon.get()) {
            throw new com.minicad.common.GeometryException("radius must be greater than epsilon");
        }
        this.sweptSurface = sweptSurface;
        this.radius = radius;
    }

    public SurfaceGeometry getSweptSurface() {
        return sweptSurface;
    }

    public double getRadius() {
        return radius;
    }

    // Record-style accessors
    public SurfaceGeometry sweptSurface() { return getSweptSurface(); }
    public double radius() { return getRadius(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurfaceOfConstantRadius3 that = (SurfaceOfConstantRadius3) o;
        return Objects.equals(sweptSurface, that.sweptSurface) && radius == that.radius;
    }

    @Override
    public BoundingBox3 boundingBox() {
        // Expand the swept surface's bounding box by the radius
        BoundingBox3 sweptBox = sweptSurface.boundingBox();
        if (sweptBox.isEmpty()) {
            return BoundingBox3.empty();
        }
        return sweptBox.expand(radius);
    }

    @Override
    public java.util.List<java.util.List<CartesianPoint>> sampleGrid(int uSegments, int vSegments) {
        java.util.List<java.util.List<CartesianPoint>> result = new java.util.ArrayList<>();
        java.util.List<java.util.List<CartesianPoint>> base = sweptSurface.sampleGrid(uSegments, vSegments);
        for (int i = 0; i < base.size(); i++) {
            java.util.List<CartesianPoint> row = new java.util.ArrayList<>();
            for (int j = 0; j < base.get(i).size(); j++) {
                double u = 2.0 * Math.PI * i / Math.max(1, uSegments);
                double v = (double) j / Math.max(1, vSegments);
                row.add(base.get(i).get(j).add(sweptSurface.normalAt(u, v).scale(radius)));
            }
            result.add(java.util.List.copyOf(row));
        }
        return java.util.List.copyOf(result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sweptSurface, radius);
    }

    @Override
    public String toString() {
        return "SurfaceOfConstantRadius3{" + "sweptSurface=" + sweptSurface + "radius=" + radius + "}";
    }

    /**
     * Returns a point on the surface at parametric coordinates.
     * Delegates to the swept surface if it has pointAt method.
     *
     * @param u first parametric coordinate
     * @param v second parametric coordinate
     * @return point on the surface
     */
    public CartesianPoint pointAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        CartesianPoint base;
        if (sweptSurface instanceof CylindricalSurface) base = ((CylindricalSurface) sweptSurface).pointAt(u, v);
        else if (sweptSurface instanceof SphericalSurface) base = ((SphericalSurface) sweptSurface).pointAt(u, v);
        else if (sweptSurface instanceof Plane) base = ((Plane) sweptSurface).pointAt(u, v);
        else throw new com.minicad.common.GeometryException("unsupported constant-radius base surface");
        return base.add(sweptSurface.normalAt(u, v).scale(radius));
    }

    /**
     * Returns the closest point on the surface to a given point.
     * Approximate implementation using bounding box.
     *
     * @param point target point
     * @return closest point on surface (approximate)
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint base;
        if (sweptSurface instanceof CylindricalSurface) base = ((CylindricalSurface) sweptSurface).closestPointTo(point);
        else if (sweptSurface instanceof SphericalSurface) base = ((SphericalSurface) sweptSurface).closestPointTo(point);
        else if (sweptSurface instanceof Plane) base = ((Plane) sweptSurface).closestPointTo(point);
        else base = sweptSurface.boundingBox().closestPointTo(point);
        Vector3 direction = point.subtract(base);
        if (direction.norm() <= Epsilon.get()) return base;
        return base.add(direction.normalize().scale(radius));
    }

    /**
     * Returns the distance from a point to the surface.
     * Approximate implementation.
     *
     * @param point target point
     * @return distance to surface (approximate)
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = closestPointTo(point);
        return point.distanceTo(closest);
    }

    @Override
    public Vector3 normalAt(double u, double v) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        return sweptSurface.normalAt(u, v);
    }
}
