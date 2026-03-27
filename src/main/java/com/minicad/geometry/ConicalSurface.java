package com.minicad.geometry;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal infinite conical surface representation.
 *
 * @param position cone placement
 * @param radius radius at placement origin
 * @param semiAngle cone semi-angle in radians
 */
public record ConicalSurface(Axis2Placement3D position, double radius, double semiAngle) {

    /**
     * Creates a conical surface and validates its invariants.
     */
    public ConicalSurface {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(radius, "radius");
        Preconditions.requireFinite(semiAngle, "semiAngle");
        if (radius <= 0.0) {
            throw new GeometryException("radius must be positive");
        }
        if (Math.abs(semiAngle) >= Math.PI / 2.0) {
            throw new GeometryException("semiAngle magnitude must be less than pi/2");
        }
    }
}
