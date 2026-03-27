package com.minicad.geometry;

import com.minicad.common.Preconditions;

/**
 * Minimal trimmed-curve wrapper over a supported basis curve.
 *
 * @param basisCurve supported basis curve
 */
public record TrimmedCurve3(Curve3 basisCurve) implements Curve3 {

    /**
     * Creates a trimmed curve.
     */
    public TrimmedCurve3 {
        Preconditions.requireNonNull(basisCurve, "basisCurve");
    }

    @Override
    public boolean contains(CartesianPoint point) {
        return basisCurve.contains(point);
    }
}
