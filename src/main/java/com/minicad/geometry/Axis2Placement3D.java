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
}
