package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved ELLIPSE.
 *
 * @param id step id
 * @param name step label
 * @param position ellipse placement
 * @param semiAxis1 local X semi-axis
 * @param semiAxis2 local Y semi-axis
 */
public record StepEllipse(
        int id,
        String name,
        StepEntity position,
        double semiAxis1,
        double semiAxis2
) implements StepEntity {
}
