package com.minicad.geometry;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Infinite 3D line defined by an origin and a unit direction.
 *
 * @param origin point on the line
 * @param direction unit direction of the line
 */
public record Line3(CartesianPoint origin, Direction3 direction) implements Curve3 {

    /**
     * Creates a line and validates its fields.
     *
     */
    public Line3 {
        Preconditions.requireNonNull(origin, "origin");
        Preconditions.requireNonNull(direction, "direction");
    }

    /**
     * Evaluates a point on the line.
     *
     * @param parameter signed distance along the unit direction
     * @return point at the given parameter
     */
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        return origin.add(direction.asVector().scale(parameter));
    }

    /**
     * Returns the shortest distance from a point to this line.
     *
     * @param point queried point
     * @return shortest distance
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 offset = point.subtract(origin);
        Vector3 projection = direction.asVector().scale(offset.dot(direction.asVector()));
        return offset.subtract(projection).norm();
    }

    /**
     * Returns whether a point lies on the line within epsilon.
     *
     * @param point queried point
     * @return whether the point lies on the line
     */
    public boolean contains(CartesianPoint point) {
        return distanceTo(point) <= com.minicad.common.Epsilon.EPS;
    }
}
