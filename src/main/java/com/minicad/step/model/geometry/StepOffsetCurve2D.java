package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal OFFSET_CURVE_2D parse-only curve.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param basisCurve curve being offset
 * @param distance offset distance
 * @param selfIntersect self-intersection flag
 */
public record StepOffsetCurve2D(
        int id,
        String name,
        StepEntity basisCurve,
        double distance,
        boolean selfIntersect
) implements StepEntity {
}
