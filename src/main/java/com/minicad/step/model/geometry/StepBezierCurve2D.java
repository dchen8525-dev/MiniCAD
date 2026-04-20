package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved BEZIER_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the Bezier curve
 * @param controlPoints control points in 2D
 */
public record StepBezierCurve2D(
        int id,
        String name,
        int degree,
        List<StepCartesianPoint> controlPoints
) implements StepEntity {
    public StepBezierCurve2D {
        controlPoints = List.copyOf(controlPoints);
    }
}
