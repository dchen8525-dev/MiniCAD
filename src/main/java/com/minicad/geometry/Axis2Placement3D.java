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

    // Record-style accessors
    public CartesianPoint location() { return getLocation(); }
    public Direction3 axis() { return getAxis(); }
    public Direction3 refDirection() { return getRefDirection(); }

    /**
     * Returns the local X direction (refDirection normalized).
     *
     * @return X direction
     */
    public Direction3 xDirection() {
        return refDirection != null ? refDirection : Direction3.xAxis();
    }

    /**
     * Returns the local Y direction (computed from axis and refDirection).
     *
     * @return Y direction
     */
    public Direction3 yDirection() {
        Direction3 z = axis != null ? axis : Direction3.zAxis();
        Direction3 x = xDirection();
        return Direction3.from(z.asVector().cross(x.asVector()));
    }

    /**
     * Transforms a local direction to world coordinates.
     *
     * @param localDir local direction
     * @return world direction
     */
    public Direction3 transformDirectionToWorld(Direction3 localDir) {
        Vector3 localVec = localDir.asVector();
        Vector3 worldVec = xDirection().asVector().scale(localVec.x())
                .add(yDirection().asVector().scale(localVec.y()))
                .add(axis.asVector().scale(localVec.z()));
        return Direction3.from(worldVec);
    }

    /**
     * Transforms a local point to world coordinates.
     *
     * @param localPoint local point
     * @return world point
     */
    public CartesianPoint transformToWorld(CartesianPoint localPoint) {
        Vector3 offset = xDirection().asVector().scale(localPoint.x())
                .add(yDirection().asVector().scale(localPoint.y()))
                .add(axis.asVector().scale(localPoint.z()));
        return location.add(offset);
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
