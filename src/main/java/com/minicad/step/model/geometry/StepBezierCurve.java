package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved BEZIER_CURVE marker with inherited B-spline data when present.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 * @param degree spline degree, or {@code -1} when this is only a marker
 * @param controlPoints control-point references
 * @param curveForm curve form enum
 * @param closedCurve closed flag
 * @param selfIntersect self-intersection flag
 */
public record StepBezierCurve(
        int id,
        String name,
        int degree,
        List<StepCartesianPoint> controlPoints,
        String curveForm,
        boolean closedCurve,
        boolean selfIntersect
) implements StepEntity {

    public StepBezierCurve {
        controlPoints = List.copyOf(controlPoints);
    }

    public StepBezierCurve(int id, String name) {
        this(id, name, -1, List.of(), "", false, false);
    }
}
