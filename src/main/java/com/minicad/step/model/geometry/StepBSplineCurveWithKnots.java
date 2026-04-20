package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved B_SPLINE_CURVE_WITH_KNOTS.
 *
 * @param id step id
 * @param name step label
 * @param degree spline degree
 * @param controlPoints control-point references
 * @param curveForm curve form enum
 * @param closedCurve closed flag
 * @param selfIntersect self-intersection flag
 * @param knotMultiplicities multiplicities
 * @param knots knot values
 * @param knotSpec knot-spec enum
 */
public record StepBSplineCurveWithKnots(
        int id,
        String name,
        int degree,
        List<StepCartesianPoint> controlPoints,
        String curveForm,
        boolean closedCurve,
        boolean selfIntersect,
        List<Integer> knotMultiplicities,
        List<Double> knots,
        String knotSpec
) implements StepEntity {

    /**
     * Creates an immutable spline-curve record.
     */
    public StepBSplineCurveWithKnots {
        controlPoints = List.copyOf(controlPoints);
        knotMultiplicities = List.copyOf(knotMultiplicities);
        knots = List.copyOf(knots);
    }
}
