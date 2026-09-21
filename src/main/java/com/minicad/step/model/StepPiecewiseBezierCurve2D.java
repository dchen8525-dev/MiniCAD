package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PIECEWISE_BEZIER_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the curve
 * @param controlPoints control points in 2D
 */
public final class StepPiecewiseBezierCurve2D extends AbstractStepEntity {
    private final int degree;
    private final List<StepCartesianPoint> controlPoints;

    public StepPiecewiseBezierCurve2D(int id, String name, int degree, List<StepCartesianPoint> controlPoints) {
        super(id, name);
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
    }

    public int getDegree() {
        return degree;
    }

    public List<StepCartesianPoint> getControlPoints() {
        return controlPoints;
    }

    // Record-style accessors
    public int degree() { return getDegree(); }
    public List<StepCartesianPoint> controlPoints() { return getControlPoints(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("degree", degree);
        state.put("controlPoints", controlPoints);
        return state;
    }
}
