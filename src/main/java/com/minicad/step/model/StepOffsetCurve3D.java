package com.minicad.step.model;

/**
 * Resolved OFFSET_CURVE_3D.
 *
 * @param id step id
 * @param name step label
 * @param basisCurve basis curve
 * @param distance offset distance
 * @param selfIntersect self-intersection flag
 * @param refDirection reference direction
 */
public record StepOffsetCurve3D(
        int id,
        String name,
        StepEntity basisCurve,
        double distance,
        boolean selfIntersect,
        StepDirection refDirection
) implements StepEntity {
}
