package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal infinite cylindrical surface representation.
 *
 * @param position cylinder placement
 * @param radius positive radius
 */
public record CylindricalSurface(Axis2Placement3D position, double radius) {

    /**
     * Creates a cylindrical surface and validates its invariants.
     */
    public CylindricalSurface {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(radius, "radius");
        if (radius <= Epsilon.EPS) {
            throw new GeometryException("radius must be greater than epsilon");
        }
    }
}
