package com.minicad.geometry2d;

import com.minicad.common.Preconditions;

/**
 * Minimal 2D trimmed curve wrapper for parameter-space curves.
 *
 * @param basisCurve underlying supported 2D curve
 * @param trimStart first trim point
 * @param trimEnd second trim point
 * @param senseAgreement trimming orientation agreement
 */
public record TrimmedCurve2(
        Curve2 basisCurve,
        Point2 trimStart,
        Point2 trimEnd,
        boolean senseAgreement
) implements Curve2 {

    public TrimmedCurve2 {
        Preconditions.requireNonNull(basisCurve, "basisCurve");
        Preconditions.requireNonNull(trimStart, "trimStart");
        Preconditions.requireNonNull(trimEnd, "trimEnd");
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return basisCurve.contains(point);
    }
}
