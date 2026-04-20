package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved RATIONAL_B_SPLINE_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the B-spline
 * @param controlPoints control points in 2D
 * @param weights weights for each control point
 * @param curveForm the form of the curve
 */
public record StepRationalBSplineCurve2D(
        int id,
        String name,
        int degree,
        List<StepCartesianPoint> controlPoints,
        List<Double> weights,
        String curveForm
) implements StepEntity {
    public StepRationalBSplineCurve2D {
        controlPoints = List.copyOf(controlPoints);
        weights = List.copyOf(weights);
    }
}
