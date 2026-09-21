package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved UNIFORM_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param degree degree of the curve
 * @param controlPoints control points in 2D
 * @param curveForm the form of the curve
 */
public final class StepUniformCurve2D extends AbstractStepControlPointCurve {
    public StepUniformCurve2D(int id, String name, int degree, List<StepCartesianPoint> controlPoints, String curveForm) {
        super(id, name, degree, controlPoints, curveForm);
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("degree", getDegree());
        state.put("controlPoints", getControlPoints());
        state.put("curveForm", getCurveForm());
        return state;
    }
}
