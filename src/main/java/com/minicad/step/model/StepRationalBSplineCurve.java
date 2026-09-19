package com.minicad.step.model;

import java.util.List;
import java.util.Objects;

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
public final class StepRationalBSplineCurve extends AbstractStepControlPointCurve {
    private final boolean closedCurve;
    private final boolean selfIntersect;
    private final List<Double> weightsData;
    private final List<Integer> knotMultiplicities;
    private final List<Double> knots;
    private final String knotSpec;

    public StepRationalBSplineCurve(int id, String name, int degree, List<StepCartesianPoint> controlPoints, String curveForm, boolean closedCurve, boolean selfIntersect, List<Double> weightsData, List<Integer> knotMultiplicities, List<Double> knots, String knotSpec) {
        super(id, name, degree, controlPoints, curveForm);
        this.closedCurve = closedCurve;
        this.selfIntersect = selfIntersect;
        this.weightsData = weightsData == null ? null : java.util.List.copyOf(weightsData);
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

    public List<Double> getWeightsData() {
        return weightsData;
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
    public List<Double> weightsData() { return getWeightsData(); }
    public List<Integer> knotMultiplicities() { return getKnotMultiplicities(); }
    public List<Double> knots() { return getKnots(); }
    public String knotSpec() { return getKnotSpec(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRationalBSplineCurve that = (StepRationalBSplineCurve) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName()) && getDegree() == that.getDegree() && Objects.equals(getControlPoints(), that.getControlPoints()) && Objects.equals(getCurveForm(), that.getCurveForm()) && closedCurve == that.closedCurve && selfIntersect == that.selfIntersect && Objects.equals(weightsData, that.weightsData) && Objects.equals(knotMultiplicities, that.knotMultiplicities) && Objects.equals(knots, that.knots) && Objects.equals(knotSpec, that.knotSpec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDegree(), getControlPoints(), getCurveForm(), closedCurve, selfIntersect, weightsData, knotMultiplicities, knots, knotSpec);
    }

    @Override
    public String toString() {
        return "StepRationalBSplineCurve{" + "id=" + getId() + "name=" + getName() + "degree=" + getDegree() + "controlPoints=" + getControlPoints() + "curveForm=" + getCurveForm() + "closedCurve=" + closedCurve + "selfIntersect=" + selfIntersect + "weightsData=" + weightsData + "knotMultiplicities=" + knotMultiplicities + "knots=" + knots + "knotSpec=" + knotSpec + "}";
    }
}
