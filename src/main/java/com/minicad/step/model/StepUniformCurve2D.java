package com.minicad.step.model;

import java.util.List;

/**
 * Resolved UNIFORM_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the curve
 * @param controlPoints control points in 2D
 * @param curveForm the form of the curve
 */
public record StepUniformCurve2D(
        int id,
        String name,
        int degree,
        List<StepCartesianPoint> controlPoints,
        String curveForm
) implements StepEntity {
    public StepUniformCurve2D {
        controlPoints = List.copyOf(controlPoints);
    }
}
