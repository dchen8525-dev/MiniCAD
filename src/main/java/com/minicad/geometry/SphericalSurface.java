package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal spherical surface representation.
 *
 * @param position sphere placement
 * @param radius sphere radius
 */
public record SphericalSurface(Axis2Placement3D position, double radius) implements SurfaceGeometry {

    /**
     * Creates a spherical surface and validates its invariants.
     */
    public SphericalSurface {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(radius, "radius");
        if (radius <= Epsilon.EPS) {
            throw new GeometryException("sphere radius must be greater than epsilon");
        }
    }
}
