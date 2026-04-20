package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved DEGENERATE_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param point the degenerate point
 */
public record StepDegenerateCurve2D(
        int id,
        String name,
        StepCartesianPoint point
) implements StepEntity {
}
