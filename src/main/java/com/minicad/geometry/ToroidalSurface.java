package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal toroidal surface representation.
 *
 * @param position torus placement
 * @param majorRadius distance from axis to tube center
 * @param minorRadius tube radius
 */
public record ToroidalSurface(Axis2Placement3D position, double majorRadius, double minorRadius) implements SurfaceGeometry {

    /**
     * Creates a toroidal surface and validates its invariants.
     */
    public ToroidalSurface {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(majorRadius, "majorRadius");
        Preconditions.requireFinite(minorRadius, "minorRadius");
        if (majorRadius <= Epsilon.EPS || minorRadius <= Epsilon.EPS) {
            throw new GeometryException("toroidal radii must be greater than epsilon");
        }
    }
}
