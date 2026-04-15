package com.minicad.geometry;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal 3D degenerate curve representation.
 * A degenerate curve collapses to a single point.
 *
 * @param point the single point where the curve degenerates
 */
public record DegenerateCurve3(CartesianPoint point) implements Curve3 {

    /**
     * Creates a degenerate curve.
     */
    public DegenerateCurve3 {
        Preconditions.requireNonNull(point, "point");
    }

    @Override
    public boolean contains(CartesianPoint other) {
        Preconditions.requireNonNull(other, "other");
        return point.equals(other);
    }

    /**
     * Returns a sample of the degenerate curve (single point repeated).
     *
     * @param segments ignored (always returns two points)
     * @return list containing the point twice
     */
    public java.util.List<CartesianPoint> sample(int segments) {
        return java.util.List.of(point, point);
    }

    /**
     * Returns the point at any parameter (always the same point).
     *
     * @param parameter ignored
     * @return the degenerate point
     */
    public CartesianPoint pointAt(double parameter) {
        return point;
    }

    /**
     * Returns the tangent vector at any parameter.
     * A degenerate curve has no meaningful tangent.
     *
     * @param parameter ignored
     * @return throws GeometryException since tangent is undefined
     * @throws GeometryException always, since degenerate curve has no tangent
     */
    public Vector3 tangentAt(double parameter) {
        throw new GeometryException("degenerate curve has no meaningful tangent");
    }

    /**
     * Returns the bounding box of the degenerate curve (a single point).
     *
     * @return bounding box containing just the degenerate point
     */
    public BoundingBox3 boundingBox() {
        return BoundingBox3.of(point);
    }

    /**
     * Returns the closest point on the degenerate curve to a given point.
     * Since the curve is a single point, this always returns the degenerate point.
     *
     * @param other target point
     * @return the degenerate point
     */
    public CartesianPoint closestPointTo(CartesianPoint other) {
        Preconditions.requireNonNull(other, "other");
        return point;
    }

    /**
     * Returns the distance from a point to the degenerate curve.
     *
     * @param other target point
     * @return distance to the degenerate point
     */
    public double distanceTo(CartesianPoint other) {
        Preconditions.requireNonNull(other, "other");
        return other.distanceTo(point);
    }
}