package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal 3D circle representation.
 *
 * @param position circle placement
 * @param radius positive radius
 */
public record Circle(Axis2Placement3D position, double radius) {

    /**
     * Creates a circle and validates its invariants.
     */
    public Circle {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(radius, "radius");
        if (radius <= Epsilon.EPS) {
            throw new GeometryException("radius must be greater than epsilon");
        }
    }
}
