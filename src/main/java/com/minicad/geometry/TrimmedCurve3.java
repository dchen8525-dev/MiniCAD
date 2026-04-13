package com.minicad.geometry;

import com.minicad.common.Preconditions;

/**
 * Minimal trimmed-curve wrapper over a supported basis curve.
 *
 * @param basisCurve supported basis curve
 * @param trimStart first trim point
 * @param trimEnd second trim point
 * @param senseAgreement trimming orientation agreement
 */
public record TrimmedCurve3(
        Curve3 basisCurve,
        CartesianPoint trimStart,
        CartesianPoint trimEnd,
        boolean senseAgreement
) implements Curve3 {

    /**
     * Creates a trimmed curve.
     */
    public TrimmedCurve3 {
        Preconditions.requireNonNull(basisCurve, "basisCurve");
        Preconditions.requireNonNull(trimStart, "trimStart");
        Preconditions.requireNonNull(trimEnd, "trimEnd");
    }

    @Override
    public boolean contains(CartesianPoint point) {
        return basisCurve.contains(point);
    }
}
