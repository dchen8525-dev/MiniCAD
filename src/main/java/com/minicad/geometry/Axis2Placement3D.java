package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal 3D placement with explicit origin, axis and reference direction.
 *
 * @param location placement origin
 * @param axis local Z direction
 * @param refDirection local X reference direction
 */
/**
 * Minimal 3D placement with explicit origin, axis and reference direction.
 *
 * @param location placement origin
 * @param axis local Z direction
 * @param refDirection local X reference direction
 */
public final class Axis2Placement3D {
    private final CartesianPoint location;
    private final Direction3 axis;
    private final Direction3 refDirection;

    public Axis2Placement3D(CartesianPoint location, Direction3 axis, Direction3 refDirection) {
        this.location = location;
        this.axis = axis;
        this.refDirection = refDirection;
    }

    public CartesianPoint getLocation() {
        return location;
    }

    public Direction3 getAxis() {
        return axis;
    }

    public Direction3 getRefDirection() {
        return refDirection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Axis2Placement3D that = (Axis2Placement3D) o;
        return Objects.equals(location, that.location) && Objects.equals(axis, that.axis) && Objects.equals(refDirection, that.refDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, axis, refDirection);
    }

    @Override
    public String toString() {
        return "Axis2Placement3D{" + "location=" + location + "axis=" + axis + "refDirection=" + refDirection + "}";
    }
}
