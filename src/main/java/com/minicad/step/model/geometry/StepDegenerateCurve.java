package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved DEGENERATE_CURVE.
 * A curve that has degenerated to a point or line.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param basisCurve the original curve before degeneration
 */
public record StepDegenerateCurve(
    int id,
    String name,
    StepEntity basisCurve) implements StepEntity {
}
