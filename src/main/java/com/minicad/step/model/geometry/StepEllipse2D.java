package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved ELLIPSE_2D.
 * An ellipse in 2D parameter space.
 *
 * @param id step id
 * @param name step label
 * @param position 2D placement (center and direction)
 * @param semiAxis1 semi-major axis length
 * @param semiAxis2 semi-minor axis length
 */
public record StepEllipse2D(
        int id,
        String name,
        StepAxis2Placement2D position,
        double semiAxis1,
        double semiAxis2
) implements StepEntity {
}
