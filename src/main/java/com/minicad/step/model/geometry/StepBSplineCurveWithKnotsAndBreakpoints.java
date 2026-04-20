package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS.
 * A B-spline curve with explicit knot and breakpoint information.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param degree polynomial degree
 * @param controlPoints control point entities
 * @param knotMultiplicities knot multiplicity values
 * @param knots knot values
 * @param breakpoints breakpoint parameter values
 * @param curveForm curve form indicator
 * @param closedCurve whether the curve is closed
 * @param selfIntersect whether the curve self-intersects
 */
public record StepBSplineCurveWithKnotsAndBreakpoints(
    int id,
    String name,
    int degree,
    List<StepCartesianPoint> controlPoints,
    List<Integer> knotMultiplicities,
    List<Double> knots,
    List<Double> breakpoints,
    String curveForm,
    boolean closedCurve,
    boolean selfIntersect) implements StepEntity {
}