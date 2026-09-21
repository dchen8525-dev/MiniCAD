package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepBSplineCurveWithKnotsAndBreakpoints extends AbstractStepControlPointCurve {
    private final List<Integer> knotMultiplicities;
    private final List<Double> knots;
    private final List<Double> breakpoints;
    private final boolean closedCurve;
    private final boolean selfIntersect;

    public StepBSplineCurveWithKnotsAndBreakpoints(int id, String name, int degree, List<StepCartesianPoint> controlPoints, List<Integer> knotMultiplicities, List<Double> knots, List<Double> breakpoints, String curveForm, boolean closedCurve, boolean selfIntersect) {
        super(id, name, degree, controlPoints, curveForm);
        this.knotMultiplicities = knotMultiplicities == null ? null : java.util.List.copyOf(knotMultiplicities);
        this.knots = knots == null ? null : java.util.List.copyOf(knots);
        this.breakpoints = breakpoints == null ? null : java.util.List.copyOf(breakpoints);
        this.closedCurve = closedCurve;
        this.selfIntersect = selfIntersect;
    }

    public List<Integer> getKnotMultiplicities() {
        return knotMultiplicities;
    }

    public List<Double> getKnots() {
        return knots;
    }

    public List<Double> getBreakpoints() {
        return breakpoints;
    }

    public boolean isClosedCurve() {
        return closedCurve;
    }

    public boolean isSelfIntersect() {
        return selfIntersect;
    }

    // Record-style accessors
    public List<Integer> knotMultiplicities() { return getKnotMultiplicities(); }
    public List<Double> knots() { return getKnots(); }
    public List<Double> breakpoints() { return getBreakpoints(); }
    public boolean closedCurve() { return isClosedCurve(); }
    public boolean selfIntersect() { return isSelfIntersect(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("degree", getDegree());
        state.put("controlPoints", getControlPoints());
        state.put("knotMultiplicities", knotMultiplicities);
        state.put("knots", knots);
        state.put("breakpoints", breakpoints);
        state.put("curveForm", getCurveForm());
        state.put("closedCurve", closedCurve);
        state.put("selfIntersect", selfIntersect);
        return state;
    }
}