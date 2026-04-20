package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved CURVE_2D.
 * A 2D curve defined by a placement and parametric equation.
 *
 * @param id step id
 * @param name step label
 * @param position the 2D placement
 * @param equation the parametric equation coefficients
 */
public record StepCurve2D(
        int id,
        String name,
        StepAxis2Placement2D position,
        double[] equation
) implements StepEntity {
    public StepCurve2D {
        equation = equation.clone();
    }
}
