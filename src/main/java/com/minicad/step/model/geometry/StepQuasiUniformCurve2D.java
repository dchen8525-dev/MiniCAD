package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved QUASI_UNIFORM_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the curve
 * @param controlPoints control points in 2D
 * @param curveForm the form of the curve
 */
public record StepQuasiUniformCurve2D(
        int id,
        String name,
        int degree,
        List<StepCartesianPoint> controlPoints,
        String curveForm
) implements StepEntity {
    public StepQuasiUniformCurve2D {
        controlPoints = List.copyOf(controlPoints);
    }
}
