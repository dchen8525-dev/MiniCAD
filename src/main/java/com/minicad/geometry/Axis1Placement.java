package com.minicad.geometry;

import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal 1D axis placement with origin point and axis direction.
 * Used for representing rotation axes and similar constructs.
 *
 * @param location origin point
 * @param axis axis direction
 */
public final class Axis1Placement {
    private final CartesianPoint location;
    private final Direction3 axis;

    public Axis1Placement(CartesianPoint location, Direction3 axis) {
        Preconditions.requireNonNull(location, "location");
        Preconditions.requireNonNull(axis, "axis");
        this.location = location;
        this.axis = axis;
    }

    public CartesianPoint getLocation() {
        return location;
    }

    public Direction3 getAxis() {
        return axis;
    }

    // Record-style accessors
    public CartesianPoint location() { return getLocation(); }
    public Direction3 axis() { return getAxis(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Axis1Placement that = (Axis1Placement) o;
        return Objects.equals(location, that.location) && Objects.equals(axis, that.axis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, axis);
    }

    @Override
    public String toString() {
        return "Axis1Placement{" + "location=" + location + ", axis=" + axis + '}';
    }
}