package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
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
public final class StepBSplineCurveWithKnots implements StepEntity {
    private final int id;
    private final String name;
    private final int degree;
    private final List<StepCartesianPoint> controlPoints;
    private final String curveForm;
    private final boolean closedCurve;
    private final boolean selfIntersect;
    private final List<Integer> knotMultiplicities;
    private final List<Double> knots;
    private final String knotSpec;

    public StepBSplineCurveWithKnots(int id, String name, int degree, List<StepCartesianPoint> controlPoints, String curveForm, boolean closedCurve, boolean selfIntersect, List<Integer> knotMultiplicities, List<Double> knots, String knotSpec) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.curveForm = curveForm;
        this.closedCurve = closedCurve;
        this.selfIntersect = selfIntersect;
        this.knotMultiplicities = knotMultiplicities == null ? null : java.util.List.copyOf(knotMultiplicities);
        this.knots = knots == null ? null : java.util.List.copyOf(knots);
        this.knotSpec = knotSpec;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDegree() {
        return degree;
    }

    public List<StepCartesianPoint> getControlPoints() {
        return controlPoints;
    }

    public String getCurveForm() {
        return curveForm;
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
    public int id() { return getId(); }
    public String name() { return getName(); }
    public int degree() { return getDegree(); }
    public List<StepCartesianPoint> controlPoints() { return getControlPoints(); }
    public String curveForm() { return getCurveForm(); }
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
        return id == that.id && Objects.equals(name, that.name) && degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(curveForm, that.curveForm) && closedCurve == that.closedCurve && selfIntersect == that.selfIntersect && Objects.equals(knotMultiplicities, that.knotMultiplicities) && Objects.equals(knots, that.knots) && Objects.equals(knotSpec, that.knotSpec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, degree, controlPoints, curveForm, closedCurve, selfIntersect, knotMultiplicities, knots, knotSpec);
    }

    @Override
    public String toString() {
        return "StepBSplineCurveWithKnots{" + "id=" + id + "name=" + name + "degree=" + degree + "controlPoints=" + controlPoints + "curveForm=" + curveForm + "closedCurve=" + closedCurve + "selfIntersect=" + selfIntersect + "knotMultiplicities=" + knotMultiplicities + "knots=" + knots + "knotSpec=" + knotSpec + "}";
    }
}
