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
        Preconditions.requireNonNull(location, "location");
        Preconditions.requireNonNull(axis, "axis");
        Preconditions.requireNonNull(refDirection, "refDirection");
        if (axis.asVector().cross(refDirection.asVector()).norm() <= Epsilon.get()) {
            throw new GeometryException("axis and refDirection must not be parallel");
        }
        this.location = location;
        this.axis = axis;
        this.refDirection = refDirection;
    }

    /**
     * Creates a placement at the given point with default axes.
     *
     * @param point origin point
     * @return placement at the point with default axis (Z) and refDirection (X)
     */
    public static Axis2Placement3D at(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return new Axis2Placement3D(point, Direction3.zAxis(), Direction3.xAxis());
    }

    /**
     * Creates a default placement at the origin.
     *
     * @return placement at origin with default axis (Z) and refDirection (X)
     */
    public static Axis2Placement3D origin() {
        return at(CartesianPoint.origin());
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
     * Returns a new placement with the given point as the location.
     *
     * @param point the new origin point
     * @return new placement at the given point
     */
    public Axis2Placement3D withOrigin(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return new Axis2Placement3D(point, axis, refDirection);
    }

    /**
     * Returns the local X direction, computed by projecting the reference
     * direction onto the plane normal to the axis (Gram-Schmidt).
     * This ensures an orthonormal coordinate system per the STEP standard.
     *
     * @return X direction
     */
    public Direction3 xDirection() {
        Direction3 z = axis != null ? axis : Direction3.zAxis();
        Direction3 ref = refDirection != null ? refDirection : Direction3.xAxis();
        double dot = ref.dot(z);
        Vector3 projected = ref.asVector().subtract(z.asVector().scale(dot));
        if (projected.isZero()) {
            return ref;
        }
        return Direction3.from(projected);
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
        Vector3 worldVec = xDirection().asVector().scale(localVec.getX())
                .add(yDirection().asVector().scale(localVec.getY()))
                .add(axis.asVector().scale(localVec.getZ()));
        return Direction3.from(worldVec);
    }

    /**
     * Transforms a local point to world coordinates.
     *
     * @param localPoint local point
     * @return world point
     */
    public CartesianPoint transformToWorld(CartesianPoint localPoint) {
        Vector3 offset = xDirection().asVector().scale(localPoint.getX())
                .add(yDirection().asVector().scale(localPoint.getY()))
                .add(axis.asVector().scale(localPoint.getZ()));
        return location.add(offset);
    }

    /**
     * Transforms a world point to local coordinates.
     *
     * @param worldPoint world point
     * @return local point
     */
    public CartesianPoint transformToLocal(CartesianPoint worldPoint) {
        Preconditions.requireNonNull(worldPoint, "worldPoint");
        Vector3 offset = worldPoint.subtract(location);
        double xLocal = offset.dot(xDirection().asVector());
        double yLocal = offset.dot(yDirection().asVector());
        double zLocal = offset.dot(axis.asVector());
        return new CartesianPoint(xLocal, yLocal, zLocal);
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
