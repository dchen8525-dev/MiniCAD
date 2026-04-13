package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal surface of linear extrusion representation.
 *
 * @param sweptCurve directrix curve
 * @param extrusionVector extrusion vector
 */
public record SurfaceOfLinearExtrusion3(Curve3 sweptCurve, Vector3 extrusionVector) implements SurfaceGeometry {

    public SurfaceOfLinearExtrusion3 {
        Preconditions.requireNonNull(sweptCurve, "sweptCurve");
        Preconditions.requireNonNull(extrusionVector, "extrusionVector");
        if (extrusionVector.norm() <= Epsilon.EPS) {
            throw new GeometryException("extrusionVector must be greater than epsilon");
        }
    }
}
