package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved PIECEWISE_BEZIER_CURVE marker with inherited B-spline data when present.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 * @param degree spline degree, or {@code -1} when this is only a marker
 * @param controlPoints control-point references
 * @param curveForm curve form enum
 * @param closedCurve closed flag
 * @param selfIntersect self-intersection flag
 */
public final class StepPiecewiseBezierCurve extends AbstractStepControlPointCurve {
    private final boolean closedCurve;
    private final boolean selfIntersect;

    public StepPiecewiseBezierCurve(int id, String name, int degree, List<StepCartesianPoint> controlPoints, String curveForm, boolean closedCurve, boolean selfIntersect) {
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
