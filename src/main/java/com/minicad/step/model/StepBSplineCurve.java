package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepBSplineCurve extends AbstractStepControlPointCurve {
    private final boolean closedCurve;
    private final boolean selfIntersect;

    public StepBSplineCurve(int id, String name, int degree, List<StepCartesianPoint> controlPoints, String curveForm, boolean closedCurve, boolean selfIntersect) {
        super(id, name, degree, controlPoints, curveForm);
        this.closedCurve = closedCurve;
        this.selfIntersect = selfIntersect;
    }

    public boolean isClosedCurve() {
        return closedCurve;
    }

    public boolean isSelfIntersect() {
        return selfIntersect;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "degree", getDegree(), "controlPoints", getControlPoints(),
                "curveForm", getCurveForm(), "closedCurve", closedCurve, "selfIntersect", selfIntersect);
    }
}
