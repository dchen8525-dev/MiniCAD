package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Infinite 3D plane defined by a point and a unit normal.
 *
 * @param origin point on the plane
 * @param normal unit plane normal
 */
public record Plane(CartesianPoint origin, Direction3 normal) {

    /**
     * Creates a plane and validates its fields.
     *
     */
    public Plane {
        Preconditions.requireNonNull(origin, "origin");
        Preconditions.requireNonNull(normal, "normal");
    }

    /**
     * Returns the signed distance from a point to the plane.
     *
     * @param point queried point
     * @return signed distance
     */
    public double signedDistanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.subtract(origin).dot(normal.asVector());
    }

    /**
     * Returns the absolute distance from a point to the plane.
     *
     * @param point queried point
     * @return absolute distance
     */
    public double distanceTo(CartesianPoint point) {
        return Math.abs(signedDistanceTo(point));
    }

    /**
     * Returns whether a point lies on the plane within epsilon.
     *
     * @param point queried point
     * @return whether the point lies on the plane
     */
    public boolean contains(CartesianPoint point) {
        return Epsilon.isZero(signedDistanceTo(point));
    }

    /**
     * Intersects this plane with a line.
     *
     * @param line line to intersect
     * @return intersection point
     */
    public CartesianPoint intersect(Line3 line) {
        Preconditions.requireNonNull(line, "line");
        double denominator = normal.asVector().dot(line.direction().asVector());
        if (Epsilon.isZero(denominator)) {
            throw new GeometryException("line is parallel to plane");
        }
        double t = -signedDistanceTo(line.origin()) / denominator;
        return line.pointAt(t);
    }
}
