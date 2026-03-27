package com.minicad.geometry;

import com.minicad.common.Preconditions;

/**
 * Minimal surface-curve wrapper over a supported 3D curve.
 *
 * @param curve3d supported 3D curve
 */
public record SurfaceCurve3(Curve3 curve3d) implements Curve3 {

    /**
     * Creates a surface curve.
     */
    public SurfaceCurve3 {
        Preconditions.requireNonNull(curve3d, "curve3d");
    }

    @Override
    public boolean contains(CartesianPoint point) {
        return curve3d.contains(point);
    }
}
