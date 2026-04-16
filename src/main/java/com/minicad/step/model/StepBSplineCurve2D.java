package com.minicad.step.model;

import java.util.List;

/**
 * Resolved B_SPLINE_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the B-spline
 * @param controlPoints control points in 2D
 * @param curveForm the form of the curve
 */
public record StepBSplineCurve2D(
        int id,
        String name,
        int degree,
        List<StepCartesianPoint> controlPoints,
        String curveForm
) implements StepEntity {
    public StepBSplineCurve2D {
        controlPoints = List.copyOf(controlPoints);
    }
}
