package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepRationalBSplineCurve2D extends AbstractStepControlPointCurve {
    private final List<Double> weights;

    public StepRationalBSplineCurve2D(int id, String name, int degree, List<StepCartesianPoint> controlPoints, List<Double> weights, String curveForm) {
        super(id, name, degree, controlPoints, curveForm);
        this.weights = weights == null ? null : java.util.List.copyOf(weights);
    }

    public List<Double> getWeights() {
        return weights;
    }

    // Record-style accessors
    public List<Double> weights() { return getWeights(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("degree", getDegree());
        state.put("controlPoints", getControlPoints());
        state.put("weights", weights);
        state.put("curveForm", getCurveForm());
        return state;
    }
}
