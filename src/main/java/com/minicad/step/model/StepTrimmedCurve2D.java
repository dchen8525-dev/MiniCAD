package com.minicad.step.model;

/**
 * Resolved TRIMMED_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param basisCurve the underlying 2D curve
 * @param trim1 first trim parameter
 * @param trim2 second trim parameter
 * @param senseAgreement whether the trimmed curve follows the same sense as the basis curve
 */
public record StepTrimmedCurve2D(
        int id,
        String name,
        StepCurve basisCurve,
        double trim1,
        double trim2,
        boolean senseAgreement
) implements StepEntity {
}
