package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal rational B-spline curve.
 *
 * @param id step id
 * @param name step label
 * @param degree spline degree
 * @param controlPoints control-point references
 * @param curveForm curve form enum
 * @param closedCurve closed flag
 * @param selfIntersect self-intersection flag
 * @param weightsData rational weights
 * @param knotMultiplicities optional knot multiplicities
 * @param knots optional knot values
 * @param knotSpec optional knot-spec enum
 */
public record StepRationalBSplineCurve(
        int id,
        String name,
        int degree,
        List<StepCartesianPoint> controlPoints,
        String curveForm,
        boolean closedCurve,
        boolean selfIntersect,
        List<Double> weightsData,
        List<Integer> knotMultiplicities,
        List<Double> knots,
        String knotSpec
) implements StepEntity {

    public StepRationalBSplineCurve {
        controlPoints = List.copyOf(controlPoints);
        weightsData = List.copyOf(weightsData);
        knotMultiplicities = List.copyOf(knotMultiplicities);
        knots = List.copyOf(knots);
    }
}
