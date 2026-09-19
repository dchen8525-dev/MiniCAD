package com.minicad.step.model;

import java.util.List;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBSplineCurveWithKnots that = (StepBSplineCurveWithKnots) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName()) && getDegree() == that.getDegree() && Objects.equals(getControlPoints(), that.getControlPoints()) && Objects.equals(getCurveForm(), that.getCurveForm()) && closedCurve == that.closedCurve && selfIntersect == that.selfIntersect && Objects.equals(knotMultiplicities, that.knotMultiplicities) && Objects.equals(knots, that.knots) && Objects.equals(knotSpec, that.knotSpec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDegree(), getControlPoints(), getCurveForm(), closedCurve, selfIntersect, knotMultiplicities, knots, knotSpec);
    }

    @Override
    public String toString() {
        return "StepBSplineCurveWithKnots{" + "id=" + getId() + "name=" + getName() + "degree=" + getDegree() + "controlPoints=" + getControlPoints() + "curveForm=" + getCurveForm() + "closedCurve=" + closedCurve + "selfIntersect=" + selfIntersect + "knotMultiplicities=" + knotMultiplicities + "knots=" + knots + "knotSpec=" + knotSpec + "}";
    }
}
