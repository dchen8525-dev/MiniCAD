package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal surface of revolution representation.
 *
 * @param sweptCurve generatrix curve
 * @param axisOrigin point on revolution axis
 * @param axisDirection revolution axis direction
 */
public record SurfaceOfRevolution3(
        Curve3 sweptCurve,
        CartesianPoint axisOrigin,
        Direction3 axisDirection
) implements SurfaceGeometry {

    public SurfaceOfRevolution3 {
        Preconditions.requireNonNull(sweptCurve, "sweptCurve");
        Preconditions.requireNonNull(axisOrigin, "axisOrigin");
        Preconditions.requireNonNull(axisDirection, "axisDirection");
        if (axisDirection.asVector().norm() <= Epsilon.EPS) {
            throw new GeometryException("axisDirection must be greater than epsilon");
        }
    }
}
