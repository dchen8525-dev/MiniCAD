package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepBSplineCurveWithKnotsAndBreakpoints implements StepEntity {
    private final int id;
    private final String name;
    private final int degree;
    private final List<StepCartesianPoint> controlPoints;
    private final List<Integer> knotMultiplicities;
    private final List<Double> knots;
    private final List<Double> breakpoints;
    private final String curveForm;
    private final boolean closedCurve;
    private final boolean selfIntersect;

    public StepBSplineCurveWithKnotsAndBreakpoints(int id, String name, int degree, List<StepCartesianPoint> controlPoints, List<Integer> knotMultiplicities, List<Double> knots, List<Double> breakpoints, String curveForm, boolean closedCurve, boolean selfIntersect) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.knotMultiplicities = knotMultiplicities == null ? null : java.util.List.copyOf(knotMultiplicities);
        this.knots = knots == null ? null : java.util.List.copyOf(knots);
        this.breakpoints = breakpoints == null ? null : java.util.List.copyOf(breakpoints);
        this.curveForm = curveForm;
        this.closedCurve = closedCurve;
        this.selfIntersect = selfIntersect;
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

    public List<Integer> getKnotMultiplicities() {
        return knotMultiplicities;
    }

    public List<Double> getKnots() {
        return knots;
    }

    public List<Double> getBreakpoints() {
        return breakpoints;
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

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public int degree() { return getDegree(); }
    public List<StepCartesianPoint> controlPoints() { return getControlPoints(); }
    public List<Integer> knotMultiplicities() { return getKnotMultiplicities(); }
    public List<Double> knots() { return getKnots(); }
    public List<Double> breakpoints() { return getBreakpoints(); }
    public String curveForm() { return getCurveForm(); }
    public boolean closedCurve() { return isClosedCurve(); }
    public boolean selfIntersect() { return isSelfIntersect(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBSplineCurveWithKnotsAndBreakpoints that = (StepBSplineCurveWithKnotsAndBreakpoints) o;
        return id == that.id && Objects.equals(name, that.name) && degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(knotMultiplicities, that.knotMultiplicities) && Objects.equals(knots, that.knots) && Objects.equals(breakpoints, that.breakpoints) && Objects.equals(curveForm, that.curveForm) && closedCurve == that.closedCurve && selfIntersect == that.selfIntersect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, degree, controlPoints, knotMultiplicities, knots, breakpoints, curveForm, closedCurve, selfIntersect);
    }

    @Override
    public String toString() {
        return "StepBSplineCurveWithKnotsAndBreakpoints{" + "id=" + id + "name=" + name + "degree=" + degree + "controlPoints=" + controlPoints + "knotMultiplicities=" + knotMultiplicities + "knots=" + knots + "breakpoints=" + breakpoints + "curveForm=" + curveForm + "closedCurve=" + closedCurve + "selfIntersect=" + selfIntersect + "}";
    }
}