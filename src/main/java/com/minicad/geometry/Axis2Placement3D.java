package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal 3D placement with explicit origin, axis and reference direction.
 *
 * @param location placement origin
 * @param axis local Z direction
 * @param refDirection local X reference direction
 */
public record Axis2Placement3D(CartesianPoint location, Direction3 axis, Direction3 refDirection) {

    /**
     * Creates a placement and validates that axis and reference direction are not parallel.
     */
    public Axis2Placement3D {
        Preconditions.requireNonNull(location, "location");
        Preconditions.requireNonNull(axis, "axis");
        Preconditions.requireNonNull(refDirection, "refDirection");
        if (axis.asVector().cross(refDirection.asVector()).isZero()) {
            throw new GeometryException("axis and refDirection must not be parallel");
        }
    }

    /**
     * Returns an orthonormal local X direction derived from the reference direction.
     *
     * @return orthonormal local X direction
     */
    public Direction3 xDirection() {
        Vector3 z = axis.asVector();
        Vector3 ref = refDirection.asVector();
        Vector3 y = z.cross(ref).normalize().asVector();
        Vector3 x = y.cross(z);
        return x.normalize();
    }

    /**
     * Returns an orthonormal local Y direction.
     *
     * @return orthonormal local Y direction
     */
    public Direction3 yDirection() {
        return axis.asVector().cross(xDirection().asVector()).normalize();
    }

    /**
     * Transforms local coordinates to world coordinates.
     *
     * @param localX local X coordinate
     * @param localY local Y coordinate
     * @param localZ local Z coordinate
     * @return point in world coordinates
     */
    public CartesianPoint transformToWorld(double localX, double localY, double localZ) {
        Direction3 xDir = xDirection();
        Direction3 yDir = yDirection();
        double worldX = location.x() + localX * xDir.x() + localY * yDir.x() + localZ * axis.x();
        double worldY = location.y() + localX * xDir.y() + localY * yDir.y() + localZ * axis.y();
        double worldZ = location.z() + localX * xDir.z() + localY * yDir.z() + localZ * axis.z();
        return new CartesianPoint(worldX, worldY, worldZ);
    }

    /**
     * Transforms a local point to world coordinates.
     *
     * @param localPoint point in local coordinate system
     * @return point in world coordinates
     */
    public CartesianPoint transformToWorld(CartesianPoint localPoint) {
        return transformToWorld(localPoint.x(), localPoint.y(), localPoint.z());
    }

    /**
     * Transforms a world point to local coordinates.
     *
     * @param worldPoint point in world coordinate system
     * @return point in local coordinates
     */
    public CartesianPoint transformToLocal(CartesianPoint worldPoint) {
        Direction3 xDir = xDirection();
        Direction3 yDir = yDirection();
        Vector3 diff = worldPoint.subtract(location);
        double localX = diff.dot(xDir.asVector());
        double localY = diff.dot(yDir.asVector());
        double localZ = diff.dot(axis.asVector());
        return new CartesianPoint(localX, localY, localZ);
    }

    /**
     * Transforms a direction from local to world.
     *
     * @param localDirection direction in local coordinate system
     * @return direction in world coordinate system
     */
    public Direction3 transformDirectionToWorld(Direction3 localDirection) {
        Direction3 xDir = xDirection();
        Direction3 yDir = yDirection();
        double worldX = localDirection.x() * xDir.x() + localDirection.y() * yDir.x() + localDirection.z() * axis.x();
        double worldY = localDirection.x() * xDir.y() + localDirection.y() * yDir.y() + localDirection.z() * axis.y();
        double worldZ = localDirection.x() * xDir.z() + localDirection.y() * yDir.z() + localDirection.z() * axis.z();
        return new Direction3(worldX, worldY, worldZ);
    }

    /**
     * Transforms a direction from world to local.
     *
     * @param worldDirection direction in world coordinate system
     * @return direction in local coordinate system
     */
    public Direction3 transformDirectionToLocal(Direction3 worldDirection) {
        Direction3 xDir = xDirection();
        Direction3 yDir = yDirection();
        double localX = worldDirection.x() * xDir.x() + worldDirection.y() * xDir.y() + worldDirection.z() * xDir.z();
        double localY = worldDirection.x() * yDir.x() + worldDirection.y() * yDir.y() + worldDirection.z() * yDir.z();
        double localZ = worldDirection.x() * axis.x() + worldDirection.y() * axis.y() + worldDirection.z() * axis.z();
        return new Direction3(localX, localY, localZ);
    }

    /**
     * Creates a placement at the world origin with standard axes.
     *
     * @return origin placement
     */
    public static Axis2Placement3D origin() {
        return new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
    }

    /**
     * Creates a placement at a given origin with Z axis pointing up.
     *
     * @param origin placement origin
     * @return placement at origin with standard orientation
     */
    public static Axis2Placement3D at(CartesianPoint origin) {
        return new Axis2Placement3D(origin, new Direction3(0, 0, 1), new Direction3(1, 0, 0));
    }
}
