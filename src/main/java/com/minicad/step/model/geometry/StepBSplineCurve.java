package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal B_SPLINE_CURVE without knot data.
 *
 * @param id step id
 * @param name step label
 * @param degree spline degree
 * @param controlPoints control-point references
 * @param curveForm curve form enum
 * @param closedCurve closed flag
 * @param selfIntersect self-intersection flag
 */
public record StepBSplineCurve(
        int id,
        String name,
        int degree,
        List<StepCartesianPoint> controlPoints,
        String curveForm,
        boolean closedCurve,
        boolean selfIntersect
) implements StepEntity {

    public StepBSplineCurve {
        controlPoints = List.copyOf(controlPoints);
    }
}
