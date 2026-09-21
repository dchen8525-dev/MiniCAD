package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepBSplineCurveWithKnots extends AbstractStepControlPointCurve {
    private final boolean closedCurve;
    private final boolean selfIntersect;
    private final List<Integer> knotMultiplicities;
    private final List<Double> knots;
    private final String knotSpec;

    public StepBSplineCurveWithKnots(int id, String name, int degree, List<StepCartesianPoint> controlPoints, String curveForm, boolean closedCurve, boolean selfIntersect, List<Integer> knotMultiplicities, List<Double> knots, String knotSpec) {
        super(id, name, degree, controlPoints, curveForm);
        this.closedCurve = closedCurve;
        this.selfIntersect = selfIntersect;
        this.knotMultiplicities = knotMultiplicities == null ? null : java.util.List.copyOf(knotMultiplicities);
        this.knots = knots == null ? null : java.util.List.copyOf(knots);
        this.knotSpec = knotSpec;
    }

    public boolean isClosedCurve() {
        return closedCurve;
    }

    public boolean isSelfIntersect() {
        return selfIntersect;
    }

    public List<Integer> getKnotMultiplicities() {
        return knotMultiplicities;
    }

    public List<Double> getKnots() {
        return knots;
    }

    public String getKnotSpec() {
        return knotSpec;
    }

    // Record-style accessors
    public boolean closedCurve() { return isClosedCurve(); }
    public boolean selfIntersect() { return isSelfIntersect(); }
    public List<Integer> knotMultiplicities() { return getKnotMultiplicities(); }
    public List<Double> knots() { return getKnots(); }
    public String knotSpec() { return getKnotSpec(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("degree", getDegree());
        state.put("controlPoints", getControlPoints());
        state.put("curveForm", getCurveForm());
        state.put("closedCurve", closedCurve);
        state.put("selfIntersect", selfIntersect);
        state.put("knotMultiplicities", knotMultiplicities);
        state.put("knots", knots);
        state.put("knotSpec", knotSpec);
        return state;
    }
}
